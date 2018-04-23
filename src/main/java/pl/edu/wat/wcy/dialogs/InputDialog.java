package pl.edu.wat.wcy.dialogs;

import javafx.scene.control.TextInputDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;

import java.util.Optional;

public class InputDialog extends BaseDialog {
    public String getTextInputDialog(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        centerDialog(dialog);

        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
