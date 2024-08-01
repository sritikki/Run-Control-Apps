package org.genevaers.utilities;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.google.common.flogger.FluentLogger;

public class GersFilesUtils {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	static Collection<GersFile> gersFiles = new ArrayList<>();

	public Collection<GersFile> getGersFiles(String dir) {
		gersFiles.clear();
		if (GersConfigration.isZos()) {
            try {
                Class<?> rrc = Class.forName("org.genevaers.utilities.ZosGersFilesUtils");
                Constructor<?>[] constructors = rrc.getConstructors();
                return ((GersFilesUtils) constructors[0].newInstance()).getGersFiles(dir);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | ClassNotFoundException e) {
                logger.atSevere().log("getGersFiles failed %s", e.getMessage());
            }
            return null;

		} else {
			Iterator<File> it = null;
			Path xmlPath = Paths.get(dir);
			if (xmlPath.toFile().exists()) {
				WildcardFileFilter fileFilter = new WildcardFileFilter("*.xml");
				Collection<File> xmlFiles = FileUtils.listFiles(xmlPath.toFile(), fileFilter, TrueFileFilter.TRUE);
				for (File d : xmlFiles) {
					GersFile gf = new GersFile();
					gf.setName(d.getAbsolutePath());
					gersFiles.add(gf);
				}
			} else {
				logger.atSevere().log("WBXML file %s not found", xmlPath.toString());
			}
		}
		return gersFiles;
	}

	public static void clear() {
		gersFiles.clear();;
	}

}
