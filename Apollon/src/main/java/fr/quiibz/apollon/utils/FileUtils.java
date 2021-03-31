package fr.quiibz.apollon.utils;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class FileUtils {

    public static void createFile(File file) throws IOException {

        if(!file.exists()) {

            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public static void saveFile(File file, String text) {

        try {

            createFile(file);

            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.flush();
            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static String loadFile(File file) {

        if(file.exists()) {

            try {

                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null)
                    stringBuilder.append(line);

                reader.close();

                return stringBuilder.toString();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return "";
    }
}
