package Torrent;

import Bencode.Bdecoder;
import Bencode.Bencoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

class TorrentParser {
    private Map<String, Object> obj;
    private Torrent torrent = new Torrent();
    public TorrentParser(File torrentFile) {
        try (FileInputStream fis = new FileInputStream(torrentFile)) {
            Bdecoder bdecoder = new Bdecoder(fis);
            this.obj = (Map<String, Object>) bdecoder.getDecoded();
            torrent = parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Torrent getTorrent() {
        return torrent;
    }

    private Torrent parse(){
        Torrent torrent = new Torrent();
        torrent.setAnnounce(parseAnnounce());
        torrent.setInfo(parseInfo());
        return torrent;
    }

    private String parseAnnounce(){
         return new String((byte[]) obj.get("announce"));
    }

    private Info parseInfo(){
        Info info = new Info();
        var infoMap = (Map<String, Object>) obj.get("info");
        Bencoder bencoder = new Bencoder(infoMap);
        info.setName(new String((byte[]) infoMap.get("name")));
        info.setLength(((Number) infoMap.get("length")).longValue());
        info.setHash(parseInfoHash(infoMap));
        info.setPieces(parsePieces((byte[]) infoMap.get("pieces")));
        info.setPieceLength((int) infoMap.get("pieceLength"));
        return info;
    }

    private byte[] parseInfoHash(Map<String, Object> infoMap) {
        Bencoder bencoder = new Bencoder(infoMap);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(bencoder.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[][] parsePieces(byte[] bytes){
        int division = 20;
        int len = bytes.length/division;
        byte[][] pieces = new byte[len][];
        for (int i = 0; i < len; i++) {
            int from = i * division;
            int to = from + division;
            byte[] piece = Arrays.copyOfRange(bytes, from, to);
            pieces[i] = piece;
        }
        return pieces;
    }
}
