package com.sims.v2.dao;

import com.sims.v2.model.Student;
import com.sims.v2.model.User;
import com.sims.v2.model.User_;
import com.sims.v2.util.HibernateUtil;
import com.sims.v2.util.MD5Encrypt;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
    }

    @Override
    public User login(String username, String password){
        String pass = MD5Encrypt.convertHashToString(password);
        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot);
            criteria.where(builder.equal(userRoot.get(User_.username), username ));
            user = session.createQuery(criteria).uniqueResult();
            if (user != null){
                if (!pass.equals(user.getPassword())) {
                    return null;
                }
            }

        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public List<User> getList(){
        List<User> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public User getUserById(Integer id){
        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            user = session.get(User.class, id);
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User getUserByName(Student student){
        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> userRoot = criteria.from(User.class);
            criteria.select(userRoot);
            criteria.where(builder.equal(userRoot.get(User_.student), student ));
            user = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public boolean addOne(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(user);
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
    public boolean updateOne(User user){
        return updateField(user);
    }

    @Override
    public boolean deleteOne(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaDelete<User> criteriaDelete = builder.createCriteriaDelete(User.class);
            Root<User> userRoot = criteriaDelete.from(User.class);
            criteriaDelete.where(builder.equal(userRoot.get(User_.id), user.getId()));
            session.createQuery(criteriaDelete).executeUpdate();
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
    public boolean deleteAll(List<User> users){
        List<User> list = getList();
        list.removeAll(users);
        for (User user : list){
            deleteOne(user);
        }
        return true;
    }

    @Override
    public boolean changePassword(User user){
        return updateField(user);
    }

    private boolean updateField(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<User> criteriaUpdate = builder.createCriteriaUpdate(User.class);
            Root<User> userRoot = criteriaUpdate.from(User.class);
            criteriaUpdate.set(userRoot.get(User_.username), user.getUsername());
            criteriaUpdate.set(userRoot.get(User_.password), user.getPassword());
            criteriaUpdate.where(builder.equal(userRoot.get(User_.id), user.getId()));
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
