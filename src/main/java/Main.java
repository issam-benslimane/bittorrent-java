import Bencode.Bdecoder;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    // System.out.println("Logs from your program will appear here!");
    String command = args[0];
    if("decode".equals(command)) {
      //  Uncomment this block to pass the first stage
        String bencodedValue = args[1];
        Object decoded;
        try {
            Bdecoder b = new Bdecoder(bencodedValue.getBytes());
            decoded = b.getDecoded();
        } catch(RuntimeException e) {
          System.out.println(e.getMessage());
          return;
        }
        System.out.println(gson.toJson(decoded));

    } else if (command.equals("info")) {
        File file = new File(args[1]);
        Object decoded = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            Bdecoder b = new Bdecoder(fis, true);
            decoded = b.getDecoded();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(gson.toJson(decoded));
    } else {
      System.out.println("Unknown command: " + command);
    }

  }
  
}
