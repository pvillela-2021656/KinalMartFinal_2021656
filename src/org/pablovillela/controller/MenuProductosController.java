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
import org.pablovillela.system.Principal;
import org.pablovillela.bean.Productos;
import org.pablovillela.bean.TipoProducto;
import org.pablovillela.bean.Proveedores;
import org.pablovillela.db.Conexion;

public class MenuProductosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProductos;
    private ObservableList <Proveedores> listaProveedores;
    private ObservableList <TipoProducto> listaTipoDeProducto;
    
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
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    public void agregar (){
         switch(tipoDeOperacion){
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
             //guardar();
             desactivarControles();
             limpiarControles ();
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
    
    public void selecionarElementos(){
       txtcodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
       txtDescripcion.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbcodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));

       
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
    
    public TipoProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                                            registro.getString("descripcionProducto")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
    
    public void desactivarControles(){
        txtcodigoProducto.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        cmbcodigoTipoProducto.setDisable(true);
        cmbproveedorId.setDisable(true);
    
    }
      public void activarControles(){
        txtcodigoProducto.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        cmbcodigoTipoProducto.setDisable(false);
        cmbproveedorId.setDisable(false);
    
    }
      public void limpiarControles(){
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
