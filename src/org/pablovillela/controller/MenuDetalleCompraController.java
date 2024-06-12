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
import org.pablovillela.bean.Compras;
import org.pablovillela.bean.DetalleCompra;
import org.pablovillela.bean.Productos;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuDetalleCompraController implements Initializable {

    private Principal escenarioPrincipal;

    
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProductos;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    private ObservableList<Compras> listaCompras;

    @FXML
    private Button btnRegresarDetalleCompra;
    @FXML
    private Button btnAgregarDetalleCompra;
    @FXML
    private Button btnEliminarDetalleCompra;
    @FXML
    private Button btnEditarDetalleCompra;
    @FXML
    private Button btnReportesDetalleCompra;
    @FXML
    private TextField txtIdDetalleCompra;
    @FXML
    private TextField txtCostoUnitario;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox cmbcodigoProducto;
    @FXML
    private ComboBox cmbNumeroDocumento;
    @FXML
    private TableColumn colIdDetalleCompra;
    @FXML
    private TableColumn colCostoUnitario;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colIdProductoP;
    @FXML
    private TableColumn colNumeroDocumento;
    @FXML
    private TableView tblDetalleCompras;
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
        cmbcodigoProducto.setItems(getProductos());
        cmbNumeroDocumento.setItems(getCompras());
        cmbcodigoProducto.setDisable(false);
        cmbNumeroDocumento.setDisable(false);
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
                btnAgregarDetalleCompra.setText("Guardar");
                btnEliminarDetalleCompra.setText("Cancelar");
                btnEditarDetalleCompra.setDisable(true);
                btnReportesDetalleCompra.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarDetalleCompra.setText("Agregar");
                btnEliminarDetalleCompra.setText("Eliminar");
                btnEditarDetalleCompra.setDisable(false);
                btnReportesDetalleCompra.setDisable(false);
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
                btnAgregarDetalleCompra.setText("Agregar");
                btnEliminarDetalleCompra.setText("Eliminar");
                btnEditarDetalleCompra.setDisable(false);
                btnReportesDetalleCompra.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblDetalleCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del detalle de compra",
                            "Eliminar Detalle De Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarDetalleCompra(?);");
                            procedimiento.setInt(1, ((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listaDetalleCompra.remove(tblDetalleCompras.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un detalle de compra para Eliminar");
                }
                break;
        }

    }
    
    public void guardar() {
        DetalleCompra registro = new DetalleCompra();
        registro.setCodigoDetalleCompra(Integer.parseInt(txtIdDetalleCompra.getText()));
        registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setCodigoProducto(((Productos) cmbcodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroDocumento(((Compras) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompra.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void actualizar() {
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("{call sp_editarDetalleCompra(?,?,?,?,?)}");
            DetalleCompra registro = (DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem();
            registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setCodigoProducto(((Productos) cmbcodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroDocumento(((Compras) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            p.setInt(1, registro.getCodigoDetalleCompra());
            p.setDouble(2, registro.getCostoUnitario());
            p.setInt(3, registro.getCantidad());
            p.setInt(4, registro.getCodigoProducto());
            p.setInt(5, registro.getNumeroDocumento());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargarDatos() {
        tblDetalleCompras.setItems(getDetalleCompra());
        colIdDetalleCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoDetalleCompra"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        colIdProductoP.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
    }

    public void selecionarElementos() {
        txtIdDetalleCompra.setText(String.valueOf(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCostoUnitario.setText(String.valueOf(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbcodigoProducto.getSelectionModel().select(buscarProductos(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbNumeroDocumento.getSelectionModel().select(buscarCompras(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
    }
    
    public void desactivarControles() {
        txtIdDetalleCompra.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbcodigoProducto.setDisable(true);
        cmbNumeroDocumento.setDisable(true);

    }

    public void activarControles() {
        txtIdDetalleCompra.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbcodigoProducto.setDisable(false);
        cmbNumeroDocumento.setDisable(false);

    }

    public void limpiarControles() {
        txtIdDetalleCompra.clear();
        txtCostoUnitario.clear();
        txtCantidad.clear();
        tblDetalleCompras.getSelectionModel().getSelectedItem();
        cmbcodigoProducto.getSelectionModel().getSelectedItem();
        cmbNumeroDocumento.getSelectionModel().getSelectedItem();

    }
    
    public Productos buscarProductos(int codigoProducto) {
        Productos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?);}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Productos(registro.getInt("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getInt("existencia"),
                        registro.getInt("codigoTipoProducto"),
                        registro.getInt("proveedorId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

        public Compras buscarCompras(int numeroDocumento) {
        Compras resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCompras(?)}");
            procedimiento.setInt(1, numeroDocumento);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Compras(registro.getInt("numeroDocumento"),
                        registro.getString("fechaDocumento"),
                        registro.getString("descripcion"),
                        registro.getDouble("totalDocumento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getString("fechaDocumento"),
                        resultado.getString("descripcion"),
                        resultado.getDouble("totalDocumento")
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCompras = FXCollections.observableList(lista);
    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<>();

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

        return listaProductos = FXCollections.observableList(lista);
    }
    
    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new DetalleCompra(resultado.getInt("codigoDetalleCompra"),
                        resultado.getDouble("costoUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("codigoProducto"),
                        resultado.getInt("numeroDocumento")
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableList(lista);
    }
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarDetalleCompra) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
    

}
