package dbtools.common.file;

import dbtools.common.utils.StrUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2017/10/1.
 */
public class FileReader {
    private String filePath;
    private BufferedReader reader;

    public FileReader(String filePath) {
        if (StrUtils.isEmpty(filePath)) {
            filePath = "fr";
        }
        this.filePath = filePath;
    }

    public boolean isOpen() {
        return reader != null;
    }

    public boolean open() {
        if (isOpen()) {
            close();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.printf("File doesn't exist: %s\n", filePath);
            return false;
        }

        try {
            reader = new BufferedReader(new java.io.FileReader(file));
        } catch (IOException e) {
            reader = null;
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        boolean isOpen = isOpen();
        System.out.printf("FileReader open %s: %s\n", isOpen ? "successfully" : "Failed", filePath);
        return isOpen;
    }

    public void close() {
        if (!isOpen()) {
            return;
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            reader = null;
        }
        System.out.printf("FileReader close successfully: %s\n", filePath);
    }

    public String readLine() {
        if (!isOpen()) {
            System.out.println("Please call open() firstly.");
            return null;
        }

        String str = null;
        try {
            str = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}