package pl.edu.wat.wcy.data;

import javassist.ClassPath;
import javassist.CtClass;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ProgramData {

    private static ProgramData programData;

    public static ProgramData getInstance() {
        if (programData == null) {
            programData = new ProgramData();
        }
        return programData;
    }

    public static ProgramData getNewInstance() {
        programData = new ProgramData();
        return programData;
    }

    private String jarFileName;
    private List<CtClass> classes;
    private ClassPath classPath;

    private ProgramData() {
        classes = new LinkedList<>();
    }

    public void addClass(CtClass ctClass) {
        ctClass.defrost();
        classes.add(ctClass);
    }
}
