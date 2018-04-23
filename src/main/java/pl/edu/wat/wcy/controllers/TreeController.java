package pl.edu.wat.wcy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import lombok.Data;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class TreeController implements Initializable {

    static TreeController controller;

    final String CHANGED_STYLE_CLASS = "changed";

    public static TreeController getInstance() {
        return controller;
    }

    @FXML
    TreeView treeView;

    private final class TreeCellImpl extends TreeCell<String> {

        public TreeCellImpl() {
            super();
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(selected);

            if (getTreeItem() != null && ((BaseTreeItem) getTreeItem()).isChanged()) {
                getStyleClass().add(CHANGED_STYLE_CLASS);
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
                setContextMenu(null);
                setTooltip(null);

            } else {
                setText(getItem() == null ? "" : getItem());
                setGraphic(getTreeItem().getGraphic());
                if (((BaseTreeItem) getTreeItem()).isChanged()) {
                    if (!getStyleClass().contains(CHANGED_STYLE_CLASS)) {
                        getStyleClass().add(CHANGED_STYLE_CLASS);
                    }
                } else {
                    while (getStyleClass().contains(CHANGED_STYLE_CLASS)) {
                        getStyleClass().remove(CHANGED_STYLE_CLASS);
                    }
                }
                BaseTreeItem clsPartTreeItem = (BaseTreeItem) getTreeItem();
                if (getItem() != null && !((BaseTreeItem) getTreeItem()).getType().equals(BaseTreeItem.Type.ROOT)) {
                    setTooltip(new Tooltip(((BaseTreeItem) getTreeItem()).getRealName()));
                }

                if (clsPartTreeItem != null)
                    setContextMenu(clsPartTreeItem.getMenu());
            }
        }
    }

    public void disable(boolean value) {
        treeView.setDisable(value);
    }

    public void initialize(URL location, ResourceBundle resources) {
        controller = this;
    }

    public void setData(BaseTreeItem baseTreeItem) {
        baseTreeItem.setExpanded(true);
        treeView.setRoot(baseTreeItem);
        treeView.setCellFactory((param) -> new TreeCellImpl());
    }
}