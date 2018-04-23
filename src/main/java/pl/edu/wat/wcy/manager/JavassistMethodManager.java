package pl.edu.wat.wcy.manager;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavassistMethodManager {
    private CtMethod method;

    public JavassistMethodManager(CtMethod method) {
        this.method = method;
    }

    public boolean insertBefore(String source) {
        try {
            method.insertBefore(source);
            return true;
        } catch (CannotCompileException e) {
            return false;
        }
    }

    public boolean insertAfter(String source) {
        try {
            method.insertAfter(source);
            return true;
        } catch (CannotCompileException e) {
            return false;
        }
    }

    public boolean setBody(String source) {
        try {
            method.setBody(source);
            return true;
        } catch (CannotCompileException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove() {
        try {
            CtClass declaringClass = method.getDeclaringClass();
            declaringClass.removeMethod(method);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}
