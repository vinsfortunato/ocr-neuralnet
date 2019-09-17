package net.flood.ocrnn.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * @author flood2d
 */
public class FileUtils {
    public static String getFileExtension(File file) {
        String parts[] = file.getName().split("\\.");
        return parts.length == 0 ? null : parts[parts.length - 1];
    }

    public static File getFileWithOtherExtension(File file, String extension) {
        String parts[] = file.getName().split("\\.");
        String fileName = "";
        for(int i = 0; i < parts.length - 1; i++) {
            fileName += parts[i];
        }
        fileName += "." + extension;
        return new File(file.getParentFile(), fileName);
    }

    public static File chooseFile(Component parent, String description, String ... extensions) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnVal = fileChooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
