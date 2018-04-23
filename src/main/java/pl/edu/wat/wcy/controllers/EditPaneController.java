package pl.edu.wat.wcy.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import pl.edu.wat.wcy.handlers.EditPaneEventHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPaneController implements Initializable {

    private static EditPaneController controller;

    public static EditPaneController getInstance() {
        return controller;
    }

    @FXML
    private AnchorPane editPane;

    @FXML
    private Label name;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private TextArea textarea;

    public void initialize(URL location, ResourceBundle resources) {
        controller = this;
        TreeController.getInstance().disable(true);
    }

    public void setData(String realName, String content,  EditPaneEventHandler<ActionEvent> handler) {
        name.setText(realName);
        if(content != null) {
            textarea.setText(content);
        }

        EventHandler<ActionEvent> closeHandler = event -> {
            MainController.getInstance().getEditPaneContainer().getChildren().clear();
            TreeController.getInstance().disable(false);
        };

        save.setOnAction(event -> {
            if (handler.handle(event, textarea.getText())) {
                closeHandler.handle(event);
            }
        });

        cancel.setOnAction(closeHandler);
    }
}
