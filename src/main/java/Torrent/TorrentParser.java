package Torrent;

import Bencode.Bdecoder;
import Bencode.Bencoder;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

public class TorrentParser {
    private Map<String, ?> obj;
    private Torrent torrent = new Torrent();
    public TorrentParser(InputStream in) {
        Bdecoder bdecoder = new Bdecoder(in, true);
        this.obj = (Map<String, ?>) bdecoder.getDecoded();
        torrent = parse();
    }

    public Torrent getTorrent() {
        return torrent;
    }

    public Torrent parse(){
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
        Map<String, ?> infoMap = (Map<String, ?>) obj.get("info");
        Bencoder bencoder = new Bencoder(infoMap);
        info.setName(new String((byte[]) infoMap.get("name")));
        info.setLength((long) infoMap.get("length"));
        info.setHash(sha1(bencoder.getEncoded()));
        info.setPieces(parsePieces());
        info.setPieceLength(((Long) infoMap.get("pieceLength")).intValue());
        return info;
    }

    private byte[][] parsePieces(){
        Map<String, ?> infoMap = (Map<String, ?>) obj.get("info");
        byte[] bytes = (byte[]) infoMap.get("pieces");
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

    private byte[] sha1(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
