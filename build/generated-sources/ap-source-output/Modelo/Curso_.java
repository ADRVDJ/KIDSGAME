package Modelo;

import Modelo.AnioLectivo;
import Modelo.Asignatura;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(Curso.class)
public class Curso_ { 

    public static volatile ListAttribute<Curso, Asignatura> asignaturaList;
    public static volatile SingularAttribute<Curso, BigDecimal> idCurso;
    public static volatile SingularAttribute<Curso, String> curNombre;
    public static volatile SingularAttribute<Curso, AnioLectivo> curIdAñoLectivo;

}