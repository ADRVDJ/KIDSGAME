package Modelo;

import Modelo.PersonaAsignatura;
import Modelo.PuntajeCuestionario;
import Modelo.Usuario;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T15:43:21")
@StaticMetamodel(Persona.class)
public class Persona_ { 

    public static volatile SingularAttribute<Persona, String> prGenero;
    public static volatile SingularAttribute<Persona, String> prEdad;
    public static volatile ListAttribute<Persona, Usuario> usuarioList;
    public static volatile ListAttribute<Persona, PuntajeCuestionario> puntajeCuestionarioList;
    public static volatile SingularAttribute<Persona, String> prNombre;
    public static volatile SingularAttribute<Persona, BigDecimal> idPersona;
    public static volatile ListAttribute<Persona, PersonaAsignatura> personaAsignaturaList;
    public static volatile SingularAttribute<Persona, String> prApellido;

}