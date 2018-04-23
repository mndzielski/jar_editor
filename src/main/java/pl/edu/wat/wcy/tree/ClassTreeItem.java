package pl.edu.wat.wcy.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.data.ProgramData;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;
import pl.edu.wat.wcy.manager.JavassistClassManager;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClassTreeItem extends BaseTreeItem {
    static Image classIcon = ResourceLoader.getImage("c.png");
    static Image interfaceIcon = ResourceLoader.getImage("i.png");
    private CtClass ctClass;

    public ClassTreeItem(CtClass ctClass) {
        this(ctClass, false);
    }

    public ClassTreeItem(CtClass ctClass, boolean changed) {
        super(ctClass.getSimpleName(), ctClass.getName(), Type.CLASS, ctClass.isInterface() ? interfaceIcon : classIcon);
        this.ctClass = ctClass;
        setChanged(changed);
    }

    @Override
    public ContextMenu getMenu() {
        Menu parentMenu = new Menu("Dodaj");

        MenuItem addInbox1 = new MenuItem("Metodę");
        addInbox1.setOnAction(event -> {
            System.out.println("Dodaj metode");

            openEditPane("Dodaj metode dla " + getRealName(), (action, val) -> {
                CtMethod method = new JavassistClassManager(ctClass).createMethod(val);

                if (method != null) {
                    getChildren().add(0, new MethodTreeItem(method, true));
                } else {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    return false;
                }
                return true;
            });
        });

        MenuItem addInbox2 = new MenuItem("Pole");
        addInbox2.setOnAction(event -> {
            System.out.println("Dodaj pole");

            openEditPane("Dodaj pole dla " + getRealName(), (action, val) -> {
                CtField field = new JavassistClassManager(ctClass).createField(val);

                if (field != null) {
                    getChildren().add(0, new FieldTreeItem(field, true));
                } else {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    return false;
                }
                return true;
            });
        });

        MenuItem addInbox3 = new MenuItem("Konstruktor");
        addInbox3.setOnAction(event -> {
            System.out.println("Dodaj konstruktor");

            openEditPane("Dodaj konstruktor dla " + getRealName(), "public " + ctClass.getSimpleName() + "() {\n\n}", (action, val) -> {
                CtConstructor constructor = new JavassistClassManager(ctClass).createCtConstructor(val);

                if (constructor != null) {
                    getChildren().add(0, new ConstructorTreeItem(constructor, true));
                } else {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    return false;
                }
                return true;
            });
        });

        parentMenu.getItems().addAll(addInbox1, addInbox2, addInbox3);

        MenuItem addInbox4 = new MenuItem("Usuń");
        addInbox4.setDisable(!isChanged());
        addInbox4.setOnAction(event -> {
            ProgramData.getInstance().getClasses().remove(getCtClass());
            getParent().getChildren().remove(this);
        });

        return new ContextMenu(parentMenu, addInbox4);
    }
}
