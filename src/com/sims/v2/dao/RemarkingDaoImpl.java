package com.sims.v2.dao;

import com.sims.v2.model.*;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemarkingDaoImpl implements RemarkingDao {

    public RemarkingDaoImpl(){
    }

    @Override
    public List<Remarking> getList(){
        List<Remarking> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Remarking> criteria = builder.createQuery(Remarking.class);
            Root<Remarking> remarkingRoot = criteria.from(Remarking.class);
            criteria.select(remarkingRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public List<Remarking> getListByDate(Date date){
        List<Remarking> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Remarking> criteria = builder.createQuery(Remarking.class);
            Root<Remarking> remarkingRoot = criteria.from(Remarking.class);
            criteria.select(remarkingRoot);
            criteria.where(builder.and(
                    builder.lessThanOrEqualTo(remarkingRoot.get(Remarking_.opening), date),
                    builder.greaterThanOrEqualTo(remarkingRoot.get(Remarking_.closing), date)
            ));
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Remarking getRemarkingById(String id){
        Remarking remarking = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Remarking> criteria = builder.createQuery(Remarking.class);
            Root<Remarking> remarkingRoot = criteria.from(Remarking.class);
            criteria.select(remarkingRoot);
            criteria.where(builder.equal(remarkingRoot.get(Remarking_.id), id ));
            remarking = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return remarking;
    }

    @Override
    public boolean addOne(Remarking remarking){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(remarking);
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
    public boolean updateOne(Remarking remarking){
        return updateField(remarking);
    }

    @Override
    public boolean deleteOne(Remarking remarking){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            /*List<Application> applicationList = applicationDao.getApplicationByRemarking(remarking);
            for (Application application : applicationList) {
                applicationDao.deleteOne(application);
            }*/
            session.delete(remarking);
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
        List<Remarking> remarkings = getList();
        for (Remarking remarking : remarkings){
            deleteOne(remarking);
        }
        return true;
    }

    private boolean updateField(Remarking remarking){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Remarking> criteriaUpdate = builder.createCriteriaUpdate(Remarking.class);
            Root<Remarking> remarkingRoot = criteriaUpdate.from(Remarking.class);
            criteriaUpdate.set(remarkingRoot.get(Remarking_.opening), remarking.getOpening());
            criteriaUpdate.set(remarkingRoot.get(Remarking_.closing), remarking.getClosing());
            criteriaUpdate.where(builder.equal(remarkingRoot.get(Remarking_.id), remarking.getId()));
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
