package com.sims.v2.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Classes implements Serializable {
    private Integer id;
    private String name;
    private List<Student> students;
    private List<Calendar> calendars;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return this.getId() + "|" + this.getName();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classes th = (Classes) o;
        return (getId() == th.getId() &&
                Objects.equals(name, th.name));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), name);
    }

}
