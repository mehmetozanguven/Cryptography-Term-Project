package file_handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class FileToByteArrayConverter {
    private static int BYTE_LENGTH= 4;

    public static byte[] readFileToByteArray(String filePath){
        return getBytes(filePath);
    }


    public static byte[][] readFileTo8BytesArrays(String filePath){

        byte[] bytesOfFile = getBytes(filePath);
        int numberOfBytesArray = bytesOfFile.length / BYTE_LENGTH;
        int numberOfRedundantBytes = bytesOfFile.length % BYTE_LENGTH;

        int totalNumberOf8Bytes = numberOfRedundantBytes > 0 ? numberOfBytesArray + 1 : numberOfBytesArray;

        byte[][] numberOf8Bytes = new byte[totalNumberOf8Bytes][BYTE_LENGTH];

        int bytesCounter = 0;
        int outerArrayCounter = 0;
        while (outerArrayCounter < totalNumberOf8Bytes){

            for (int i = 0; i < BYTE_LENGTH; i++){
                if (bytesCounter < bytesOfFile.length){
                    numberOf8Bytes[outerArrayCounter][i] = bytesOfFile[bytesCounter];
                }
                bytesCounter ++;
            }

            outerArrayCounter ++;
        }

        return numberOf8Bytes;
    }


    private static byte[] getBytes(String filePath) {
        File file = new File(filePath);
        byte[] bytes = null;

        try {
            bytes = (Files.readAllBytes(file.toPath()));
            System.out.println("FileToByArray bytes size: " + bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
