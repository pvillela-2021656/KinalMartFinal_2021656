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
import org.pablovillela.system.Principal;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.pablovillela.bean.Clientes;
import org.pablovillela.bean.Proveedores;
import org.pablovillela.db.Conexion;

public class MenuProveedoresController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Proveedores> listaProveedores;

    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private Button btnRegresar3;
    @FXML
    private Button btnAgregarProveedores;
    @FXML
    private Button btnEliminarProveedores;
    @FXML
    private Button btnReportesProveedores;
    @FXML
    private Button btnEditarProveedores;
    @FXML
    private TextField txtDireccionProv;
    @FXML
    private TextField txtIdProveedor;
    @FXML
    private TextField txtContactoProv;
    @FXML
    private TextField txtNombreProv;
    @FXML
    private TextField txtApellidoProv;
    @FXML
    private TextField txtNitProv;
    @FXML
    private TextField txtRazonSocial;
    @FXML
    private TextField txtPagWeb;
    @FXML
    private TableView tblProveedores;
    @FXML
    private TableColumn colProveedoresP;
    @FXML
    private TableColumn colNitP;
    @FXML
    private TableColumn colNombreP;
    @FXML
    private TableColumn colApellidoP;
    @FXML
    private TableColumn colDireccionP;
    @FXML
    private TableColumn colRazonP;
    @FXML
    private TableColumn colContactoP;
    @FXML
    private TableColumn colPagWebP;

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
                btnAgregarProveedores.setText("Guardar");
                btnEliminarProveedores.setText("Cancelar");
                btnEditarProveedores.setDisable(true);
                btnReportesProveedores.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarProveedores.setText("Agregar");
                btnEliminarProveedores.setText("Eliminar");
                btnEditarProveedores.setDisable(false);
                btnReportesProveedores.setDisable(false);
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
                btnAgregarProveedores.setText("Agregar");
                btnEliminarProveedores.setText("Eliminar");
                btnEditarProveedores.setDisable(false);
                btnReportesProveedores.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarProveedores(?)}");
                            procedimiento.setInt(1, ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getProveedorId());
                            procedimiento.execute();
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
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
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditarProveedores.setText("Actualizar");
                    btnReportesProveedores.setText("Cancelar");
                    btnAgregarProveedores.setDisable(true);
                    btnEliminarProveedores.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/actualizar.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/cancel.png"));
                    activarControles();
                    txtIdProveedor.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un proveedor para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarProveedores.setText("Editar");
                btnReportesProveedores.setText("Reporte");
                btnAgregarProveedores.setDisable(false);
                btnEliminarProveedores.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colProveedoresP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("proveedorId"));
        colNitP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nitProveedor"));
        colNombreP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombreProveedor"));
        colApellidoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidoProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colRazonP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
        colContactoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colPagWebP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWeb"));
    }

    public void seleccionarElemento() {
        txtIdProveedor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getProveedorId()));
        txtNitProv.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNitProveedor());
        txtNombreProv.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor());
        txtApellidoProv.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor());
        txtDireccionProv.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txtRazonSocial.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContactoProv.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtPagWeb.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());

    }

    public void desactivarControles() {
        txtIdProveedor.setEditable(false);
        txtNitProv.setEditable(false);
        txtNombreProv.setEditable(false);
        txtApellidoProv.setEditable(false);
        txtDireccionProv.setEditable(false);
        txtRazonSocial.setEditable(false);
        txtContactoProv.setEditable(false);
        txtPagWeb.setEditable(false);
    }

    public void activarControles() {
        txtIdProveedor.setEditable(true);
        txtNitProv.setEditable(true);
        txtNombreProv.setEditable(true);
        txtApellidoProv.setEditable(true);
        txtDireccionProv.setEditable(true);
        txtRazonSocial.setEditable(true);
        txtContactoProv.setEditable(true);
        txtPagWeb.setEditable(true);
    }

    public void limpiarControles() {
        txtIdProveedor.clear();
        txtNitProv.clear();
        txtNombreProv.clear();
        txtApellidoProv.clear();
        txtDireccionProv.clear();
        txtRazonSocial.clear();
        txtContactoProv.clear();
        txtPagWeb.clear();
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNitProveedor(txtNitProv.getText());
            registro.setNombreProveedor(txtNombreProv.getText());
            registro.setApellidoProveedor(txtApellidoProv.getText());
            registro.setDireccionProveedor(txtDireccionProv.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setContactoPrincipal(txtContactoProv.getText());
            registro.setPaginaWeb(txtPagWeb.getText());
            Procedimiento.setInt(1, registro.getProveedorId());
            Procedimiento.setString(2, registro.getNitProveedor());
            Procedimiento.setString(3, registro.getNombreProveedor());
            Procedimiento.setString(4, registro.getApellidoProveedor());
            Procedimiento.setString(5, registro.getDireccionProveedor());
            Procedimiento.setString(6, registro.getRazonSocial());
            Procedimiento.setString(7, registro.getContactoPrincipal());
            Procedimiento.setString(8, registro.getPaginaWeb());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores ()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("proveedorId"),
                        resultado.getString("nitProveedor"),
                        resultado.getString("nombreProveedor"),
                        resultado.getString("apellidoProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(lista);
    }

    public void guardar() {
        Proveedores registro = new Proveedores();
        registro.setProveedorId(Integer.parseInt(txtIdProveedor.getText()));
        registro.setNitProveedor(txtNitProv.getText());
        registro.setNombreProveedor(txtNombreProv.getText());
        registro.setApellidoProveedor(txtApellidoProv.getText());
        registro.setDireccionProveedor(txtDireccionProv.getText());
        registro.setRazonSocial(txtRazonSocial.getText());
        registro.setContactoPrincipal(txtContactoProv.getText());
        registro.setPaginaWeb(txtPagWeb.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?,?,?,?,?,?,?,?)}");
            Procedimiento.setInt(1, registro.getProveedorId());
            Procedimiento.setString(2, registro.getNitProveedor());
            Procedimiento.setString(3, registro.getNombreProveedor());
            Procedimiento.setString(4, registro.getApellidoProveedor());
            Procedimiento.setString(5, registro.getDireccionProveedor());
            Procedimiento.setString(6, registro.getRazonSocial());
            Procedimiento.setString(7, registro.getContactoPrincipal());
            Procedimiento.setString(8, registro.getPaginaWeb());
            Procedimiento.execute();
            listaProveedores.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar3) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
