package pl.edu.wat.wcy.jar;

import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import pl.edu.wat.wcy.data.ProgramData;
import pl.edu.wat.wcy.tree.ClassTreeItem;
import pl.edu.wat.wcy.tree.ConstructorTreeItem;
import pl.edu.wat.wcy.tree.FieldTreeItem;
import pl.edu.wat.wcy.tree.MethodTreeItem;
import pl.edu.wat.wcy.tree.PackageTreeItem;
import pl.edu.wat.wcy.tree.base.BaseTreeItem;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileLoader {

    public static void getAllElementsInClass(CtClass clazz, BaseTreeItem insideHelper) {
        for (CtField field : clazz.getDeclaredFields()) {
            insideHelper.getChildren().add(new FieldTreeItem(field));
        }

        for (CtMethod method : clazz.getDeclaredMethods()) {
            insideHelper.getChildren().add(new MethodTreeItem(method));
        }

        for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
            insideHelper.getChildren().add(new ConstructorTreeItem(constructor));
        }
    }

    public static BaseTreeItem getClassesList(String pathToJar) throws IOException, ClassNotFoundException, NotFoundException {
        ProgramData programData = ProgramData.getNewInstance();
        programData.setJarFileName(pathToJar);

        JarFile jarFile = new JarFile(pathToJar);
        ClassPool pool = ClassPool.getDefault();
        ClassPath classPath = pool.insertClassPath(pathToJar);
        programData.setClassPath(classPath);
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        PackageTreeItem baseTreeItem = new PackageTreeItem(jarFile.getName(), "", BaseTreeItem.Type.ROOT);

        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry je = jarEntryEnumeration.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }

            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            String[] path = className.split("\\.");

            BaseTreeItem helper = baseTreeItem;
            for (int i = 0; i < path.length; i++) {
                BaseTreeItem.Type type = i == path.length - 1 ? BaseTreeItem.Type.CLASS : BaseTreeItem.Type.PACKAGE;

                BaseTreeItem insideHelper = helper.getChildrenWithValue(path[i], type);
                if (insideHelper == null) {

                    if (type.equals(BaseTreeItem.Type.PACKAGE)) {
                        insideHelper = new PackageTreeItem(path, i);
                        helper.getChildren().add(insideHelper);
                    } else if (type == BaseTreeItem.Type.CLASS) {
                        CtClass ctClass = pool.get(className);
                        programData.addClass(ctClass);
                        insideHelper = new ClassTreeItem(ctClass);
                        helper.getChildren().add(insideHelper);

                        getAllElementsInClass(ctClass, insideHelper);
                    }
                }
                helper = insideHelper;
            }
        }

        jarFile.close();

        return baseTreeItem;
    }
}