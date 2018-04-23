package pl.edu.wat.wcy.dialogs.base;

import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import pl.edu.wat.wcy.controllers.MainController;

public abstract class BaseDialog {
    public enum Action {
        CREATE, EDIT, DELETE
    }

    protected void centerDialog(Dialog dialog, double width, double height) {
        Stage scene = MainController.getInstance().getMainStage();
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(width, height);
        double x = scene.getX() + scene.getWidth() / 2 - dialog.getDialogPane().getPrefWidth() / 2;
        double y = scene.getY() + scene.getHeight() / 2 - dialog.getDialogPane().getPrefHeight() / 2;
        dialog.setResizable(false);
        dialog.setX(x);
        dialog.setY(y);
    }

    protected void centerDialog(Dialog dialog) {
        centerDialog(dialog, 400, 100);
    }
}
