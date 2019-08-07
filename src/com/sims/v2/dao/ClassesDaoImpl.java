package com.sims.v2.dao;

import com.sims.v2.model.Classes;
import com.sims.v2.model.Classes_;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClassesDaoImpl implements ClassesDao {

    public ClassesDaoImpl(){

    }

    @Override
    public List<Classes> getList(){
        List<Classes> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Classes> criteria = builder.createQuery(Classes.class);
            Root<Classes> classesRoot = criteria.from(Classes.class);
            criteria.select(classesRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Classes getClassById(Integer id){
        Classes classes = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            classes = session.get(Classes.class, id);
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return classes;
    }

    @Override
    public Classes getClassByName(String name){
        Classes classes = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Classes> criteria = builder.createQuery(Classes.class);
            Root<Classes> classesRoot = criteria.from(Classes.class);
            criteria.select(classesRoot);
            criteria.where(builder.equal(classesRoot.get(Classes_.name), name ));
            classes = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return classes;
    }

    @Override
    public boolean addOne(Classes classes){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            transaction.begin();
            session.save(classes);
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
