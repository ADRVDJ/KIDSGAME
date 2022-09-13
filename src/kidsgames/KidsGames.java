/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kidsgames;

import Control.Controller_login;
import Modelo.UsuarioJpaController;
import Vista.viewLogindatos;
import Vista.viewlogin;

/**
 *
 * @author Shalva
 */
public class KidsGames {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ManagerFactory manager = new ManagerFactory();
        viewLogindatos vista = new viewLogindatos();

        UsuarioJpaController modelo = new UsuarioJpaController(manager.getentityManagerFactory());
        Controller_login controlador = new Controller_login(manager, vista, modelo);

    }

}
