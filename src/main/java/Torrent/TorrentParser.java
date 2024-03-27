package Torrent;

import Bencode.Bdecoder;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

    private URL parseAnnounce(){
        try{
            String announce = new String((byte[]) obj.get("announce"));
            return new URI(announce).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Torrent.Info parseInfo(){
        var info = new Torrent.Info();
        Map<String, ?> infoMap = (Map<String, ?>) obj.get("info");
        info.setName(new String((byte[]) infoMap.get("name")));
        info.setLength((long) infoMap.get("length"));
        info.setPieceLength((long) infoMap.get("pieceLength"));
        return info;
    }
}
