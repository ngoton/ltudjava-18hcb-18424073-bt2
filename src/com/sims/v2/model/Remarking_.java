package com.sims.v2.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Remarking.class)
public class Remarking_ {
    public static volatile SingularAttribute<Remarking, Integer> id;
    public static volatile SingularAttribute<Remarking, Date> opening;
    public static volatile SingularAttribute<Remarking, Date> closing;
    public static volatile SetAttribute<Remarking, Application> applications;
}
