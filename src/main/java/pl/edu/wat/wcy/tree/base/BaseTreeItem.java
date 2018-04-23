package pl.edu.wat.wcy.tree.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.controllers.EditPaneController;
import pl.edu.wat.wcy.controllers.MainController;
import pl.edu.wat.wcy.handlers.EditPaneEventHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseTreeItem extends TreeItem<String> {

    public enum Type {
        ROOT, PACKAGE, CLASS, CONSTRUCTOR, METHOD, FIELD
    }

    protected void openEditPane(String name, String content, EditPaneEventHandler<ActionEvent> handler) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent editPane = loader.load(ResourceLoader.getFxml("editPane.fxml"));
            EditPaneController editPaneController = loader.getController();
            editPaneController.setData(name, content, handler);
            pane.getChildren().clear();
            pane.getChildren().add(editPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void openEditPane(String name, EditPaneEventHandler<ActionEvent> handler) {
        openEditPane(name, null, handler);
    }

    public abstract ContextMenu getMenu();

    private Type type;
    private String realName;
    private AnchorPane pane;
    private boolean isChanged;

    public BaseTreeItem(String value, String realName, Type type, Image icon) {
        super(value);
        this.type = type;
        this.realName = realName;
        this.pane = MainController.getInstance().getEditPaneContainer();
        this.isChanged = false;
        if (icon != null) setGraphic(new ImageView(icon));
    }

    public BaseTreeItem getChildrenWithValue(String value, Type type) {
        List<TreeItem<String>> list = getChildren().stream().filter(x -> x.getValue().equals(value)).filter(x -> ((BaseTreeItem) x).getType().equals(type)).collect(Collectors.toList());
        return list.isEmpty() ? null : (BaseTreeItem) list.get(0);
    }
}
