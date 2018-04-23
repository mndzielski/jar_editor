package pl.edu.wat.wcy.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javassist.CtField;
import javassist.NotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;
import pl.edu.wat.wcy.manager.JavassistFieldManager;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

@EqualsAndHashCode(callSuper = true)
@Data
public class FieldTreeItem extends BaseTreeItem {
    static Image fieldIcon = ResourceLoader.getImage("f.png");
    private CtField field;

    public FieldTreeItem(CtField field) {
        this(field, false);
    }

    public FieldTreeItem(CtField field, boolean changed) {
        super(field.getName(), getName(field), Type.FIELD, fieldIcon);

        setChanged(changed);
        this.field = field;
    }

    private static String getName(CtField field) {
        try {
            return field.getType().getName() + " " + field.getName();
        } catch (NotFoundException e) {
            return field.getName();
        }
    }

    @Override
    public ContextMenu getMenu() {
        MenuItem addInbox1 = new MenuItem("Usuń");
        addInbox1.setOnAction(event -> {
            boolean success = new JavassistFieldManager(field).remove();

            if (!success) {
                new ErrorDialog().showErrorDialog(BaseDialog.Action.DELETE);
                return;
            }

            this.getParent().getChildren().remove(this);
        });

        return new ContextMenu(addInbox1);
    }
}
