package com.sims.v2.dao;

import com.sims.v2.model.*;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarDaoImpl extends IOFileDao implements CalendarDao {
    private ClassesDao classesDao;
    private SubjectDao subjectDao;
    private StudentDao studentDao;
    private AttendanceDao attendanceDao;

    public CalendarDaoImpl(){
        this.classesDao = new ClassesDaoImpl();
        this.subjectDao = new SubjectDaoImpl();
        this.studentDao = new StudentDaoImpl();
        this.attendanceDao = new AttendanceDaoImpl();
    }

    @Override
    public List<Calendar> getList(){
        List<Calendar> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Calendar> criteria = builder.createQuery(Calendar.class);
            Root<Calendar> calendarRoot = criteria.from(Calendar.class);
            calendarRoot.join(Calendar_.classes);
            calendarRoot.join(Calendar_.subject);
            criteria.select(calendarRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public List<Calendar> getListByStudent(String code){
        List<Calendar> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Calendar> criteria = builder.createQuery(Calendar.class);
            Root<Calendar> calendarRoot = criteria.from(Calendar.class);
            criteria.select(calendarRoot);
            Join<Calendar, Attendance> attendances = calendarRoot.join(Calendar_.attendances);
            Join<Attendance, Student> student = attendances.join(Attendance_.student);
            criteria.where(builder.equal(student.get(Student_.code), code ));
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Calendar getCalendarById(Classes classes, Subject subject){
        Calendar calendar = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Calendar> criteria = builder.createQuery(Calendar.class);
            Root<Calendar> calendarRoot = criteria.from(Calendar.class);
            criteria.select(calendarRoot);
            criteria.where(builder.equal(calendarRoot.get(Calendar_.classes), classes ));
            criteria.where(builder.equal(calendarRoot.get(Calendar_.subject), subject ));
            calendar = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return calendar;
    }

    @Override
    public Calendar getCalendarByName(String name){
        List<Calendar> calendars = this.getList();
        Calendar calendar = null;
        for (Calendar s : calendars){
            if (name.equals(s.getClasses().getName()+"-"+s.getSubject().getCode())){
                calendar = s;
                break;
            }
        }
        return calendar;
    }

    @Override
    public boolean addOne(Calendar calendar){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(calendar);
            transaction.commit();
            List<Student> studentList = studentDao.getStudentByClass(calendar.getClasses());
            for (Student s : studentList) {
                Attendance attendance = new Attendance();
                attendance.setStudent(s);
                attendance.setCalendar(calendar);
                attendanceDao.addOne(attendance);
            }
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
    public boolean updateOne(Calendar calendar){
        return updateField(calendar);
    }

    @Override
    public boolean deleteOne(Calendar calendar){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Attendance> attendanceList = attendanceDao.getAttendanceByCalendar(calendar);
            for (Attendance attendance : attendanceList) {
                attendanceDao.deleteOne(attendance);
            }
            session.delete(calendar);
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
        List<Calendar> calendars = getList();
        for (Calendar calendar : calendars){
            deleteOne(calendar);
        }
        return true;
    }

    @Override
    public List<Calendar> importFile(String path){
        List<Calendar> list = getList();
        Subject subject = null;
        Classes classes = null;
        List<String[]> data = readFile(path, ",");

        int i = 0;
        for (String[] arr : data){
            if (i == 0){
                String className = arr[0].trim();
                if (!className.isEmpty()){
                    classes = classesDao.getClassByName(className);
                    if (classes == null){
                        Classes c = new Classes();
                        c.setName(className);
                        if(classesDao.addOne(c)){
                            classes = c;
                        }
                        else {
                            return list;
                        }
                    }
                }
            }
            else if (i > 1){
                String subjectCode = arr[1].trim();
                if (!subjectCode.isEmpty()){
                    subject = subjectDao.getSubjectByCode(subjectCode);
                    if (subject == null){
                        Subject c = new Subject();
                        c.setCode(subjectCode);
                        c.setName(arr[2].trim());
                        if(subjectDao.addOne(c)){
                            subject = c;
                        }
                        else {
                            return list;
                        }
                    }
                }

                boolean checkCode = true;
                for (Calendar cl : list){
                    if (classes.getId().equals(cl.getClasses().getId()) && subject.getId().equals(cl.getSubject().getId())){
                        checkCode = false;
                        break;
                    }
                }

                if (checkCode == true) {
                    Calendar calendar = new Calendar();
                    calendar.setRoom(arr[3].trim());
                    calendar.setClasses(classes);
                    calendar.setSubject(subject);
                    addOne(calendar);
                }
            }
            i++;
        }

        list = getList();
        return list;
    }

    private boolean updateField(Calendar calendar){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Calendar> criteriaUpdate = builder.createCriteriaUpdate(Calendar.class);
            Root<Calendar> calendarRoot = criteriaUpdate.from(Calendar.class);
            criteriaUpdate.set(calendarRoot.get(Calendar_.room), calendar.getRoom());
            criteriaUpdate.where( builder.and(
                    builder.equal(calendarRoot.get(Calendar_.classes), calendar.getClasses()),
                    builder.equal(calendarRoot.get(Calendar_.subject), calendar.getSubject())) );
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
