package pl.edu.wat.wcy.jar;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import pl.edu.wat.wcy.data.ProgramData;
import pl.edu.wat.wcy.dialogs.ErrorDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarHandler {

    public boolean replaceJarFile(ProgramData programData, Map<String, CtClass> map) throws IOException {
        String jarPathAndName = programData.getJarFileName();

        File jarFile = new File(jarPathAndName);
        File tempJarFile = new File(jarPathAndName + ".tmp");
        JarFile jar = new JarFile(jarFile);
        boolean jarWasUpdated = false;

        try {
            JarOutputStream tempJar = new JarOutputStream(new FileOutputStream(tempJarFile));
            byte[] buffer = new byte[1024];
            int bytesRead;

            try {
                map.forEach((s, ctClass) -> {
                    try {
                        JarEntry entry = new JarEntry(s);
                        tempJar.putNextEntry(entry);
                        tempJar.write(ctClass.toBytecode());
                    } catch (IOException | CannotCompileException ex) {
                        System.err.println(ex);
                    }
                });
            } catch (Exception ex) {
                System.out.println("ex");
                tempJar.putNextEntry(new JarEntry("stub"));
            }

            try {
                InputStream entryStream = null;
                for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    if (!map.containsKey(entry.getName())) {
                        entryStream = jar.getInputStream(entry);
                        tempJar.putNextEntry(entry);
                        while ((bytesRead = entryStream.read(buffer)) != -1) {
                            tempJar.write(buffer, 0, bytesRead);
                        }
                    }
                }

                if (entryStream != null) entryStream.close();
                jarWasUpdated = true;
            } catch (Exception ex) {
                System.err.println(ex);
                tempJar.putNextEntry(new JarEntry("stub"));
            } finally {
                tempJar.close();
            }
        } finally {
            jar.close();

            ClassPool.getDefault().removeClassPath(programData.getClassPath());
            programData.setClassPath(null);
        }

        if (jarWasUpdated && jarFile.delete()) {
            System.out.println("jarWasUpdated == true");
            tempJarFile.renameTo(jarFile);
            System.out.println(jarPathAndName + " updated.");
            return true;

        } else {
            System.out.println("Could Not Delete JAR File");
            tempJarFile.delete();

            new ErrorDialog().showErrorDialog("Wystąpił błąd", "Nie można zastąpić pliku.");
        }

        return false;
    }

    public boolean save(ProgramData programData) {
        Map<String, CtClass> map = new HashMap<>();

        programData.getClasses().forEach(x -> {
            map.put(x.getName().replaceAll("\\.", "/") + ".class", x);
        });

        try {
            return replaceJarFile(programData, map);
        } catch (IOException e) {
            new ErrorDialog().showErrorDialog("Nie można utworzyć pliku.");
        }

        return false;
    }
}