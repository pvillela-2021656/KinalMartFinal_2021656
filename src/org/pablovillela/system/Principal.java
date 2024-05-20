package org.pablovillela.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.pablovillela.controller.MenuCargoEmpleadoController;
import org.pablovillela.controller.MenuClientesController;
import org.pablovillela.controller.MenuComprasController;
import org.pablovillela.controller.MenuPrincipalController;
import org.pablovillela.controller.MenuProductosController;
import org.pablovillela.controller.MenuProgramadorController;
import org.pablovillela.controller.MenuProveedoresController;
import org.pablovillela.controller.MenuTipoProductoController;

public class Principal extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;
    //private final String URLVIEW = "/org/pablovillela/view/";

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("KinalMart");
        escenarioPrincipal.getIcons().add(new Image(Principal.class.getResourceAsStream("/org/pablovillela/image/kinalpvfoto.png")));
        menuPrincipalView();
        //Parent root = FXMLLoader.load(getClass().getResource("/org/pablovillela/view/MenuInicioPV.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception {
        Initializable resultado = null;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Principal.class.getResourceAsStream("/org/pablovillela/view/" + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Principal.class.getResource("/org/pablovillela/view/" + fxmlName));
        escena = new Scene((AnchorPane) loader.load(file), width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) loader.getController();
        return resultado;
    }

    public void menuPrincipalView() {
        try {
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController) cambiarEscena("MenuPV.fxml", 615, 407);
            menuPrincipalView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuClientesView() {
        try {
            MenuClientesController menuClientesView = (MenuClientesController) cambiarEscena("ClienteMenuView.fxml", 940, 727);
            menuClientesView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuProgramadorView() {
        try {
            MenuProgramadorController menuProgramadorView = (MenuProgramadorController) cambiarEscena("ProgramadorPV1.fxml", 600, 400);
            menuProgramadorView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuProveedoresView() {
        try {
            MenuProveedoresController menuProveedoresView = (MenuProveedoresController) cambiarEscena("ProveedoresPV.fxml", 940, 727);
            menuProveedoresView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuComprasView() {
        try {
            MenuComprasController menuComprasView = (MenuComprasController) cambiarEscena("ComprasPV.fxml", 940, 727);
            menuComprasView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuCargoView() {
        try {
            MenuCargoEmpleadoController menuCargoView = (MenuCargoEmpleadoController) cambiarEscena("CargoPV.fxml", 940, 727);
            menuCargoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuProductosView() {
        try {
            MenuProductosController menuProductosView = (MenuProductosController) cambiarEscena("ProductosPV.fxml", 940, 727);
            menuProductosView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void menuTipoProductoView() {
        try {
            MenuTipoProductoController menuTipoProductoView = (MenuTipoProductoController) cambiarEscena("TipoProductoPV.fxml", 940, 727);
            menuTipoProductoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
