package org.pablovillela.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.pablovillela.bean.CargoEmpleado;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuCargoEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;
    
    private ObservableList<CargoEmpleado> listaCargoE;
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    @FXML
    private Button btnRegresar5;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private TextField txtCargoCodigo;
    @FXML
    private TextField txtNombreCargo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TableView tblCargo;
    @FXML
    private TableColumn colCargoEmpleado;
    @FXML
    private TableColumn colNombreCargo;
    @FXML
    private TableColumn colDescripcion; 
    @FXML
    private Button btnAgregarCargoEmpleado;
    @FXML
    private Button btnEliminarCargoEmpleado;
    @FXML
    private Button btnReportesCargoEmpleado;
    @FXML
    private Button btnEditarCargoEmpleado;        
    
            
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void Agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregarCargoEmpleado.setText("Guardar");
                btnEliminarCargoEmpleado.setText("Cancelar");
                btnEditarCargoEmpleado.setDisable(true);
                btnReportesCargoEmpleado.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCargoEmpleado.setText("Agregar");
                btnEliminarCargoEmpleado.setText("Eliminar");
                btnEditarCargoEmpleado.setDisable(false);
                btnReportesCargoEmpleado.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;

        }

    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarCargoEmpleado.setText("Agregar");
                btnEliminarCargoEmpleado.setText("Eliminar");
                btnEditarCargoEmpleado.setDisable(false);
                btnReportesCargoEmpleado.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblCargo.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Cargo de Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargoE.remove(tblCargo.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un cargo de empleado antes..");
                }
        }
    }
    
    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblCargo.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCargoEmpleado.setText("Actualizar");
                    btnReportesCargoEmpleado.setText("Cancelar");
                    btnAgregarCargoEmpleado.setDisable(true);
                    btnEliminarCargoEmpleado.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/actualizar.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/cancel.png"));
                    activarControles();
                    txtCargoCodigo.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cargo de empleado para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCargoEmpleado.setText("Editar");
                btnReportesCargoEmpleado.setText("Reporte");
                btnAgregarCargoEmpleado.setDisable(false);
                btnEliminarCargoEmpleado.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }
    
    
    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCargoEmpleado(?, ?, ?)}");
            CargoEmpleado registro = (CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            registro.setDescripcionCargo(txtDescripcion.getText());
            Procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            Procedimiento.setString(2, registro.getNombreCargo());
            Procedimiento.setString(3, registro.getDescripcionCargo());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar() {
        CargoEmpleado registro = new CargoEmpleado();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCargoCodigo.getText()));
        registro.setNombreCargo(txtNombreCargo.getText());
        registro.setDescripcionCargo(txtDescripcion.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?, ?)}");
            Procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            Procedimiento.setString(2, registro.getNombreCargo());
            Procedimiento.setString(3, registro.getDescripcionCargo());
            listaCargoE.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoEmpleado(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCargoE = FXCollections.observableList(lista);
    }
    
    public void cargarDatos() {
        tblCargo.setItems(getCargoEmpleado());
        colCargoEmpleado.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("codigoCargoEmpleado"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));
    }

    public void seleccionarElemento() {
        txtCargoCodigo.setText(String.valueOf(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreCargo.setText(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcion.setText(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }

    public void desactivarControles() {
        txtCargoCodigo.setEditable(false);
        txtNombreCargo.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void activarControles() {
        txtCargoCodigo.setEditable(true);
        txtNombreCargo.setEditable(true);
        txtDescripcion.setEditable(true);
    }

    public void limpiarControles() {
        txtCargoCodigo.clear();
        txtNombreCargo.clear();
        txtDescripcion.clear();
    }
    
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar5) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}