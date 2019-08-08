package com.sims.v2.dao;

import com.sims.v2.model.Subject;
import com.sims.v2.model.Subject_;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoImpl implements SubjectDao {

    public SubjectDaoImpl(){

    }

    @Override
    public List<Subject> getList(){
        List<Subject> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Subject> criteria = builder.createQuery(Subject.class);
            Root<Subject> subjectRoot = criteria.from(Subject.class);
            criteria.select(subjectRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Subject getSubjectById(Integer id){
        Subject subject = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            subject = session.get(Subject.class, id);
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return subject;
    }

    @Override
    public Subject getSubjectByName(String name){
        Subject subject = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Subject> criteria = builder.createQuery(Subject.class);
            Root<Subject> subjectRoot = criteria.from(Subject.class);
            criteria.select(subjectRoot);
            criteria.where(builder.equal(subjectRoot.get(Subject_.name), name ));
            subject = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return subject;
    }

    @Override
    public Subject getSubjectByCode(String code){
        Subject subject = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Subject> criteria = builder.createQuery(Subject.class);
            Root<Subject> subjectRoot = criteria.from(Subject.class);
            criteria.select(subjectRoot);
            criteria.where(builder.equal(subjectRoot.get(Subject_.code), code ));
            subject = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return subject;
    }

    @Override
    public boolean addOne(Subject subject){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(subject);
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
