package pl.edu.wat.wcy;

import javafx.scene.image.Image;

import java.io.InputStream;

public class ResourceLoader {
    private static final ClassLoader classLoader = ResourceLoader.class.getClassLoader();

    public static InputStream getFxml(String name) {
        return classLoader.getResourceAsStream(name);
    }

    public static Image getImage(String name) {
        return new Image(classLoader.getResourceAsStream(name));
    }

    private ResourceLoader() {
    }
}