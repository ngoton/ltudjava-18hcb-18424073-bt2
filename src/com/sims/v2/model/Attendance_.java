package com.sims.v2.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Attendance.class)
public class Attendance_ {
    public static volatile SingularAttribute<Attendance, Student> student;
    public static volatile SingularAttribute<Attendance, Calendar> calendar;
    public static volatile SingularAttribute<Attendance, Application> application;
    public static volatile SingularAttribute<Attendance, Float> middleMark;
    public static volatile SingularAttribute<Attendance, Float> finalMark;
    public static volatile SingularAttribute<Attendance, Float> otherMark;
    public static volatile SingularAttribute<Attendance, Float> mark;
}
