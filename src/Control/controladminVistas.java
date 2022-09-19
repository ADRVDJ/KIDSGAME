package Control;

import Modelo.PersonaJpaController;
import Vista.Administrador.viewCrearAdmin;
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
    }

    public void controlEvento() {
        this.vista.getAdmin().addActionListener(l -> cargarvistaPersona());
        this.vista.getJuego().addActionListener(l -> cargarvistaProducto());
        this.vista.getPuntaje().addActionListener(l -> cargarvistaUsuario());
    }

    public static viewPersona vpd = null;
    public static viewCrearAdmin vp = null;
    public static viewJuegos vps = null;
    public static viewCuestionarios vu = null;

    public void cargarvistaPersona() {
        new Controller_admin_Crud(vp, manage, new PersonaJpaController(manage.getentityManagerFactory()), this.vista.getEscritorio());
        System.out.println("mensaje");
    }

    public void cargarvistaProducto() {

    }

    public void cargarvistaUsuario() {

    }
}
