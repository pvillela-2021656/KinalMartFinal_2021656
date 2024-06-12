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
import org.pablovillela.bean.Clientes;
import org.pablovillela.bean.Empleados;
import org.pablovillela.bean.Factura;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuFacturaController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Empleados> listaEmpleados;
    
    @FXML
    private Button btnAgregarFactura;
    @FXML
    private Button btnEliminarFactura;
    @FXML
    private Button btnEditarFactura;
    @FXML
    private Button btnReportesFactura;
    @FXML
    private Button btnRegresarFactura;
    @FXML
    private TableColumn colnumeroFactura;
    @FXML
    private TableColumn colestado;
    @FXML
    private TableColumn coltotalFactura;
    @FXML
    private TableColumn colfechaFactura;
    @FXML
    private TableColumn colclienteId;
    @FXML
    private TableColumn colempleadoId;
    @FXML
    private TableView tblFactura;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private TextField txtnumeroFactura;
    @FXML
    private TextField txtestado;
    @FXML
    private TextField txtTotalFactura;
    @FXML
    private TextField txtFechaFactura;
    @FXML
    private ComboBox cmbIdCliente;
    @FXML
    private ComboBox cmbIdEmpleado;
    
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbIdCliente.setItems(getClientes());
        cmbIdEmpleado.setItems(getEmpleados());
        cmbIdCliente.setDisable(false);
        cmbIdEmpleado.setDisable(false);
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    public void agregar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                btnAgregarFactura.setText("Guardar");
                btnEliminarFactura.setText("Cancelar");
                btnEditarFactura.setDisable(true);
                btnReportesFactura.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarFactura.setText("Agregar");
                btnEliminarFactura.setText("Eliminar");
                btnEditarFactura.setDisable(false);
                btnReportesFactura.setDisable(false);
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
                btnAgregarFactura.setText("Agregar");
                btnEliminarFactura.setText("Eliminar");
                btnEditarFactura.setDisable(false);
                btnReportesFactura.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblFactura.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro",
                            "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarFactura(?);");
                            procedimiento.setInt(1, ((Factura) tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                            procedimiento.execute();
                            listaFactura.remove(tblFactura.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar una Factura para Eliminar");
                }
                break;
        }

    }
    
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblFactura.getSelectionModel().getSelectedItem() != null) {
                    btnEditarFactura.setText("Actualizar");
                    btnReportesFactura.setText("Cancelar");
                    btnAgregarFactura.setDisable(true);
                    btnEliminarFactura.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/plus.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/basura.png"));
                    activarControles();
                    txtnumeroFactura.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una factura para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarFactura.setText("Editar");
                btnReportesFactura.setText("Cancelar");
                btnAgregarFactura.setDisable(false);
                btnEliminarFactura.setDisable(false);
                imgEditar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgRecorte.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_editarFactura(?,?,?,?,?,?);");
            Factura registro = (Factura) tblFactura.getSelectionModel().getSelectedItem();
            registro.setEstado(txtestado.getText());
            registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
            registro.setFechaFactura(txtFechaFactura.getText());
            registro.setClienteId(((Clientes) cmbIdCliente.getSelectionModel().getSelectedItem()).getClienteId());
            registro.setEmpleadoId(((Empleados) cmbIdEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
            p.setInt(1, registro.getNumeroFactura());
            p.setString(2, registro.getEstado());
            p.setDouble(3, registro.getTotalFactura());
            p.setString(4, registro.getFechaFactura());
            p.setInt(5, registro.getClienteId());
            p.setInt(6, registro.getEmpleadoId());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar() {
        Factura registro = new Factura();
        registro.setNumeroFactura(Integer.parseInt(txtnumeroFactura.getText()));
        registro.setEstado(txtestado.getText());
        registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
        registro.setFechaFactura(txtFechaFactura.getText());
        registro.setClienteId(((Clientes) cmbIdCliente.getSelectionModel().getSelectedItem()).getClienteId());
        registro.setEmpleadoId(((Empleados) cmbIdEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarFactura(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setDouble(3, registro.getTotalFactura());
            procedimiento.setString(4, registro.getFechaFactura());
            procedimiento.setInt(5, registro.getClienteId());
            procedimiento.setInt(6, registro.getEmpleadoId());
            procedimiento.execute();
            listaFactura.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void cargarDatos() {
        tblFactura.setItems(getFacturas());
        colnumeroFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("numeroFactura"));
        colestado.setCellValueFactory(new PropertyValueFactory<Factura, String>("estado"));
        coltotalFactura.setCellValueFactory(new PropertyValueFactory<Factura, Double>("totalFactura"));
        colfechaFactura.setCellValueFactory(new PropertyValueFactory<Factura, String>("fechaFactura"));
        colclienteId.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("clienteId"));
        colempleadoId.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("empleadoId"));
    }

    public void selecionarElementos() {
        txtnumeroFactura.setText(String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        txtestado.setText(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getEstado());
        txtTotalFactura.setText(String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura()));
        txtFechaFactura.setText((((Factura) tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura()));
        cmbIdCliente.getSelectionModel().select(buscarClientes(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getClienteId()));
        cmbIdEmpleado.getSelectionModel().select(buscarEmpleados(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getEmpleadoId()));
    }
    
    public ObservableList<Factura> getFacturas() {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Factura(resultado.getInt("numeroFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("clienteId"),
                        resultado.getInt("empleadoId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFactura = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes ()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteId"),
                        resultado.getString("NIT"),
                        resultado.getString("nombreCliente"),
                        resultado.getString("apellidoCliente"),
                        resultado.getString("direccionCliente"),
                        resultado.getString("telefonoCliente"),
                        resultado.getString("correoCliente")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
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
    
    public void desactivarControles() {
        txtnumeroFactura.setEditable(false);
        txtestado.setEditable(false);
        txtTotalFactura.setEditable(false);
        txtFechaFactura.setEditable(false);
        cmbIdCliente.setEditable(false);
        cmbIdEmpleado.setEditable(false);
    }

    public void activarControles() {
        txtnumeroFactura.setEditable(true);
        txtestado.setEditable(true);
        txtTotalFactura.setEditable(true);
        txtFechaFactura.setEditable(true);
        cmbIdCliente.setDisable(false);
        cmbIdEmpleado.setDisable(false);

    }

    public void limpiarControles() {
        txtnumeroFactura.clear();
        txtestado.clear();
        txtTotalFactura.clear();
        txtFechaFactura.clear();
        tblFactura.getSelectionModel().getSelectedItem();
        cmbIdCliente.getSelectionModel().getSelectedItem();
        cmbIdEmpleado.getSelectionModel().getSelectedItem();

    }
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarFactura) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
    
    public Empleados buscarEmpleados(int empleadoid) {
        Empleados resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, empleadoid);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleados(registro.getInt("empleadoid"),
                        registro.getString("nombreEmpleado"),
                        registro.getString("apellidoEmpleado"),
                        registro.getDouble("sueldo"),
                        registro.getString("direccion"),
                        registro.getString("turno"),
                        registro.getInt("codigoCargoEmpleado")
                       );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Clientes buscarClientes(int clienteid) {
        Clientes resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, clienteid);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Clientes(registro.getInt("clienteid"),
                        registro.getString("NIT"),
                        registro.getString("nombreCliente"),
                        registro.getString("apellidoCliente"),
                        registro.getString("direccionCliente"),
                        registro.getString("telefonoCliente"),
                        registro.getString("correoCliente")
                       );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
}
