package pl.edu.wat.wcy.manager;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import lombok.Data;

@Data
public class JavassistClassManager {
    private static ClassPool classPool = ClassPool.getDefault();
    private CtClass ctClass;

    public JavassistClassManager(CtClass ctClass) {
        this.ctClass = ctClass;
        this.ctClass.defrost();
    }

    public CtMethod createMethod(String source) {
        try {
            CtMethod method = CtNewMethod.make(source, ctClass);
            ctClass.addMethod(method);
            return method;
        } catch (CannotCompileException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CtField createField(String source) {
        try {
            CtField field = CtField.make(source, ctClass);
            ctClass.addField(field);
            return field;
        } catch (CannotCompileException e) {
            e.printStackTrace();

            return null;
        }
    }

    public CtConstructor createCtConstructor(String source) {
        try {
            CtConstructor constructor = CtNewConstructor.make(source, ctClass);
            ctClass.addConstructor(constructor);
            return constructor;
        } catch (CannotCompileException e) {
            return null;
        }
    }
}
