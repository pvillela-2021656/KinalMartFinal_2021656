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
import org.pablovillela.bean.TipoProducto;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuTipoProductoController implements Initializable {

    private Principal escenarioPrincipal;
    
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoProducto> listaTipoProducto;
    
    @FXML
    private Button btnRegresar7;
    @FXML
    private Button btnAgregarTP;
    @FXML
    private Button btnEliminarTP;
    @FXML
    private Button btnReportesTP;
    @FXML
    private Button btnEditarTP;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private TextField txtIdTipoProducto;
    @FXML
    private TextField txtDescripcionTipoProducto;
    @FXML
    private TableColumn colIdTipoProductoT;
    @FXML
    private TableColumn colDescripcionTipoProductoT;
    @FXML
    private TableView tblTipoProductos;
    
    
    
    
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
                btnAgregarTP.setText("Guardar");
                btnEliminarTP.setText("Cancelar");
                btnEditarTP.setDisable(true);
                btnReportesTP.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarTP.setText("Agregar");
                btnEliminarTP.setText("Eliminar");
                btnEditarTP.setDisable(false);
                btnReportesTP.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;

        }

    }
    
    public void guardar() {
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtIdTipoProducto.getText()));
        registro.setDescripcion(txtDescripcionTipoProducto.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarTipoProducto(?,?)}");
            Procedimiento.setInt(1, registro.getCodigoTipoProducto());
            Procedimiento.setString(2, registro.getDescripcion());
            listaTipoProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarTP.setText("Agregar");
                btnEliminarTP.setText("Eliminar");
                btnEditarTP.setDisable(false);
                btnReportesTP.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblTipoProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del tipo de producto",
                            "Eliminar tipo producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((TipoProducto) tblTipoProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoProducto.remove(tblTipoProductos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Cliente antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblTipoProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditarTP.setText("Actualizar");
                    btnReportesTP.setText("Cancelar");
                    btnAgregarTP.setDisable(true);
                    btnEliminarTP.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/actualizar.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/cancel.png"));
                    activarControles();
                    txtIdTipoProducto.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un tipo de producto para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarTP.setText("Editar");
                btnReportesTP.setText("Reporte");
                btnAgregarTP.setDisable(false);
                btnEliminarTP.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_editarTipoProducto(?,?);");
            TipoProducto registro = (TipoProducto) tblTipoProductos.getSelectionModel().getSelectedItem();
            registro.setDescripcion((txtDescripcionTipoProducto.getText()));
            Procedimiento.setInt(1, registro.getCodigoTipoProducto()); 
            Procedimiento.setString(2, registro.getDescripcion());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void cargarDatos() {
        tblTipoProductos.setItems(getTipoProducto());
        colIdTipoProductoT.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
        colDescripcionTipoProductoT.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcion"));
    }
    
    public void seleccionarElemento() {
        txtIdTipoProducto.setText(String.valueOf(((TipoProducto) tblTipoProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtDescripcionTipoProducto.setText(((TipoProducto) tblTipoProductos.getSelectionModel().getSelectedItem()).getDescripcion());

    }

    public void desactivarControles() {
        txtIdTipoProducto.setEditable(false);
        txtDescripcionTipoProducto.setEditable(false);

    }
    public void activarControles() {
        txtIdTipoProducto.setEditable(true);
        txtDescripcionTipoProducto.setEditable(true);

    }

    public void limpiarControles() {
        txtIdTipoProducto.clear();
        txtDescripcionTipoProducto.clear();

    }
    
    public ObservableList<TipoProducto> getTipoProducto() {
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto ()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")

                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoProducto = FXCollections.observableList(lista);
    }
    
   
    
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar7) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
