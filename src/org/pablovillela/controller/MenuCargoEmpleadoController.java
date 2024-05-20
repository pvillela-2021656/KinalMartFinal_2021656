package org.pablovillela.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.pablovillela.system.Principal;

public class MenuCargoEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private Button btnRegresar5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar5) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
