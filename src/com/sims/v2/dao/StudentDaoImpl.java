package com.sims.v2.dao;

import com.sims.v2.model.Classes;
import com.sims.v2.model.Student;
import com.sims.v2.model.Student_;
import com.sims.v2.model.User;
import com.sims.v2.util.HibernateUtil;
import com.sims.v2.util.MD5Encrypt;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
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
    public List<Student> getStudentByClass(Classes classes){
        List<Student> list = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> criteria = builder.createQuery(Student.class);
            Root<Student> studentRoot = criteria.from(Student.class);
            criteria.select(studentRoot);
            criteria.where(builder.equal(studentRoot.get(Student_.studentClass), classes ));
            list = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            System.err.println(ex);
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public boolean addOne(Student student){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(student);
            transaction.commit();
            User user = new User();
            user.setUsername(student.getCode());
            user.setPassword(MD5Encrypt.convertHashToString(student.getCode()));
            user.setRole("USER");
            user.setStudent(student);
            userDao.addOne(user);
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
    public boolean updateOne(Student student){
        return updateField(student);
    }

    @Override
    public boolean deleteOne(Student student){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = userDao.getUserByName(student);
            userDao.deleteOne(user);
            session.delete(student);
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
            deleteOne(student);
        }
        return true;
    }

    @Override
    public List<Student> importFile(String path){
        ClassesDao classesDao = new ClassesDaoImpl();
        List<Student> list = getList();
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
                String code = arr[1].trim();
                boolean checkCode = true;
                for (Student st : list){
                    if (st.getCode().equals(code)){
                        checkCode = false;
                        break;
                    }
                }
                if (checkCode == true) {
                    Student student = new Student();
                    student.setCode(code);
                    student.setName(arr[2].trim());
                    student.setGender(arr[3].trim());
                    student.setIdNumber(arr[4].trim());
                    student.setStudentClass(classes);
                    addOne(student);
                }
            }
            i++;
        }
        list = getList();
        return list;
    }

    private boolean updateField(Student student){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = userDao.getUserByName(getStudentById(student.getId()));
            user.setUsername(student.getCode());
            userDao.updateOne(user);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Student> criteriaUpdate = builder.createCriteriaUpdate(Student.class);
            Root<Student> studentRoot = criteriaUpdate.from(Student.class);
            criteriaUpdate.set(studentRoot.get(Student_.code), student.getCode());
            criteriaUpdate.set(studentRoot.get(Student_.name), student.getName());
            criteriaUpdate.set(studentRoot.get(Student_.gender), student.getGender());
            criteriaUpdate.set(studentRoot.get(Student_.idNumber), student.getIdNumber());
            criteriaUpdate.set(studentRoot.get(Student_.studentClass), student.getStudentClass());
            criteriaUpdate.where(builder.equal(studentRoot.get(Student_.id), student.getId()));
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
