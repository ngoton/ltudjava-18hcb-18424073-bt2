package com.sims.v2.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Student.class)
public class Student_ {
    public static volatile SingularAttribute<Student, Integer> id;
    public static volatile SingularAttribute<Student, String> code;
    public static volatile SingularAttribute<Student, String> name;
    public static volatile SingularAttribute<Student, String> gender;
    public static volatile SingularAttribute<Student, String> idNumber;
    public static volatile SingularAttribute<Student, Classes> studentClass;
    public static volatile SetAttribute<Student, Attendance> attendances;
}
