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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.pablovillela.bean.CargoEmpleado;
import org.pablovillela.bean.Empleados;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuEmpleadoController implements Initializable {

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<CargoEmpleado> listaCargoEmpleado;

    private Principal escenarioPrincipal;

    @FXML
    private Button btnRegresarEmpleados;
    @FXML
    private Button btnAgregarEmpleados;
    @FXML
    private Button btnEliminarEmpleados;
    @FXML
    private Button btnEditarEmpleados;
    @FXML
    private Button btnReportesEmpleados;
    @FXML
    private TextField txtIdEmpleado;
    @FXML
    private TextField txtNombreEmpleado;
    @FXML
    private TextField txtApellidoEmpleado;
    @FXML
    private TextField txtSueldoEmpleado;
    @FXML
    private TextField txtDireccionEmpleado;
    @FXML
    private TextField txtTurnoEmpleado;
    @FXML
    private ComboBox cmbCargoEmpleado;
    @FXML
    private TableColumn colIdEmpleadoE;
    @FXML
    private TableColumn colNombreEmpleadoE;
    @FXML
    private TableColumn colApellidoEmpleadoE;
    @FXML
    private TableColumn colSueldoEmpleadoE;
    @FXML
    private TableColumn colDireccionEmpleadoE;
    @FXML
    private TableColumn colTurnoEmpleadoE;
    @FXML
    private TableColumn colCargoEmpleado;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCargoEmpleado.setItems(getCargoEmpleado());
        cmbCargoEmpleado.setDisable(false);
    }

    public void agregar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                btnAgregarEmpleados.setText("Guardar");
                btnEliminarEmpleados.setText("Cancelar");
                btnEditarEmpleados.setDisable(true);
                btnReportesEmpleados.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarEmpleados.setText("Agregar");
                btnEliminarEmpleados.setText("Eliminar");
                btnEditarEmpleados.setDisable(false);
                btnReportesEmpleados.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }

    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarEmpleados.setText("Agregar");
                btnEliminarEmpleados.setText("Eliminar");
                btnEditarEmpleados.setDisable(false);
                btnReportesEmpleados.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del empleado?",
                            "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarEmpleados();");
                            procedimiento.setInt(1, ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Producto para Eliminar");
                }
                break;
        }

    }

    public void guardar() {
        Empleados registro = new Empleados();
        registro.setEmpleadoId(Integer.parseInt(txtIdEmpleado.getText()));
        registro.setNombreEmpleado(txtNombreEmpleado.getText());
        registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
        registro.setSueldo(Double.parseDouble(txtSueldoEmpleado.getText()));
        registro.setDireccion(txtDireccionEmpleado.getText());
        registro.setTurno(txtTurnoEmpleado.getText());
        registro.setCodigoCargoEmpleado(((CargoEmpleado) cmbCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getEmpleadoId());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados(resultado.getInt("empleadoId"),
                        resultado.getString("nombreEmpleado"),
                        resultado.getString("apellidoEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpleados = FXCollections.observableArrayList(lista);
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
        return listaCargoEmpleado = FXCollections.observableList(lista);
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colIdEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("empleadoId"));
        colNombreEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidoEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldoEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireccionEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
        colTurnoEmpleadoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colCargoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargoEmpleado"));
    }

    public CargoEmpleado buscarCargoEmpleado(int codigoCargoEmpleado) {
        CargoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargoEmpleado(?)}");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new CargoEmpleado(registro.getInt("codigoCargoEmpleado"),
                        registro.getString("nombreCargo"),
                        registro.getString("descripcionCargo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void selecionarElementos() {
        txtIdEmpleado.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId()));
        txtNombreEmpleado.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidoEmpleado.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
        txtSueldoEmpleado.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        txtDireccionEmpleado.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccion());
        txtTurnoEmpleado.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTurno());
        cmbCargoEmpleado.getSelectionModel().select(buscarCargoEmpleado(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
    }

    public void desactivarControles() {
        txtIdEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtSueldoEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTurnoEmpleado.setEditable(false);
        cmbCargoEmpleado.setDisable(true);
    }

    public void activarControles() {
        txtIdEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtSueldoEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTurnoEmpleado.setEditable(true);
        cmbCargoEmpleado.setDisable(false);
    }

    public void limpiarControles() {
        txtIdEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtSueldoEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTurnoEmpleado.clear();
        tblEmpleados.getSelectionModel().getSelectedItem();
        cmbCargoEmpleado.getSelectionModel().getSelectedItem();

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarEmpleados) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
