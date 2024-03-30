import Bencode.Bdecoder;
import Torrent.Torrent;
import Torrent.TorrentParser;
import Tracker.Tracker;
import Tracker.TrackerRequest;
import Tracker.TrackerResponse;
import Tracker.Peer;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

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
        Torrent torrent = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            TorrentParser parser = new TorrentParser(fis);
            torrent = parser.getTorrent();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (torrent != null){
            System.out.println("Tracker URL: " + torrent.getAnnounce());
            System.out.println("Length: " + torrent.getInfo().getLength());
            System.out.println("Info Hash: " + new String(Hex.encodeHex(torrent.getInfo().getHash())));
            System.out.println("Piece Length: " + torrent.getInfo().getPieceLength());
            System.out.println("Piece Hashes:");
            for (byte[] piece: torrent.getInfo().getPieces()){
                System.out.println(Hex.encodeHex(piece));
            }
        }
    } else if (command.equals("peers")) {
        File file = new File(args[1]);
        Torrent torrent = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            TorrentParser parser = new TorrentParser(fis);
            torrent = parser.getTorrent();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assert torrent != null;
        TrackerRequest req = new TrackerRequest(torrent);
        Tracker tracker = new Tracker(req);
        TrackerResponse response = tracker.getResponse();
        for (Peer p: response.getPeers()){
            System.out.println(p);
        }
    } else {
      System.out.println("Unknown command: " + command);
    }

  }
  
}
