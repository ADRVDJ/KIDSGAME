package Control;

import Modelo.Persona;
import Modelo.PersonaJpaController;
import Modelo.Usuario;
import Modelo.UsuarioJpaController;
import Vista.Administrador.viewCrearAdmin;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import kidsgames.ManagerFactory;

/**
 *
 * @author ASUS TUF GAMING
 */
public class Controller_admin_Crud {

    viewCrearAdmin vista;
    ManagerFactory manage;
    PersonaJpaController modeloPersona;
    UsuarioJpaController modeloUsuario;
    Usuario usuario;
    Persona persona;
    JDesktopPane panelEscritorio;
    ModeloTablaAdmin modeloTablaAdmin;
    ListSelectionModel listapersonamodel;

    public Controller_admin_Crud(viewCrearAdmin vista, ManagerFactory manage, PersonaJpaController modeloPersona, JDesktopPane panelEscritorio) {
        this.manage = manage;
        this.modeloPersona = modeloPersona;
        this.panelEscritorio = panelEscritorio;
        this.modeloTablaAdmin = new ModeloTablaAdmin();
        this.modeloTablaAdmin.setFilas(modeloPersona.findPersonaEntities());

        if (controladminVistas.vp == null) {
            controladminVistas.vp = new viewCrearAdmin();
            this.vista = controladminVistas.vp;
            this.panelEscritorio.add(this.vista);
            this.vista.getTablaadministrador().setModel(modeloTablaAdmin);
            this.vista.show();
            iniciarControl();
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            controladminVistas.vp.show();
        }
    }

    public void iniciarControl() {
        this.vista.getBtnRegistrar().addActionListener(l -> guardarUsuario());
        this.vista.getBtnEditar().addActionListener(l -> editarUsuario());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarUsuario());
        this.vista.getTablaadministrador().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listapersonamodel = this.vista.getTablaadministrador().getSelectionModel();
        listapersonamodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSeleccionado();
                }
            }

        });

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
            usuario.setUsContrase??a(this.vista.getTxtcontrase??a().getText());
            usuario.setUsPermisos(this.vista.getAdmin().getText());
            modeloPersona.create(persona);
            modeloUsuario.create(usuario);
            modeloTablaAdmin.agregar(persona);

            Resouces.success("Atenci??n!!", "USUARIO GUARDADA CORECTAMENTE");
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
        usuario.setUsContrase??a(this.vista.getTxtcontrase??a().getText());
//        Resouces.success("Atenci??n!!", "USUARIO EDITADA CORECTAMENTE");
        try {
//            modeloUsuario.edit(usuario);
//           modeloUsuario.(usuario);
//           modelotabla.actualizar(usuario);
//            limpiar();
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
                Logger.getLogger(Controller_admin_Crud.class.getName()).log(Level.SEVERE, null, ex);
                limpiar();
            }
            modeloTablaAdmin.eliminar(persona);
            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
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
        this.vista.getTablaadministrador().getSelectionModel().clearSelection();
    }

    public void usuarioSeleccionado() {
        if (this.vista.getTablaadministrador().getSelectedRow() != -1) {
            persona = modeloTablaAdmin.getFilas().get(this.vista.getTablaadministrador().getSelectedRow());
            this.vista.getTxtnombre().setText(persona.getPrNombre());
            this.vista.getTxtapellido().setText(persona.getPrApellido());
            this.vista.getGenerocbm().setSelectedItem(persona.getPrGenero());
            this.vista.getTxtusuario().setText(usuario.getUsUsuario());
            this.vista.getTxtcontrase??a().setText(usuario.getUsContrase??a());
            //
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnRegistrar().setEnabled(false);
        }
    }

    public void limpiarbuscador() {
        this.vista.getTxtBuscar().setText("");
        modeloTablaAdmin.setFilas(modeloPersona.findPersonaEntities());
        modeloTablaAdmin.fireTableDataChanged();
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
