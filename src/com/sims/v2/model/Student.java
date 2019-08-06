package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    private Integer id;
    private String code;
    private String name;
    private String gender;
    private String idNumber;
    private Classes studentClass;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Classes getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(Classes studentClass) {
        this.studentClass = studentClass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.getId() + "|" + this.getCode() + "|" + this.getName() + "|" + this.getGender() + "|" + this.getIdNumber() + "|" + this.getStudentClass().getId();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student th = (Student) o;
        return (getId() == th.getId() &&
                Objects.equals(code, th.code) &&
                Objects.equals(name, th.name) &&
                Objects.equals(gender, th.gender) &&
                Objects.equals(idNumber, th.idNumber));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), code, name, gender, idNumber);
    }

}
