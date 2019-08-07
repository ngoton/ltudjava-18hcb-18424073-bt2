package com.sims.v2.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Classes.class)
public class Classes_ {
    public static volatile SingularAttribute<Classes, Integer> id;
    public static volatile SingularAttribute<Classes, String> name;
    public static volatile SetAttribute<Classes, Student> students;
    public static volatile SetAttribute<Classes, Calendar> calendars;
}
