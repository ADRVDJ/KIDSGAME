package Modelo;

import Modelo.Persona;
import Modelo.Rol;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-19T15:43:21")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Persona> usIdPersona;
    public static volatile SingularAttribute<Usuario, BigDecimal> idUsuario;
    public static volatile SingularAttribute<Usuario, String> usPermisos;
    public static volatile SingularAttribute<Usuario, String> usUsuario;
    public static volatile SingularAttribute<Usuario, String> usContraseña;
    public static volatile SingularAttribute<Usuario, Rol> usIdRol;

}