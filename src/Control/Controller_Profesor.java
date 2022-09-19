/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Persona;
import Modelo.PersonaJpaController;
import Modelo.Usuario;
import Modelo.UsuarioJpaController;
import Vista.Administrador.viewCrearAdmin;
import Vista.Administrador.viewCrearProfe;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import kidsgames.ManagerFactory;

/**
 *
 * @author ASUS TUF GAMING
 */
public class Controller_Profesor {
    
    viewCrearProfe vista;
    ManagerFactory manage;
    PersonaJpaController modeloPersona;
    UsuarioJpaController modeloUsuario;
    Usuario usuario;
    Persona persona;
    JDesktopPane panelEscritorio;

    public Controller_Profesor(viewCrearAdmin vista, ManagerFactory manage, UsuarioJpaController modeloUsuario, JDesktopPane panelEscritorio) {
        this.manage = manage;
        this.modeloPersona = modeloPersona;
        this.modeloUsuario = modeloUsuario;
        this.panelEscritorio = panelEscritorio;

        if (controladminVistas.vp == null) {
//            controladminVistas.vp = modeloPersona;
//            this.vista = controladminVistas.vp;
//            this.panelEscritorio.add(this.vista);
//            this.vista.getTablausuario().setModel(modelotabla);

            this.vista.show();
            iniciarControl();

            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            controladminVistas.vu.show();

        }
    }

    public void iniciarControl() {
        this.vista.getBtnRegistrar().addActionListener(l -> guardarUsuario());
        this.vista.getBtnEditar().addActionListener(l -> editarUsuario());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarUsuario());
//        this.vista.getTablausuario().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        listaUsernaModel = this.vista.getTablausuario().getSelectionModel();
//        listaUsernaModel.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    usuarioSeleccionado();
//                }
//            }
//
//        });

        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnLimpiar1().addActionListener(l -> limpiar());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiarbuscador());
//        this.vista.getBtnBuscar().addActionListener(l -> buscarusuario());
        //      this.vista.getjCheckMostrar().addActionListener(l -> buscarusuario());
//        this.vista.getReportesgenral().addActionListener(l -> reporteGeneral());
//        this.vista.getBtnReporteIndividualU().addActionListener(l->reporteIndividual());
    }

    //GUARDAR PERSONA
    public void guardarUsuario() {
        try {
            persona = new Persona();
            persona.setPrNombre(this.vista.getTxtnombre().getText());
            persona.setPrApellido(this.vista.getTxtapellido().getText());
            System.out.println(vista.getEdad().getValue());
//        persona.setPrEdad(this.vista.getEdad().getValue());
            persona.setPrGenero((String) this.vista.getGenerocbm().getSelectedItem());
            usuario.setUsUsuario(this.vista.getTxtusuario().getText());
            usuario.setUsContraseña(this.vista.getTxtcontraseña().getText());
            usuario.setUsPermisos(this.vista.getProfesor().getText());
            modeloPersona.create(persona);
            modeloUsuario.create(usuario);
//        modelotabla.agregar(usuario);
//Resouces.success("Atención!!", "USUARIO GUARDADA CORECTAMENTE");
            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA CREADA CORRECTAMENTE");
            limpiar();
        } catch (Exception ex) {
            Logger.getLogger(Controller_admin_Crud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //EDITAR PERSONA
    public void editarUsuario() {
        if (usuario != null) {
            persona.setPrNombre(this.vista.getTxtnombre().getText());
            persona.setPrApellido(this.vista.getTxtapellido().getText());
        }
        persona.setPrGenero((String) this.vista.getGenerocbm().getSelectedItem());
//          persona.setPrEdad();
        persona.setPrGenero((String) this.vista.getGenerocbm().getSelectedItem());
        usuario.setUsUsuario(this.vista.getTxtusuario().getText());
        usuario.setUsContraseña(this.vista.getTxtcontraseña().getText());
//        Resouces.success("Atención!!", "USUARIO EDITADA CORECTAMENTE");
        try {
            modeloUsuario.edit(usuario);
//            modelotabla.eliminar(usuario);
//            modelotabla.actualizar(usuario);
            limpiar();
        } catch (Exception ex) {

            Logger.getLogger(Controller_admin_Crud.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //ELIMINAR PERSONA
    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modeloUsuario.destroy(usuario.getIdUsuario());
                limpiar();
            } catch (Exception ex) {
//                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
                limpiar();
            }
//            modelotabla.eliminar(usuario);
            //  JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
//            Resouces.success("ALERTA!!", "USUARIO ELIMINADO CORECTAMENTE");
        }
    }

    public void limpiar() {
        this.vista.getTxtnombre().setText("");
        this.vista.getTxtapellido().setText("");
        this.vista.getGenerocbm().setSelectedItem(0);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnRegistrar().setEnabled(true);
        this.vista.getTablaprofe().getSelectionModel().clearSelection();
    }

    public void usuarioSeleccionado() {
        if (this.vista.getTablaprofe().getSelectedRow() != -1) {
//            usuario = modelotabla.getFilas().get(this.vista.getTablausuario().getSelectedRow());
            this.vista.getTxtnombre().setText(persona.getPrNombre());
            this.vista.getTxtapellido().setText(persona.getPrApellido());
            this.vista.getGenerocbm().setSelectedItem(persona.getPrGenero());
            this.vista.getTxtusuario().setText(usuario.getUsUsuario());
            this.vista.getTxtcontraseña().setText(usuario.getUsContraseña());
            //
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnRegistrar().setEnabled(false);
        }
    }

    public void limpiarbuscador() {
        this.vista.getTxtBuscar().setText("");
//        modelotabla.setFilas(modeloPersona.findUsuarioEntities());
//        modelotabla.fireTableDataChanged();
    }

//    public void buscarusuario() {
//        if (this.vista.getjCheckMostrar().isSelected()) {
//            modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
//            modelotabla.fireTableDataChanged();
//            limpiarbuscador();
//            // System.out.println("llego");
//        } else {
//            if (!this.vista.getBuscartxt().getText().equals("")) {
//                modelotabla.setFilas(modeloUsuario.buscarusuario(this.vista.getBuscartxt().getText()));
//                modelotabla.fireTableDataChanged();
//                //  System.out.println("llego2");
//            } else {
//
//            }
//
//        }
//
//    }
}
