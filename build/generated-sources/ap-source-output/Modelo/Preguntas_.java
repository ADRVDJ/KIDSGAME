package Modelo;

import Modelo.Cuestionario;
import Modelo.Respuestas;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(Preguntas.class)
public class Preguntas_ { 

    public static volatile SingularAttribute<Preguntas, Respuestas> prIdRespuestas;
    public static volatile SingularAttribute<Preguntas, Cuestionario> prIdCuestionario;
    public static volatile SingularAttribute<Preguntas, String> prPregunta;
    public static volatile SingularAttribute<Preguntas, BigDecimal> idPregunta;

}