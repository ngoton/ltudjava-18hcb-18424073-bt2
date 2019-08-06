package com.sims.v2.dao;

import com.sims.v2.model.Classes;
import com.sims.v2.model.Student;
import com.sims.v2.model.User;
import com.sims.v2.util.MD5Encrypt;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl extends IOFileDao implements StudentDao {
    private static String studentFile = "data/student.txt";
    private ClassesDao classesDao;
    private UserDao userDao;

    public StudentDaoImpl(){
        this.classesDao = new ClassesDaoImpl();
        this.userDao = new UserDaoImpl();
    }

    @Override
    public List<Student> getList(){
        List<Student> list = new ArrayList<>();
        List<String[]> data = readFile(studentFile, "\\|");
        for (String[] arr : data){
            Student student = new Student();
            student.setId(Integer.parseInt(arr[0]));
            student.setCode(arr[1]);
            student.setName(arr[2]);
            student.setGender(arr[3]);
            student.setIdNumber(arr[4]);

            Classes classes = classesDao.getClassById(Integer.parseInt(arr[5]));
            if (classes != null){
                student.setStudentClass(classes);
            }

            list.add(student);
        }

        return list;
    }

    @Override
    public Student getStudentById(Integer id){
        List<Student> students = this.getList();
        Student student = null;
        for (Student s : students){
            if (id.equals(s.getId())){
                student = s;
                break;
            }
        }
        return student;
    }

    @Override
    public Student getStudentByCode(String code){
        List<Student> students = this.getList();
        Student student = null;
        for (Student s : students){
            if (code.equals(s.getCode())){
                student = s;
                break;
            }
        }
        return student;
    }

    @Override
    public boolean save(List<Student> students){
        List<User> users = userDao.getList();
        List<User> userList = new ArrayList<>();
        userList.add(users.get(0));
        Integer lastId = 0;
        if (users.size() > 0) {
            lastId = users.get(users.size() - 1).getId();
        }
        for (Student s : students){
            User user = userDao.getUserByName(s);
            if (user == null){
                user = new User();
                user.setId(++lastId);
                user.setUsername(s.getCode());
                user.setPassword(MD5Encrypt.convertHashToString(s.getCode()));
                user.setRole("USER");
                user.setStudent(s);
                userDao.addOne(user);
            }
            else {
                if (!user.getUsername().equals(s.getCode())){
                    user.setUsername(s.getCode());
                    userDao.updateOne(user);
                }
            }
            userList.add(user);
        }

        List<User> newList = new ArrayList<>();

        for (User u : userList) {
            if (u.getRole().equals("ADMIN")){
                newList.add(u);
            }
            else {
                for (Student s : students) {
                    if(u.getStudent() != null && u.getStudent().getId().equals(s.getId())){
                        newList.add(u);
                        break;
                    }
                }
            }
        }
        userDao.deleteAll(newList);

        return writeFile(students, studentFile, false);
    }

    @Override
    public boolean deleteAll(){
        List<User> newList = new ArrayList<>();
        List<User> users = userDao.getList();
        for (User u : users) {
            if(u.getRole().equals("ADMIN")){
                newList.add(u);
            }
        }
        userDao.deleteAll(newList);
        return writeFile(null, studentFile, false);
    }

    @Override
    public List<Student> importFile(String path){
        List<Student> list = getList();
        List<Student> newList = new ArrayList<>();
        List<User> userList = userDao.getList();
        Classes classes = null;
        List<String[]> data = readFile(path, ",");
        Integer lastStudent = 0;
        if (list.size() > 0) {
            lastStudent = list.get(list.size() - 1).getId();
        }
        Integer lastUser = 0;
        if (userList.size() > 0) {
            lastUser = userList.get(userList.size() - 1).getId();
        }
        int i = 0;
        for (String[] arr : data){
            if (i == 0){
                String className = arr[0].trim();
                if (!className.isEmpty()){
                    classes = classesDao.getClassByName(className);
                    if (classes == null){
                        List<Classes> classesList = classesDao.getList();
                        Integer lastId = 0;
                        if (classesList.size() > 0) {
                            lastId = classesList.get(classesList.size() - 1).getId();
                        }
                        Classes c = new Classes();
                        c.setId(++lastId);
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
                    student.setId(++lastStudent);
                    student.setCode(code);
                    student.setName(arr[2].trim());
                    student.setGender(arr[3].trim());
                    student.setIdNumber(arr[4].trim());
                    student.setStudentClass(classes);
                    newList.add(student);
                    list.add(student);

                    User user = new User();
                    user.setId(++lastUser);
                    user.setUsername(code);
                    user.setPassword(MD5Encrypt.convertHashToString(code));
                    user.setRole("USER");
                    user.setStudent(student);
                    userDao.addOne(user);
                    userList.add(user);
                }
            }
            i++;
        }

        writeFile(newList, studentFile, true);

        return list;
    }
}
