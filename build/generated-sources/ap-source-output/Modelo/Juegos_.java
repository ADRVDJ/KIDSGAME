package Modelo;

import Modelo.Cuestionario;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(Juegos.class)
public class Juegos_ { 

    public static volatile SingularAttribute<Juegos, String> jgNombre;
    public static volatile SingularAttribute<Juegos, String> jgPuntajemin;
    public static volatile ListAttribute<Juegos, Cuestionario> cuestionarioList;
    public static volatile SingularAttribute<Juegos, BigDecimal> idJuego;
    public static volatile SingularAttribute<Juegos, String> jgPuntajemax;

}