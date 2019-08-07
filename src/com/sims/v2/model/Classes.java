package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Classes implements Serializable {
    private Integer id;
    private String name;
    private Set<Student> students;
    private Set<Calendar> calendars;

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

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(Set<Calendar> calendars) {
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
