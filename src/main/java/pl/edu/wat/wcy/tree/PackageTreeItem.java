package pl.edu.wat.wcy.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.edu.wat.wcy.ResourceLoader;
import pl.edu.wat.wcy.data.ProgramData;
import pl.edu.wat.wcy.dialogs.ErrorDialog;
import pl.edu.wat.wcy.dialogs.InputDialog;
import pl.edu.wat.wcy.dialogs.base.BaseDialog;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PackageTreeItem extends BaseTreeItem {
    static Image packageIcon = ResourceLoader.getImage("package.png");

    //Dla roota
    public PackageTreeItem(String value, String path, Type root) {
        this(value, path, root, false);
    }

    //dla package
    public PackageTreeItem(String[] path, int i) {
        this(path[i], buildPackage(path, i), false);
    }

    private static String buildPackage(String[] path, int n) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            out.add(path[i]);
        }

        StringBuffer buffer = new StringBuffer();

        for (Iterator iterator = out.iterator(); iterator.hasNext(); buffer.append((String) iterator.next())) {
            if (buffer.length() != 0) {
                buffer.append(".");
            }
        }

        return buffer.toString();
    }

    //dla package
    public PackageTreeItem(String value, String path, boolean added) {
        this(value, path, Type.PACKAGE, added);
    }

    private PackageTreeItem(String value, String path, Type type, boolean added) {
        super(value, path, type, packageIcon);
        setChanged(added);
    }

    @Override
    public ContextMenu getMenu() {
        Menu parentMenu = new Menu("Dodaj");

        MenuItem addInbox1 = new MenuItem("Pakiet");
        addInbox1.setOnAction(event -> {
            String result = new InputDialog().getTextInputDialog("Dodaj pakiet", "Nazwa: ");
            if (result != null) {
                result = result.trim();
                String[] split = result.split("\\.");
                if (split.length == 0) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    return;
                }

                BaseTreeItem treeItem = this;

                for (String s : split) {
                    s = s.trim();
                    System.out.println(":" + s);
                    BaseTreeItem parentTreeItem = treeItem;

                    if (s.equals("")) {
                        new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                        return;
                    }

                    treeItem = treeItem.getChildrenWithValue(s, Type.PACKAGE);

                    if (treeItem == null) {
                        String pack = parentTreeItem.getRealName();
                        if (!parentTreeItem.getType().equals(Type.ROOT)) {
                            pack += ".";
                        }

                        treeItem = new PackageTreeItem(s, pack + s, true);
                        parentTreeItem.getChildren().add(0, treeItem);
                    }
                }

                System.out.println("Dodano pakiet " + result);
            }
        });

        MenuItem addInbox2 = new MenuItem("Klasę");
        addInbox2.setOnAction(event -> {
            addClass(false);
        });

        MenuItem addInbox3 = new MenuItem("Interface");
        addInbox3.setOnAction(event -> {
            addClass(true);
        });

        parentMenu.getItems().addAll(addInbox1, addInbox2, addInbox3);

        MenuItem addInbox4 = new MenuItem("Usuń");
        addInbox4.setDisable(!isChanged());
        addInbox4.setOnAction(event -> {
            deleteChildren();
            getParent().getChildren().remove(this);
        });

        return new ContextMenu(parentMenu, addInbox4);
    }

    private void addClass(boolean isInterface) {
        String result = new InputDialog().getTextInputDialog(isInterface ? "Dodaj interface" : "Dodaj klasę", "Nazwa: ");
        if (result != null) {
            result = result.trim();
            String[] split = result.split("\\.");
            if (split.length == 0) {
                new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                return;
            }

            BaseTreeItem treeItem = this;

            for (int i = 0; i < split.length; i++) {
                String s = split[i];

                Type prefType = i == split.length - 1 ? Type.CLASS : Type.PACKAGE;
                s = s.trim();
                System.out.println(":" + s);
                BaseTreeItem parentTreeItem = treeItem;

                if (s.equals("")) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    break;
                }

                treeItem = treeItem.getChildrenWithValue(s, prefType);

                if (treeItem == null) {
                    String pack = parentTreeItem.getRealName();
                    if (!parentTreeItem.getType().equals(Type.ROOT)) {
                        pack += ".";
                    }

                    if (prefType.equals(Type.PACKAGE)) {
                        treeItem = new PackageTreeItem(s, pack + s, true);
                    } else {
                        try {
                            CtClass ctClass = isInterface ? ClassPool.getDefault().makeInterface(pack + s)
                                    : ClassPool.getDefault().makeClass(pack + s);
                            ProgramData.getInstance().addClass(ctClass);
                            treeItem = new ClassTreeItem(ctClass, true);
                        } catch (RuntimeException r) {
                            new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                            return;
                        }
                    }
                    parentTreeItem.getChildren().add(0, treeItem);
                } else if (prefType.equals(Type.CLASS)) {
                    new ErrorDialog().showErrorDialog(BaseDialog.Action.CREATE);
                    return;
                }
            }

            System.out.println((isInterface ? "Dodano interface " : "Dodano klase ") + result);
        }
    }

    private void deleteChildren() {
        for (TreeItem<String> stringTreeItem : getChildren()) {
            BaseTreeItem baseTreeItem = (BaseTreeItem) stringTreeItem;
            System.out.println(baseTreeItem.getPane().getClass());
            if (baseTreeItem.getType().equals(Type.PACKAGE)) {
                ((PackageTreeItem) baseTreeItem).deleteChildren();
            }

            if (baseTreeItem.getType().equals(Type.CLASS)) {
                ProgramData.getInstance().getClasses().remove(((ClassTreeItem) baseTreeItem).getCtClass());
            }
        }
    }
}
