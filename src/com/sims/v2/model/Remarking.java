package com.sims.v2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Remarking implements Serializable {
    private Integer id;
    private Date opening;
    private Date closing;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOpening() {
        return opening;
    }

    public void setOpening(Date opening) {
        this.opening = opening;
    }

    public Date getClosing() {
        return closing;
    }

    public void setClosing(Date closing) {
        this.closing = closing;
    }

    @Override
    public String toString() {
        return this.getId() + "|" + this.getOpening() + "|" + this.getClosing();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remarking th = (Remarking) o;
        return (getId() == th.getId() &&
                Objects.equals(opening, th.opening) &&
                Objects.equals(closing, th.closing));
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), opening, closing);
    }
}
