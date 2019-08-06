package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private Student student;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return this.getId() + "|" + this.getUsername() + "|" + this.getPassword() + "|" + this.getRole() + "|" + (this.getStudent()!=null ? this.getStudent().getId() : 0);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User th = (User) o;
        return (getId() == th.getId() &&
                Objects.equals(username, th.username) &&
                Objects.equals(password, th.password) &&
                Objects.equals(role, th.role));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), username, password, role);
    }
}
