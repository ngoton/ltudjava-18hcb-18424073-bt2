package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Subject implements Serializable {
    private Integer id;
    private String code;
    private String name;
    private Set<Calendar> calendars;

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

    public Set<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(Set<Calendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public String toString() {
        return this.getId() + "|" + this.getCode() + "|" + this.getName();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject th = (Subject) o;
        return (getId() == th.getId() &&
                Objects.equals(code, th.code) &&
                Objects.equals(name, th.name));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), code, name);
    }

}
