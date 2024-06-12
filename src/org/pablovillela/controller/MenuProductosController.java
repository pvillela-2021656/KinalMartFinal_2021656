package org.pablovillela.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.pablovillela.system.Principal;
import org.pablovillela.bean.Productos;
import org.pablovillela.bean.TipoProducto;
import org.pablovillela.bean.Proveedores;
import org.pablovillela.db.Conexion;
import org.pablovillela.report.GenerarReporte;

public class MenuProductosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProductos;
    private ObservableList<Proveedores> listarProveedores;
    private ObservableList<TipoProducto> listaTipoDeProducto;

    @FXML
    private Button btnRegresar6;
    @FXML
    private Button btnAgregarProductos;
    @FXML
    private Button btnEliminarProductos;
    @FXML
    private Button btnEditarProductos;
    @FXML
    private Button btnReportesProveedores;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private TextField txtcodigoProducto;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecioUnitario;
    @FXML
    private TextField txtPrecioDocena;
    @FXML
    private TextField txtPrecioMayor;
    @FXML
    private TextField txtExistencia;
    @FXML
    private ComboBox cmbcodigoTipoProducto;
    @FXML
    private ComboBox cmbproveedorId;
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colIdProductoP;
    @FXML
    private TableColumn colDescripcionProductoP;
    @FXML
    private TableColumn colPrecioUnitarioP;
    @FXML
    private TableColumn colPrecioDocenaP;
    @FXML
    private TableColumn colPrecioMayorP;
    @FXML
    private TableColumn colExistenciaP;
    @FXML
    private TableColumn colIdTipoProductoT;
    @FXML
    private TableColumn colProveedoresP;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoTipoProducto.setItems(getTipoProducto());
        cmbproveedorId.setItems(getProveedores());
        cmbcodigoTipoProducto.setDisable(false);
        cmbproveedorId.setDisable(false);

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
                btnAgregarProductos.setText("Guardar");
                btnEliminarProductos.setText("Cancelar");
                btnEditarProductos.setDisable(true);
                btnReportesProveedores.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarProductos.setText("Agregar");
                btnEliminarProductos.setText("Eliminar");
                btnEditarProductos.setDisable(false);
                btnReportesProveedores.setDisable(false);
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
                btnAgregarProductos.setText("Agregar");
                btnEliminarProductos.setText("Eliminar");
                btnEditarProductos.setDisable(false);
                btnReportesProveedores.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro",
                            "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarProductos(?);");
                            procedimiento.setInt(1, ((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
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

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditarProductos.setText("Actualizar");
                    btnReportesProveedores.setText("Cancelar");
                    btnAgregarProductos.setDisable(true);
                    btnEliminarProductos.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/plus.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/basura.png"));
                    activarControles();
                    txtcodigoProducto.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un producto para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarProductos.setText("Editar");
                btnReportesProveedores.setText("Cancelar");
                btnAgregarProductos.setDisable(false);
                btnEliminarProductos.setDisable(false);
                imgEditar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgRecorte.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditarProductos.setText("Editar");
                btnReportesProveedores.setText("Reporte");
                btnAgregarProductos.setDisable(false);
                btnEliminarProductos.setDisable(false);
                imgEditar.setImage(new Image("/org/pablovillela/image/actualizar.png"));
                imgRecorte.setImage(new Image("/org/pablovillela/image/cancel.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                imprimirReporte();
                
        }

    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReporte.mostrarReportes("ReporteProducto.jasper", "Reporte de Productos", parametros);
    }
    
    public void actualizar() {
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_editarProductos(?,?,?,?,?,?,?,?);");
            Productos registro = (Productos) tblProductos.getSelectionModel().getSelectedItem();
            registro.setDescripcionProducto(txtDescripcion.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setCodigoTipoProducto(((TipoProducto) cmbcodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            registro.setProveedorId(((Proveedores) cmbproveedorId.getSelectionModel().getSelectedItem()).getProveedorId());
            p.setInt(1, registro.getCodigoProducto());
            p.setString(2, registro.getDescripcionProducto());
            p.setDouble(3, registro.getPrecioUnitario());
            p.setDouble(4, registro.getPrecioDocena());
            p.setDouble(5, registro.getPrecioMayor());
            p.setInt(6, registro.getExistencia());
            p.setInt(7, registro.getCodigoTipoProducto());
            p.setInt(8, registro.getProveedorId());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar() {
        Productos registro = new Productos();
        registro.setCodigoProducto(Integer.parseInt(txtcodigoProducto.getText()));
        registro.setDescripcionProducto(txtDescripcion.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        registro.setProveedorId(((Proveedores) cmbproveedorId.getSelectionModel().getSelectedItem()).getProveedorId());
        registro.setCodigoTipoProducto(((TipoProducto) cmbcodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getProveedorId());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.execute();
            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cargarDatos() {
        tblProductos.setItems(getProductos());
        colIdProductoP.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProducto"));
        colDescripcionProductoP.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioUnitarioP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioDocenaP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioMayorP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colExistenciaP.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colIdTipoProductoT.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
        colProveedoresP.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("proveedorId"));
    }

    public void selecionarElementos() {
        txtcodigoProducto.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescripcion.setText(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtPrecioUnitario.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioDocena.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioMayor.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbcodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        cmbproveedorId.getSelectionModel().select(buscarProveedores(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getProveedorId()));
    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(resultado.getInt("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getInt("proveedorId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("proveedorId"),
                        resultado.getString("nitProveedor"),
                        resultado.getString("nombreProveedor"),
                        resultado.getString("apellidoProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarProveedores = FXCollections.observableList(lista);
    }

    public ObservableList<TipoProducto> getTipoProducto() {
        ArrayList<TipoProducto> lista = new ArrayList<>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoDeProducto = FXCollections.observableList(lista);
    }

    public TipoProducto buscarTipoProducto(int codigoTipoProducto) {
        TipoProducto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                        registro.getString("descripcion")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public Proveedores buscarProveedores(int proveedorId) {
        Proveedores resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
            procedimiento.setInt(1, proveedorId);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedores(registro.getInt("proveedorId"),
                        registro.getString("nitProveedor"),
                        registro.getString("nombreProveedor"),
                        registro.getString("apellidoProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWeb"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void desactivarControles() {
        txtcodigoProducto.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        cmbcodigoTipoProducto.setDisable(true);
        cmbproveedorId.setDisable(true);

    }

    public void activarControles() {
        txtcodigoProducto.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        cmbcodigoTipoProducto.setDisable(false);
        cmbproveedorId.setDisable(false);

    }

    public void limpiarControles() {
        txtcodigoProducto.clear();
        txtDescripcion.clear();
        txtPrecioUnitario.clear();
        txtPrecioDocena.clear();
        txtPrecioMayor.clear();
        txtExistencia.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbcodigoTipoProducto.getSelectionModel().getSelectedItem();
        cmbproveedorId.getSelectionModel().getSelectedItem();

    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar6) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
