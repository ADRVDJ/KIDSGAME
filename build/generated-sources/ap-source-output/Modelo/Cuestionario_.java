package Modelo;

import Modelo.Asignatura;
import Modelo.Juegos;
import Modelo.Preguntas;
import Modelo.PuntajeCuestionario;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T10:09:15")
@StaticMetamodel(Cuestionario.class)
public class Cuestionario_ { 

    public static volatile SingularAttribute<Cuestionario, String> crDescripcion;
    public static volatile SingularAttribute<Cuestionario, Asignatura> crIdAsignatura;
    public static volatile SingularAttribute<Cuestionario, String> crTitulo;
    public static volatile ListAttribute<Cuestionario, PuntajeCuestionario> puntajeCuestionarioList;
    public static volatile SingularAttribute<Cuestionario, BigDecimal> idCuestionario;
    public static volatile ListAttribute<Cuestionario, Preguntas> preguntasList;
    public static volatile SingularAttribute<Cuestionario, Juegos> crIdJuego;

}