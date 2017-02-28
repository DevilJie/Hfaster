package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.annotation.PrimaryKey;
import hsj.czy.mybatisframe.annotation.Table;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
import hsj.czy.mybatisframe.listener.ChBatisFrameConfigListener;
import hsj.czy.mybatisframe.util.CamelCaseUtils;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TableInit {

    public static void initTable(String[] packages) {
        Table t = null;
        String tname = null;
        Method[] methods = null;
        boolean primaryKeyExits = false;
        for (String string : packages) {
            List<Class> clist = getClasssFromPackage(string);
            for (Class c : clist) {
                primaryKeyExits = false;
                t = (Table) c.getAnnotation(Table.class);
                if (t != null) {
                    tname = t.table_name();
                    if (tname == null || tname.equals("")) {
                        tname = CamelCaseUtils.processNameWithSpliter(c.getSimpleName());
                    }
                    ChBatisFrameConfigListener.entity_table.put(c.getName(), tname);

                    methods = c.getMethods();
                    PrimaryKey key;
                    int i = 0;
                    for (Method method : methods) {
                        key = (PrimaryKey) method.getAnnotation(PrimaryKey.class);
                        if (key != null) {
                            primaryKeyExits = true;
                            break;
                        }
                    }
                    if (!primaryKeyExits) throw new MyBatistFrameServiceException(
                                                                                  "Entity "
                                                                                          + c.getName()
                                                                                          + " not have Primary Key , Please check!");
                }
            }
        }
    }

    /**
     * ð е class
     * 
     * @param pack package
     * @return List class ʵ
     */
    public static List<Class> getClasssFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<Class>();

        // Ƿ ѭ Ӱ
        boolean recursive = true;

        //
        String packageName = pack;
        // Ӧ ·
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    clazzs = getClassNameByJar(url.getPath(), true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 从jar获取某包下所有类
     * 
     * @param jarPath jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<Class> getClassNameByJar(String jarPath, boolean childPackage) {
        List<Class> myClassName = new ArrayList<Class>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(Class.forName(entryName));
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(Class.forName(entryName));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * package Ӧ · ҵ е class
     * 
     * @param packageName package
     * @param filePath package Ӧ ·
     * @param recursive Ƿ package
     * @param clazzs ҵ class Ժ ŵļ
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
                                                List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// dirĿ¼
                boolean acceptClass = file.getName().endsWith("class");// class ļ
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
