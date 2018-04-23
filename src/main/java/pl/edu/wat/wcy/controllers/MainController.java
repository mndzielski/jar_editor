package pl.edu.wat.wcy.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.NotFoundException;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.data.ProgramData;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.FileDialog;
import pl.edu.wat.wcy.dialogs.InfoDialog;
import pl.edu.wat.wcy.jar.JarFileLoader;
import pl.edu.wat.wcy.jar.JarHandler;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static MainController controller;
    public HBox hBox;

    @FXML
    private MenuItem closeMenu;

    @FXML
    private MenuItem openMenu;

    @FXML
    private MenuItem saveMenu;

    public static MainController getInstance() {
        return controller;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
        mainStage.setOnCloseRequest(event1 -> {
            closeJarFile(null);
            Platform.exit();
            System.exit(0);
        });
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private Stage mainStage;

    public AnchorPane getTreeContainer() {
        return treeContainer;
    }

    public HBox getHBox() {
        return hBox;
    }

    public AnchorPane getEditPaneContainer() {
        return editPaneContainer;
    }

    @FXML
    AnchorPane treeContainer;

    @FXML
    AnchorPane editPaneContainer;

    public void initialize(URL location, ResourceBundle resources) {
        controller = this;

    }

    private void handleOpenClick(String path) {
        System.out.println(path);
        try {
            BaseTreeItem baseTreeItem = JarFileLoader.getClassesList(path);

            FXMLLoader loader = new FXMLLoader();
            Parent tree = loader.load(ResourceLoader.getFxml("tree.fxml"));
            TreeController treeController = loader.getController();
            treeController.setData(baseTreeItem);

            treeContainer.getChildren().add(tree);

            openMenu.setDisable(true);
            saveMenu.setDisable(false);
            closeMenu.setDisable(false);
        } catch (IOException e) {
            new ErrorDialog().showErrorDialog("Nie można otworzyć pliku.");
        } catch (ClassNotFoundException | NotFoundException e) {
            new ErrorDialog().showErrorDialog("Nie znaleziono klasy.");
        }
    }

    private String getJarFilePath() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Wybierz plik");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Jar Files",
                        "*.jar"));

        File jarFile = fc.showOpenDialog(mainStage);

        return jarFile != null ? jarFile.getPath() : null;
    }

    @FXML
    private void openJarFile(ActionEvent event) {
        String path = getJarFilePath();

        if (path != null) {
            handleOpenClick(path);
        }
    }

    @FXML
    private void saveJarFile(ActionEvent event) {
        if (new JarHandler().save(ProgramData.getInstance())) {
            openMenu.setDisable(false);
            saveMenu.setDisable(true);
            closeMenu.setDisable(true);

            editPaneContainer.getChildren().clear();
            treeContainer.getChildren().clear();

            new InfoDialog().showInfo("Zapisano", "Pomyślnie zapisano plik.");
        }
    }

    @FXML
    private void closeApplication(ActionEvent event) {
        mainStage.close();
    }

    @FXML
    private void showHelp(ActionEvent event) {
        new InfoDialog().showInfo("O Programie", "JarEditor 1.0");
    }

    @FXML
    private void closeJarFile(ActionEvent event) {
        ClassPath classPath = ProgramData.getInstance().getClassPath();
        if (classPath != null) {
            ClassPool.getDefault().removeClassPath(classPath);
            ProgramData.getInstance().setClassPath(null);

            openMenu.setDisable(false);
            saveMenu.setDisable(true);
            closeMenu.setDisable(true);

            editPaneContainer.getChildren().clear();
            treeContainer.getChildren().clear();

            if (event != null) {
                new InfoDialog().showInfo("Zamknięto", "Pomyślnie zamknięto plik.");
            }
        }
    }
}