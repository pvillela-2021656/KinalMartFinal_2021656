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
import org.pablovillela.bean.Compras;
import org.pablovillela.db.Conexion;
import org.pablovillela.system.Principal;

public class MenuComprasController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Compras> listaCompras;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgRecorte;
    @FXML
    private Button btnRegresar4;
    @FXML
    private Button btnAgregarCompras;
    @FXML
    private Button btnEliminarCompras;
    @FXML
    private Button btnReportesCompras;
    @FXML
    private Button btnEditarCompras;
    @FXML
    private TextField txtNumeroDocumento;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtTotalDocumento;
    @FXML
    private TableView tblCompras;
    @FXML
    private TableColumn colNumeroDocumento;
    @FXML
    private TableColumn colFechaDocumento;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colTotalDocumento;

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
                btnAgregarCompras.setText("Guardar");
                btnEliminarCompras.setText("Cancelar");
                btnEditarCompras.setDisable(true);
                btnReportesCompras.setDisable(true);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCompras.setText("Agregar");
                btnEliminarCompras.setText("Eliminar");
                btnEditarCompras.setDisable(false);
                btnReportesCompras.setDisable(false);
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
                btnAgregarCompras.setText("Agregar");
                btnEliminarCompras.setText("Eliminar");
                btnEditarCompras.setDisable(false);
                btnReportesCompras.setDisable(false);
                imgAgregar.setImage(new Image("/org/pablovillela/image/plus.png"));
                imgEliminar.setImage(new Image("/org/pablovillela/image/basura.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((Compras) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar una Compra antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCompras.setText("Actualizar");
                    btnReportesCompras.setText("Cancelar");
                    btnAgregarCompras.setDisable(true);
                    btnEliminarCompras.setDisable(true);
                    imgEditar.setImage(new Image("/org/pablovillela/image/actualizar.png"));
                    imgRecorte.setImage(new Image("/org/pablovillela/image/cancel.png"));
                    activarControles();
                    txtNumeroDocumento.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una compra para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCompras.setText("Editar");
                btnReportesCompras.setText("Reporte");
                btnAgregarCompras.setDisable(false);
                btnEliminarCompras.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras) tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(txtFecha.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotalDocumento(Double.parseDouble(txtTotalDocumento.getText()));
            Procedimiento.setInt(1, registro.getNumeroDocumento());
            Procedimiento.setString(2, registro.getFechaDocumento());
            Procedimiento.setString(3, registro.getDescripcion());
            Procedimiento.setDouble(4, registro.getTotalDocumento());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {

    }

    public void cargarDatos() {
        tblCompras.setItems(getCompras());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDocumento.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        colTotalDocumento.setCellValueFactory(new PropertyValueFactory<Compras, String>("totalDocumento"));
    }

    public void seleccionarElemento() {
        txtNumeroDocumento.setText(String.valueOf(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtFecha.setText(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtDescripcion.setText(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        double total = ((Compras) tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento();
        txtTotalDocumento.setText(String.valueOf(total));

    }

    public void desactivarControles() {
        txtNumeroDocumento.setEditable(false);
        txtFecha.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotalDocumento.setEditable(false);
    }

    public void activarControles() {
        txtNumeroDocumento.setEditable(true);
        txtFecha.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotalDocumento.setEditable(true);
    }

    public void limpiarControles() {
        txtNumeroDocumento.clear();
        txtFecha.clear();
        txtDescripcion.clear();
        txtTotalDocumento.clear();
    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras() }");
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

    public void guardar() {
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtNumeroDocumento.getText()));
        registro.setFechaDocumento(txtFecha.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(Double.parseDouble(txtTotalDocumento.getText()));
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?,?,?,?)}");
            Procedimiento.setInt(1, registro.getNumeroDocumento());
            Procedimiento.setString(2, registro.getFechaDocumento());
            Procedimiento.setString(3, registro.getDescripcion());
            Procedimiento.setDouble(4, registro.getTotalDocumento());
            listaCompras.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar4) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
