package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;

public class Attendance implements Serializable {
    private Student student;
    private Calendar calendar;
    private Float middleMark;
    private Float finalMark;
    private Float otherMark;
    private Float mark;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Float getMiddleMark() {
        return middleMark;
    }

    public void setMiddleMark(Float middleMark) {
        this.middleMark = middleMark;
    }

    public Float getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(Float finalMark) {
        this.finalMark = finalMark;
    }

    public Float getOtherMark() {
        return otherMark;
    }

    public void setOtherMark(Float otherMark) {
        this.otherMark = otherMark;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return this.getStudent().getId() + "|" + this.getCalendar().getClasses().getId() + "|" + this.getCalendar().getSubject().getId();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance th = (Attendance) o;
        return (Objects.equals(student, th.student) &&
                Objects.equals(calendar, th.calendar));
    }

    @Override
    public int hashCode(){
        return Objects.hash(student, calendar);
    }

}
