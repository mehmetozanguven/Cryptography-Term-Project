package file_handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileToStringConverter {

    private FileToStringConverter(){}

    /**
     * Read the file and convert to the string
     * @param filePath is the path for the file
     * @return file content as string
     */
    public static String getFileContentAsString(String filePath){
        String contents = "";
        try {
            contents = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
