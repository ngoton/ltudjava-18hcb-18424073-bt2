package com.sims.v2.model;

import java.io.Serializable;
import java.util.Objects;

public class Calendar implements Serializable {
    private Classes classes;
    private Subject subject;
    private String room;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return this.getClasses().getId() + "|" + this.getSubject().getId() + "|" + this.getRoom();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar th = (Calendar) o;
        return (Objects.equals(classes, th.classes) &&
                Objects.equals(subject, th.subject) &&
                Objects.equals(room, th.room));
    }

    @Override
    public int hashCode(){
        return Objects.hash(classes, subject, room);
    }
}
