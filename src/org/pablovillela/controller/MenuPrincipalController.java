package org.pablovillela.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import org.pablovillela.system.Principal;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    Button btnMenuClientes;
    @FXML
    MenuItem btnMenuProgramador;
    @FXML
    ImageView kinalmart;
    @FXML
    Button btnMenuProveedores;
    @FXML
    Button btnMenuCompras;
    @FXML
    Button btnMenuCargo;
    @FXML
    MenuItem btnMenuProductos;
    @FXML
    MenuItem btnMenuTipoProducto;
    @FXML
    MenuItem btnMenuDetalleCompra;
    @FXML
    MenuItem btnMenuEmpleados;
    @FXML
    MenuItem btnMenuFacturas;
    
    @Override

    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {

        return escenarioPrincipal;

    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {

        this.escenarioPrincipal = escenarioPrincipal;

    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClientesView();
        } else if (event.getSource() == btnMenuProgramador) {
            escenarioPrincipal.menuProgramadorView();
        } else if (event.getSource() == btnMenuProveedores) {
            escenarioPrincipal.menuProveedoresView();
        } else if (event.getSource() == btnMenuCompras) {
            escenarioPrincipal.menuComprasView();
        } else if (event.getSource() == btnMenuCargo) {
            escenarioPrincipal.menuCargoView();
        } else if (event.getSource() == btnMenuProductos) {
            escenarioPrincipal.menuProductosView();
        } else if (event.getSource() == btnMenuTipoProducto) {
            escenarioPrincipal.menuTipoProductoView();
        } else if (event.getSource() == btnMenuDetalleCompra) {
            escenarioPrincipal.menuDetalleCompraView();
        } else if (event.getSource() == btnMenuEmpleados) {
            escenarioPrincipal.menuEmpleadosView();
        } else if (event.getSource() == btnMenuFacturas) {
            escenarioPrincipal.menuFacturasView();
        }
    }

}
