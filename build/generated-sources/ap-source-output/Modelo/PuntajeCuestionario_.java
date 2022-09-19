package Modelo;

import Modelo.Cuestionario;
import Modelo.Persona;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T15:43:21")
@StaticMetamodel(PuntajeCuestionario.class)
public class PuntajeCuestionario_ { 

    public static volatile SingularAttribute<PuntajeCuestionario, BigDecimal> idPuntaje;
    public static volatile SingularAttribute<PuntajeCuestionario, Date> pcFechaFin;
    public static volatile SingularAttribute<PuntajeCuestionario, Date> pcFechaInicio;
    public static volatile SingularAttribute<PuntajeCuestionario, Persona> pcIdPersona;
    public static volatile SingularAttribute<PuntajeCuestionario, Cuestionario> pcIdCuestionario;

}