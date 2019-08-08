package com.sims.v2.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Calendar.class)
public class Calendar_ {
    public static volatile SingularAttribute<Calendar, Classes> classes;
    public static volatile SingularAttribute<Calendar, Subject> subject;
    public static volatile SingularAttribute<Calendar, String> room;
    public static volatile SetAttribute<Calendar, Attendance> attendances;
}
