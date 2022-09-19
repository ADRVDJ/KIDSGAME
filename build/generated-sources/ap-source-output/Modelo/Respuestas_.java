package Modelo;

import Modelo.Preguntas;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(Respuestas.class)
public class Respuestas_ { 

    public static volatile SingularAttribute<Respuestas, String> rsRespuestai;
    public static volatile ListAttribute<Respuestas, Preguntas> preguntasList;
    public static volatile SingularAttribute<Respuestas, BigDecimal> idRespuesta;
    public static volatile SingularAttribute<Respuestas, String> rsRespuestac;

}