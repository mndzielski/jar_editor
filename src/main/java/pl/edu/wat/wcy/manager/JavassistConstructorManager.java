package pl.edu.wat.wcy.manager;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class JavassistConstructorManager {
    private CtConstructor constructor;

    public JavassistConstructorManager(CtConstructor constructor) {
        this.constructor = constructor;
    }

    public boolean setBody(String source) {
        try {
            constructor.setBody(source);
            return true;
        } catch (CannotCompileException e) {
            return false;
        }
    }

    public boolean remove() {
        try {
            CtClass declaringClass = constructor.getDeclaringClass();
            declaringClass.removeConstructor(constructor);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
