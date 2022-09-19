package Modelo;

import Modelo.Curso;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T15:43:21")
@StaticMetamodel(AnioLectivo.class)
public class AnioLectivo_ { 

    public static volatile SingularAttribute<AnioLectivo, String> alFecFin;
    public static volatile SingularAttribute<AnioLectivo, String> alFecInici;
    public static volatile SingularAttribute<AnioLectivo, BigDecimal> idAnioLectivo;
    public static volatile SingularAttribute<AnioLectivo, String> alNombre;
    public static volatile ListAttribute<AnioLectivo, Curso> cursoList;

}