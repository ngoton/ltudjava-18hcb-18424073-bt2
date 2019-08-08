package com.sims.v2.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Subject.class)
public class Subject_ {
    public static volatile SingularAttribute<Subject, Integer> id;
    public static volatile SingularAttribute<Subject, String> code;
    public static volatile SingularAttribute<Subject, String> name;
    public static volatile SetAttribute<Subject, Calendar> calendars;
}
