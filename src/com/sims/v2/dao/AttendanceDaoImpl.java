package com.sims.v2.dao;

import com.sims.v2.model.*;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDaoImpl extends IOFileDao implements AttendanceDao {

    public AttendanceDaoImpl(){
    }

    @Override
    public List<Attendance> getList(){
        List<Attendance> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attendance> criteria = builder.createQuery(Attendance.class);
            Root<Attendance> attendanceRoot = criteria.from(Attendance.class);
            attendanceRoot.join(Attendance_.student);
            attendanceRoot.join(Attendance_.calendar);
            criteria.select(attendanceRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Attendance getAttendanceById(Student student, Calendar calendar){
        Attendance attendance = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attendance> criteria = builder.createQuery(Attendance.class);
            Root<Attendance> attendanceRoot = criteria.from(Attendance.class);
            criteria.select(attendanceRoot);
            criteria.where( builder.and(
                    builder.equal(attendanceRoot.get(Attendance_.student), student ),
                    builder.equal(attendanceRoot.get(Attendance_.calendar), calendar )) );
            attendance = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return attendance;
    }

    @Override
    public List<Attendance> getAttendanceByCalendar(Calendar calendar){
        List<Attendance> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attendance> criteria = builder.createQuery(Attendance.class);
            Root<Attendance> attendanceRoot = criteria.from(Attendance.class);
            criteria.select(attendanceRoot);
            criteria.where(builder.equal(attendanceRoot.get(Attendance_.calendar), calendar ));
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public List<Attendance> getListByStudent(String code){
        List<Attendance> list = new ArrayList<>();
        StudentDao studentDao = new StudentDaoImpl();
        Student student = studentDao.getStudentByCode(code);
        if (student != null){
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Attendance> criteria = builder.createQuery(Attendance.class);
                Root<Attendance> attendanceRoot = criteria.from(Attendance.class);
                criteria.select(attendanceRoot);
                criteria.where(builder.equal(attendanceRoot.get(Attendance_.student), student ));
                list = session.createQuery(criteria).getResultList();
            } catch (HibernateException ex) {
                System.err.println(ex);
            } finally {
                session.close();
            }
        }

        return list;
    }

    @Override
    public boolean addOne(Attendance attendance){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(attendance);
            transaction.commit();
            return true;
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println(ex);
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean updateOne(Attendance attendance){
        return updateField(attendance);
    }

    @Override
    public boolean deleteOne(Attendance attendance){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(attendance);
            transaction.commit();
            return true;
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println(ex);
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean deleteAll(){
        List<Attendance> attendances = getList();
        for (Attendance attendance : attendances){
            deleteOne(attendance);
        }
        return true;
    }

    @Override
    public boolean updateAll(List<Attendance> attendances){
        for (Attendance attendance : attendances){
            updateOne(attendance);
        }
        return true;
    }

    @Override
    public List<Attendance> importFile(String path) {
        CalendarDao calendarDao = new CalendarDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        List<Attendance> list = getList();
        Student student = null;
        Calendar calendar = null;
        List<String[]> data = readFile(path, ",");

        int i = 0;
        for (String[] arr : data){
            if (i == 0){
                String calendarName = arr[0].trim();
                if (!calendarName.isEmpty()){
                    calendar = calendarDao.getCalendarByName(calendarName);
                    if (calendar == null){
                        return list;
                    }
                }
            }
            else if (i > 1){
                boolean checkCode = true;
                String studentCode = arr[1].trim();
                if (!studentCode.isEmpty()){
                    student = studentDao.getStudentByCode(studentCode);
                    if (student == null){
                        checkCode = false;
                    }
                }


                if (checkCode == true) {
                    boolean checkExists = false;
                    for (Attendance t : list){
                        if (student.getId().equals(t.getStudent().getId()) && calendar.getClasses().equals(t.getCalendar().getClasses()) && calendar.getSubject().equals(t.getCalendar().getSubject())){
                            checkExists = true;
                            break;
                        }
                    }
                    if (checkExists == false){
                        Attendance attendance = new Attendance();
                        attendance.setStudent(student);
                        attendance.setCalendar(calendar);
                        attendance.setMiddleMark(Float.parseFloat(arr[3]));
                        attendance.setFinalMark(Float.parseFloat(arr[4]));
                        attendance.setOtherMark(Float.parseFloat(arr[5]));
                        attendance.setMark(Float.parseFloat(arr[6]));
                        addOne(attendance);
                    }
                    else {
                        Attendance attendance = getAttendanceById(student, calendar);
                        attendance.setMiddleMark(Float.parseFloat(arr[3]));
                        attendance.setFinalMark(Float.parseFloat(arr[4]));
                        attendance.setOtherMark(Float.parseFloat(arr[5]));
                        attendance.setMark(Float.parseFloat(arr[6]));
                        updateOne(attendance);
                    }
                }
            }
            i++;
        }

        list = getList();
        return list;
    }

    private boolean updateField(Attendance attendance){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Attendance> criteriaUpdate = builder.createCriteriaUpdate(Attendance.class);
            Root<Attendance> attendanceRoot = criteriaUpdate.from(Attendance.class);
            criteriaUpdate.set(attendanceRoot.get(Attendance_.middleMark), attendance.getMiddleMark());
            criteriaUpdate.set(attendanceRoot.get(Attendance_.finalMark), attendance.getFinalMark());
            criteriaUpdate.set(attendanceRoot.get(Attendance_.otherMark), attendance.getOtherMark());
            criteriaUpdate.set(attendanceRoot.get(Attendance_.mark), attendance.getMark());
            criteriaUpdate.where( builder.and(
                    builder.equal(attendanceRoot.get(Attendance_.student), attendance.getStudent() ),
                    builder.equal(attendanceRoot.get(Attendance_.calendar), attendance.getCalendar() )) );
            session.createQuery(criteriaUpdate).executeUpdate();
            transaction.commit();
            return true;
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println(ex);
        } finally {
            session.close();
        }
        return false;
    }

}
