package com.sims.v2.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Application.class)
public class Application_ {
    public static volatile SingularAttribute<Application, Attendance> attendance;
    public static volatile SingularAttribute<Application, Remarking> remarking;
    public static volatile SingularAttribute<Application, Float> middleExpect;
    public static volatile SingularAttribute<Application, Float> finalExpect;
    public static volatile SingularAttribute<Application, Float> otherExpect;
    public static volatile SingularAttribute<Application, Float> markExpect;
    public static volatile SingularAttribute<Application, Float> newMiddle;
    public static volatile SingularAttribute<Application, Float> newFinal;
    public static volatile SingularAttribute<Application, Float> newOther;
    public static volatile SingularAttribute<Application, Float> newMark;
    public static volatile SingularAttribute<Application, String> reason;
    public static volatile SingularAttribute<Application, String> status;
}
