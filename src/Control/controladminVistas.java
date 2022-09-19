/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Vista.Subvista.viewCuestionarios;
import Vista.Subvista.viewJuegos;
import Vista.Subvista.viewPersona;
import Vista.viewMenuAdmin;
import kidsgames.ManagerFactory;

/**
 *
 * @author ASUS TUF GAMING
 */
public class controladminVistas extends javax.swing.JFrame {

    viewMenuAdmin vista;
    ManagerFactory manage;
    
    

    public controladminVistas(viewMenuAdmin vista, ManagerFactory manage) {
        this.vista = vista;
        this.manage = manage;
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        controlEvento();
        System.out.println("Hola cambio 1");
    }

    public void controlEvento() {
        this.vista.getBtnPersona().addActionListener(l -> cargarvistaPersona());
  
    }
    
    public static viewPersona vp ;
    public static viewJuegos vps ;
    public static viewCuestionarios vu;

    public void cargarvistaPersona() {
        System.out.println("entro al evento");
      //new Controller_admin(vp, manage, new PersonaJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
//        System.out.println("mensaje");
        vp=new viewPersona();
        vista.Escritorio.add(vp);
        vp.setVisible(true);
    }

    public void cargarvistaProducto() {
//        new ControllerProducto (vps, manage, new ProductoJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
//        System.out.println("mensaje");

    }

    public void cargarvistaUsuario() {
//        new ControllerUsuario(vu, manage, new UsuarioJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
//        System.out.println("mensaje");

    }
}
