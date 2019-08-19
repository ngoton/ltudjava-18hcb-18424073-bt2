package com.sims.v2.dao;

import com.sims.v2.model.*;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDaoImpl implements ApplicationDao {

    public ApplicationDaoImpl(){
    }

    @Override
    public List<Application> getList(){
        List<Application> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
            Root<Application> applicationRoot = criteria.from(Application.class);
            applicationRoot.join(Application_.remarking);
            applicationRoot.join(Application_.attendance);
            criteria.select(applicationRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public List<Application> getListByStudent(String code){
        List<Application> list = new ArrayList<>();
        StudentDao studentDao = new StudentDaoImpl();
        Student student = studentDao.getStudentByCode(code);
        if (student != null){
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
                Root<Application> applicationRoot = criteria.from(Application.class);
                Join<Application, Attendance> attendance = applicationRoot.join( Application_.attendance );
                Join<Attendance, Student> students = attendance.join( Attendance_.student );
                criteria.select(applicationRoot);
                criteria.where(builder.equal(students, student ));
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
    public List<Application> getApplicationByRemarking(Remarking remarking){
        List<Application> list = new ArrayList<>();
        if (remarking != null){
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
                Root<Application> applicationRoot = criteria.from(Application.class);
                Join<Application, Remarking> remarkings = applicationRoot.join( Application_.remarking );
                criteria.select(applicationRoot);
                criteria.where(builder.equal(remarkings, remarking ));
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
    public List<Application> getApplicationByAttendance(Attendance attendance){
        List<Application> list = new ArrayList<>();
        if (attendance != null){
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
                Root<Application> applicationRoot = criteria.from(Application.class);
                Join<Application, Attendance> attendances = applicationRoot.join( Application_.attendance );
                criteria.select(applicationRoot);
                criteria.where(builder.equal(attendances, attendance ));
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
    public boolean addOne(Application application){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(application);
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
    public boolean updateOne(Application application){
        return updateField(application);
    }

    @Override
    public boolean deleteOne(Application application){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(application);
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
    public boolean deleteAllByStudent(String code){
        List<Application> applications = getListByStudent(code);
        for (Application application : applications){
            if (application.getStatus() == null){
                deleteOne(application);
            }
        }
        return true;
    }

    @Override
    public boolean deleteAll(){
        List<Application> applications = getList();
        for (Application application : applications){
            deleteOne(application);
        }
        return true;
    }

    @Override
    public boolean updateAll(List<Application> applications){
        for (Application application : applications){
            updateOne(application);
        }
        return true;
    }

    private boolean updateField(Application application){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Application> criteriaUpdate = builder.createCriteriaUpdate(Application.class);
            Root<Application> applicationRoot = criteriaUpdate.from(Application.class);
            criteriaUpdate.set(applicationRoot.get(Application_.reason), application.getReason());
            criteriaUpdate.set(applicationRoot.get(Application_.middleExpect), application.getMiddleExpect());
            criteriaUpdate.set(applicationRoot.get(Application_.finalExpect), application.getFinalExpect());
            criteriaUpdate.set(applicationRoot.get(Application_.otherExpect), application.getOtherExpect());
            criteriaUpdate.set(applicationRoot.get(Application_.markExpect), application.getMarkExpect());
            criteriaUpdate.set(applicationRoot.get(Application_.newMiddle), application.getNewMiddle());
            criteriaUpdate.set(applicationRoot.get(Application_.newFinal), application.getNewFinal());
            criteriaUpdate.set(applicationRoot.get(Application_.newOther), application.getNewOther());
            criteriaUpdate.set(applicationRoot.get(Application_.newMark), application.getNewMark());
            criteriaUpdate.set(applicationRoot.get(Application_.status), application.getStatus());
            criteriaUpdate.where( builder.and(
                    builder.equal(applicationRoot.get(Application_.attendance), application.getAttendance() ),
                    builder.equal(applicationRoot.get(Application_.remarking), application.getRemarking() )) );
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
