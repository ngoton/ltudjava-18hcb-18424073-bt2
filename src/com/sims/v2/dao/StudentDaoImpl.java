package com.sims.v2.dao;

import com.sims.v2.model.Student;
import com.sims.v2.model.Student_;
import com.sims.v2.model.User;
import com.sims.v2.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl extends IOFileDao implements StudentDao {
    private UserDao userDao;

    public StudentDaoImpl(){
        this.userDao = new UserDaoImpl();
    }

    @Override
    public List<Student> getList(){
        List<Student> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> criteria = builder.createQuery(Student.class);
            Root<Student> studentRoot = criteria.from(Student.class);
            studentRoot.join(Student_.studentClass);
            criteria.select(studentRoot);
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public Student getStudentById(Integer id){
        Student student = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            student = session.get(Student.class, id);
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return student;
    }

    @Override
    public Student getStudentByCode(String code){
        Student student = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> criteria = builder.createQuery(Student.class);
            Root<Student> studentRoot = criteria.from(Student.class);
            criteria.select(studentRoot);
            criteria.where(builder.equal(studentRoot.get(Student_.code), code ));
            student = session.createQuery(criteria).uniqueResult();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return student;
    }

    @Override
    public boolean save(List<Student> students){
        return true;
    }

    @Override
    public boolean deleteOne(Student student){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaDelete<Student> criteriaDelete = builder.createCriteriaDelete(Student.class);
            Root<Student> studentRoot = criteriaDelete.from(Student.class);
            criteriaDelete.where(builder.equal(studentRoot.get(Student_.id), student.getId()));
            transaction.begin();
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
    public boolean deleteAll(){
        List<Student> students = getList();
        for (Student student : students){
            User user = userDao.getUserByName(student);
            userDao.deleteOne(user);
            deleteOne(student);
        }
        return true;
    }

    @Override
    public List<Student> importFile(String path){
        return null;
    }
}
