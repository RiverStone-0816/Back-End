package kr.co.eicn.ippbx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 * @since 2017-05-14
 */
public class ClassUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    @SuppressWarnings("unchecked")
    public static <E> List<Class<E>> getClasses(String packageName, Class<E> type) throws IOException, ClassNotFoundException, URISyntaxException {
        return getClasses(packageName).stream()
                .filter(klass -> klass.equals(type)
                        || type.isAnnotation()
                        || (type.isAnnotation() && klass.getAnnotation((Class) type) != null)
                        || Arrays.asList(klass.getClasses()).contains(type)
                        || Arrays.asList(klass.getInterfaces()).contains(type)
                        || isExpands(klass, type))
                .map(e -> (Class<E>) e)
                .distinct()
                .collect(Collectors.toList());
    }

    public static boolean isExpands(Object child, Class<?> parent) {
        return isExpands(child.getClass(), parent);
    }

    public static boolean isExpands(Class<?> child, Class<?> parent) {
        if (child == null || parent == null)
            return false;

        if (child.isInterface() && !parent.isInterface())
            return false;

        if (parent.isInterface() && ofInterface(child, parent))
            return true;

        if (child.equals(parent))
            return true;

        return isExpands(child.getSuperclass(), parent);
    }

    private static boolean ofInterface(Class<?> child, Class<?> parent) {
        if (child == null || parent == null)
            return false;

        if (Objects.equals(child, parent))
            return true;

        final List<Class<?>> childInterfaces = Arrays.asList(child.getInterfaces());

        if (childInterfaces.contains(parent))
            return true;

        if (ofInterface(child.getSuperclass(), parent))
            return true;

        for (Class<?> childInterface : childInterfaces) {
            if (ofInterface(childInterface.getSuperclass(), parent))
                return true;

            for (Class<?> anInterface : childInterface.getInterfaces())
                if (ofInterface(anInterface, parent))
                    return true;
        }

        return false;
    }

    public static List<Class<?>> getClasses() throws IOException, URISyntaxException {
        return getClasses("");
    }

    /**
     * from: http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection<br/>
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        dirs = dirs.stream().distinct().collect(Collectors.toList());
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            logger.error(directory.getAbsolutePath());
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * from: http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection<br/>
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws IOException, URISyntaxException {
        if (directory.isDirectory())
            return findClassesFromDirectory(directory, packageName);

        if (directory.getAbsolutePath().replaceAll("\\\\", "/").contains(".jar!/"))
            return findClassesFromJar(directory, packageName);

        return new ArrayList<>();
    }

    private static List<Class<?>> findClassesFromJar(File jarFile, String packageName) throws IOException, URISyntaxException {
        final List<Class<?>> classes = new ArrayList<>();
        final String filePath = jarFile.toString().replaceAll("\\\\", "/");
        if (!filePath.contains(".jar!/"))
            return classes;

        final List<Path> paths = new ArrayList<>();
        FileSystems.newFileSystem(Paths.get(
                        filePath.substring(
                                filePath.lastIndexOf(":") + ":".length() + ((System.getProperty("os.name").toLowerCase().contains("win") ? -2 : 0)),
                                filePath.lastIndexOf(".jar!/") + 4)
                ), Thread.currentThread().getContextClassLoader())
                .getRootDirectories()
                .forEach(root -> {
                    try {
                        Files.walk(root).forEach(paths::add);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        for (Path path : paths) {
            final String className = path.toString().substring(1).replaceAll("[\\\\/]", ".");
            if (className.endsWith(".class") && className.startsWith(packageName + ".")) {
                final String substring = className.substring(0, className.length() - 6);
                try {
                    classes.add(Class.forName(substring));
                } catch (Exception e) {
                    logger.error(packageName);
                    logger.error(substring);
                    logger.error("Exception!", e);
                }
            }
        }

        return classes;
    }

    private static List<Class<?>> findClassesFromDirectory(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.isDirectory())
            return classes;

        File[] files = directory.listFiles();
        if (files == null)
            return classes;

        for (File file : files) {
            final String targetPackageName = !packageName.isEmpty() ? packageName + '.' : "";
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesFromDirectory(file, targetPackageName + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                final String fileTarget = targetPackageName + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(fileTarget));
                } catch (Exception e) {
                    logger.error(packageName);
                    logger.error(fileTarget);
                    logger.error("Exception!", e);
                }
            }
        }
        return classes;
    }

}
