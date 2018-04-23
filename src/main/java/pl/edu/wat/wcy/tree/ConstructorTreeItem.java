package pl.edu.wat.wcy.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javassist.CtConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;
import pl.edu.wat.wcy.manager.JavassistConstructorManager;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConstructorTreeItem extends BaseTreeItem {
    static Image constructorIcon = ResourceLoader.getImage("co.png");
    private CtConstructor constructor;

    public ConstructorTreeItem(CtConstructor constructor) {
        this(constructor, false);
    }

    public ConstructorTreeItem(CtConstructor constructor, boolean changed) {
        super(constructor.getName(), constructor.getLongName(), Type.CONSTRUCTOR, constructorIcon);
        setChanged(changed);
        this.constructor = constructor;
    }

    @Override
    public ContextMenu getMenu() {
        MenuItem addInbox1 = new MenuItem("Nadpisz kod");
        addInbox1.setOnAction(event -> {
            openEditPane("Nadpisz kod " + getRealName(),"{\n\n}", (action, val) -> {
                boolean success = new JavassistConstructorManager(constructor).setBody(val);

                if (!success) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.EDIT);
                    return false;
                }

                setChanged(true);
                return true;
            });
        });

        MenuItem addInbox2 = new MenuItem("Usuń");
        addInbox2.setOnAction(event -> {
            boolean success = new JavassistConstructorManager(constructor).remove();

            if (!success) {
                new ErrorDialog().showErrorDialog(BaseDialog.Action.DELETE);
                return;
            }

            this.getParent().getChildren().remove(this);
        });

        return new ContextMenu(addInbox1, addInbox2);
    }
}
