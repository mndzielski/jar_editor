package pl.edu.wat.wcy.dialogs;

import javafx.scene.control.Alert;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;

public class InfoDialog extends BaseDialog {
    public void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        centerDialog(alert);

        alert.showAndWait();
    }
}
