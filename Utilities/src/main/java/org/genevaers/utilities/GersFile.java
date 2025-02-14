package org.genevaers.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.StreamHandler;

import com.google.common.flogger.FluentLogger;

public class GersFile {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private String name;

    public Writer getWriter(String name) throws IOException {
        if (GersConfigration.isZos()) {
            try {
                Class<?> rrc = Class.forName("org.genevaers.utilities.ZosGersFile");
                Constructor<?>[] constructors = rrc.getConstructors();
                return ((GersFile) constructors[0].newInstance()).getWriter(name);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IOException | ClassNotFoundException e) {
                logger.atSevere().log("getWriter failed %s", e.getMessage());
            }
            return null;
        } else {
            return new FileWriter(name);
        }
    }

    public Reader getReader(String name) throws FileNotFoundException {
        if (GersConfigration.isZos()) {
            try {
                Class<?> rrc = Class.forName("org.genevaers.utilities.ZosGersFile");
                Constructor<?>[] constructors = rrc.getConstructors();
                return ((GersFile) constructors[0].newInstance()).getReader(name);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IOException | ClassNotFoundException e) {
                logger.atSevere().log("getReader failed %s", e.getMessage());
            }
            return null;
        } else {
            File f = Paths.get(name).toFile();
            if(f.exists()) {
                return new FileReader(Paths.get(name).toFile());
            } else {
                return null;
            }

        }
    }

    public boolean exists(String name) {
        if (GersConfigration.isZos()) {
            try {
                Class<?> rrc = Class.forName("org.genevaers.utilities.ZosGersFile");
                Constructor<?>[] constructors = rrc.getConstructors();
                return ((GersFile) constructors[0].newInstance()).exists(name);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | ClassNotFoundException e) {
                logger.atSevere().log("exists failed %s", e.getMessage());
            }
            return false;
        } else {
            return Files.exists(Paths.get(name));
        }
    }

    public StreamHandler getFileHandler(String name) throws SecurityException, IOException {
        if (GersConfigration.isZos()) {
            try {
                Class<?> rrc = Class.forName("org.genevaers.utilities.ZosGersFile");
                Constructor<?>[] constructors = rrc.getConstructors();
                return ((GersFile) constructors[0].newInstance()).getFileHandler("//DD:" + name.toUpperCase());
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | ClassNotFoundException e) {
                logger.atSevere().log("getFileHandler failed %s: %s", e.getMessage(), e.getCause());
            }
            return null;
        } else {
            return new FileHandler(name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
