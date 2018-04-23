package pl.edu.wat.wcy.manager;

import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class JavassistFieldManager {
    private CtField field;

    public JavassistFieldManager(CtField field) {
        this.field = field;
    }

    public boolean remove() {
        try {
            CtClass declaringClass = field.getDeclaringClass();
            declaringClass.removeField(field);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }


}
