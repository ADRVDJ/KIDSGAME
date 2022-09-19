/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ASUS TUF GAMING
 */
public class ModeloTablaAdmin extends AbstractTableModel {

    private String[] columnas = {"PR_NOMBRE", "PR_APELLIDO", "PR_EDAD", "PR_GENERO"};
    public static List<Persona> filas;
    private Persona personaSelecionado;
    private int indice;

    public ModeloTablaAdmin() {
        filas = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        personaSelecionado = filas.get(rowIndex);
        this.indice = rowIndex;
        switch (columnIndex) {
            case 0:
                return personaSelecionado.getPrNombre();
            case 1:
                return personaSelecionado.getPrApellido();
            case 2:
                return personaSelecionado.getPrEdad();
            case 3:
                return personaSelecionado.getPrGenero();

            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            default:
                return Object.class;
        }
    }

    public String[] getColumnas() {
        return columnas;
    }

    public void setColumnas(String[] columnas) {
        this.columnas = columnas;
    }

    public List<Persona> getFilas() {
        return filas;
    }

    public void setFilas(List<Persona> filas) {
        this.filas = filas;
    }

    public Persona getPersonaSelecionado() {
        return personaSelecionado;
    }

    public void setPersonaSelecionado(Persona personaSelecionado) {
        this.personaSelecionado = personaSelecionado;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public void actualizar(Persona p) {
        setPersonaSelecionado(null);
        if (p != null) {
            filas.add(indice, p);
            fireTableDataChanged();
        }
    }

    public void agregar(Persona p) {
        if (p != null) {
            filas.add(p);
            fireTableDataChanged();
        }
    }

    public void eliminar(Persona p) {
        if (p != null) {
            filas.remove(p);
            fireTableDataChanged();
        }

    }

}
