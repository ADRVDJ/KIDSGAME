package Modelo;

import Modelo.Cuestionario;
import Modelo.Curso;
import Modelo.PersonaAsignatura;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T15:43:21")
@StaticMetamodel(Asignatura.class)
public class Asignatura_ { 

    public static volatile SingularAttribute<Asignatura, String> asDescripcion;
    public static volatile SingularAttribute<Asignatura, Curso> asIdcurso;
    public static volatile ListAttribute<Asignatura, Cuestionario> cuestionarioList;
    public static volatile SingularAttribute<Asignatura, BigDecimal> idAsignatura;
    public static volatile SingularAttribute<Asignatura, String> asNombre;
    public static volatile SingularAttribute<Asignatura, PersonaAsignatura> idPersonaA;

}