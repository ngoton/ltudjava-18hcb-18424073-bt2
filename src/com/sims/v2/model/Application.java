package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;

public class Application implements Serializable {
    private Integer id;
    private String reason;
    private Float middleExpect;
    private Float finalExpect;
    private Float otherExpect;
    private Float markExpect;
    private Float newMiddle;
    private Float newFinal;
    private Float newOther;
    private Float newMark;
    private String status;
    private Remarking remarking;
    private Attendance attendance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Float getMiddleExpect() {
        return middleExpect;
    }

    public void setMiddleExpect(Float middleExpect) {
        this.middleExpect = middleExpect;
    }

    public Float getFinalExpect() {
        return finalExpect;
    }

    public void setFinalExpect(Float finalExpect) {
        this.finalExpect = finalExpect;
    }

    public Float getOtherExpect() {
        return otherExpect;
    }

    public void setOtherExpect(Float otherExpect) {
        this.otherExpect = otherExpect;
    }

    public Float getMarkExpect() {
        return markExpect;
    }

    public void setMarkExpect(Float markExpect) {
        this.markExpect = markExpect;
    }

    public Float getNewMiddle() {
        return newMiddle;
    }

    public void setNewMiddle(Float newMiddle) {
        this.newMiddle = newMiddle;
    }

    public Float getNewFinal() {
        return newFinal;
    }

    public void setNewFinal(Float newFinal) {
        this.newFinal = newFinal;
    }

    public Float getNewOther() {
        return newOther;
    }

    public void setNewOther(Float newOther) {
        this.newOther = newOther;
    }

    public Float getNewMark() {
        return newMark;
    }

    public void setNewMark(Float newMark) {
        this.newMark = newMark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Remarking getRemarking() {
        return remarking;
    }

    public void setRemarking(Remarking remarking) {
        this.remarking = remarking;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application th = (Application) o;
        return (getId() == th.getId() &&
                Objects.equals(reason, th.reason) &&
                Objects.equals(middleExpect, th.middleExpect) &&
                Objects.equals(finalExpect, th.finalExpect) &&
                Objects.equals(otherExpect, th.otherExpect) &&
                Objects.equals(markExpect, th.markExpect) &&
                Objects.equals(newMiddle, th.newMiddle) &&
                Objects.equals(newFinal, th.newFinal) &&
                Objects.equals(newOther, th.newOther) &&
                Objects.equals(newMark, th.newMark) &&
                Objects.equals(status, th.status));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), reason, middleExpect, finalExpect, otherExpect, markExpect, newMiddle, newFinal, newOther, newMark, status);
    }

}
