/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Usuario;
import Modelo.UsuarioJpaController;
import Vista.ViewMenuEstudiante;
import Vista.ViewMenuProfesor;
import Vista.viewLogindatos;
import Vista.viewMenuAdmin;
import java.math.BigDecimal;
import kidsgames.ManagerFactory;

/**
 *
 * @author ASUS TUF GAMING
 */
public class Controller_login {

    //login
    private ManagerFactory manager;
    private viewLogindatos vista;
    private UsuarioJpaController modelo;
    ViewMenuProfesor profesor = new ViewMenuProfesor();
    viewMenuAdmin admin = new viewMenuAdmin();
    ViewMenuEstudiante estudiante = new ViewMenuEstudiante();

    public Controller_login(ManagerFactory manager, viewLogindatos vista, UsuarioJpaController modelo) {
        this.manager = manager;
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        iniciarcontrol();
    }

    public void iniciarcontrol() {
        vista.getBt_iniciar().addActionListener(l -> controlLogin());
        // vista.getCerrar().addActionListener(l -> System.exit(0));
    }

    public void controlLogin() {

        String usuario = vista.getTxtusuarioLogin().getText();
        String contraseña = new String(vista.getTxtcontraseñaLogin().getPassword());
        String rol = "administrador";
        try {
            Usuario user = modelo.buscarByCredenciales(usuario, contraseña, rol);
            if (user!=null && !usuario.equals("") || !contraseña.equals("") || !rol.equals("administrador")) {
                System.out.println("entro al primer if");

                if (user != null && rol.equals("administrador")) {
                    viewMenuAdmin a = new viewMenuAdmin();
                    System.out.println("estoy dentro ");
                    a.setVisible(true);
//                        admin.setVisible(true);
                    vista.setVisible(false);
                } else {
                    System.out.println("Tu si puedes no te rindas");
                }

            }
        } catch (Exception e) {
            System.out.println("no entro");
        }
    }

}
