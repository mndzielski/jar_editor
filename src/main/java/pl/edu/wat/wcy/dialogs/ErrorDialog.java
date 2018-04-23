package pl.edu.wat.wcy.dialogs;

import javafx.scene.control.Alert;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;

public class ErrorDialog extends BaseDialog {

    private final String COMMON_ERROR_ALERT_TITLE = "Wystąpił błąd";
    private final String COMMON_CREATE_ERROR_ALERT_CONTENT = "Wprowadzone dane są niepoprawne";
    private final String COMMON_DELETE_ERROR_ALERT_CONTENT = "Wystąpił błąd podczas usuwania.";
    private final String COMMON_EDIT_ERROR_ALERT_CONTENT = "Wprowadzone dane są niepoprawne";

    public void showErrorDialog(Action action) {
        switch (action) {
            case CREATE:
                showErrorDialog(COMMON_ERROR_ALERT_TITLE, COMMON_CREATE_ERROR_ALERT_CONTENT);
                break;
            case EDIT:
                showErrorDialog(COMMON_ERROR_ALERT_TITLE, COMMON_EDIT_ERROR_ALERT_CONTENT);
                break;
            case DELETE:
                showErrorDialog(COMMON_ERROR_ALERT_TITLE, COMMON_DELETE_ERROR_ALERT_CONTENT);
                break;

        }
    }

    public void showErrorDialog(String content) {
        showErrorDialog(COMMON_ERROR_ALERT_TITLE, content);
    }

    public void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        centerDialog(alert);

        alert.showAndWait();
    }
}
