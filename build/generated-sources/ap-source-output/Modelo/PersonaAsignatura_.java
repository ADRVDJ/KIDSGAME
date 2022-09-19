package Modelo;

import Modelo.Asignatura;
import Modelo.Persona;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(PersonaAsignatura.class)
public class PersonaAsignatura_ { 

    public static volatile SingularAttribute<PersonaAsignatura, Persona> paIdPersona;
    public static volatile ListAttribute<PersonaAsignatura, Asignatura> asignaturaList;
    public static volatile SingularAttribute<PersonaAsignatura, BigDecimal> idPersonaA;

}