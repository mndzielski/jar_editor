package pl.edu.wat.wcy.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;
import pl.edu.wat.wcy.manager.JavassistMethodManager;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

@EqualsAndHashCode(callSuper = true)
@Data
public class MethodTreeItem extends BaseTreeItem {
    static Image methodIcon = ResourceLoader.getImage("m.png");
    private CtMethod method;

    public MethodTreeItem(CtMethod method) {
        this(method, false);
    }

    public MethodTreeItem(CtMethod method, boolean changed) {
        super(method.getName(), getName(method), Type.METHOD, methodIcon);
        this.setChanged(changed);
        this.method = method;
    }

    private static String getName(CtMethod method) {
        try {
            return method.getReturnType().getName() + " " + method.getLongName();
        } catch (NotFoundException e) {
            return method.getLongName();
        }
    }

    @Override
    public ContextMenu getMenu() {
        Menu parentMenu = new Menu("Dodaj kod");

        MenuItem addInbox1 = new MenuItem("Na początek");
        addInbox1.setOnAction(event -> {
            System.out.println("Dodaj kod na poczatek");
            openEditPane("Dodaj kod na poczatek " + getRealName(), (action, val) -> {

                boolean success = new JavassistMethodManager(method).insertBefore(val);

                if (!success) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.EDIT);
                    return false;
                }

                setChanged(true);
                return true;
            });
        });

        MenuItem addInbox2 = new MenuItem("Na koniec");
        addInbox2.setOnAction(event -> {
            openEditPane("Dodaj kod na koniec " + getRealName(), (action, val) -> {
                boolean success = new JavassistMethodManager(method).insertAfter(val);

                if (!success) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.EDIT);
                    return false;
                }

                setChanged(true);
                return true;
            });
        });

        parentMenu.getItems().addAll(addInbox1, addInbox2);

        MenuItem addInbox3 = new MenuItem("Nadpisz kod");
        addInbox3.setOnAction(event -> {
            openEditPane("Nadpisz kod " + getRealName(),"{\n\n}", (action, val) -> {
                boolean success = new JavassistMethodManager(method).setBody(val);

                if (!success) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.EDIT);
                    return false;
                }

                setChanged(true);
                return true;
            });
        });

        MenuItem addInbox4 = new MenuItem("Usuń");
        addInbox4.setOnAction(event -> {
            boolean success = new JavassistMethodManager(method).remove();

            if (!success) {
                new ErrorDialog().showErrorDialog(BaseDialog.Action.DELETE);
                return;
            }

            this.getParent().getChildren().remove(this);
        });

        return new ContextMenu(parentMenu, addInbox3, addInbox4);
    }
}
