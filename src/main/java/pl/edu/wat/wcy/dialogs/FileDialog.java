package pl.edu.wat.wcy.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;

public class FileDialog extends BaseDialog {
    public void show(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dialog");
        alert.setHeaderText("Zawartość pliku: ");

        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setOnMouseClicked(event -> System.out.println(textArea.getText()));

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        centerDialog(alert, 600,500);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }
}
