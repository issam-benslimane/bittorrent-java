package Torrent;

import java.io.File;

public class Torrent {
    private String announce;
    private Info info;

    public static Torrent read(File torrentFile){
        TorrentParser parser = new TorrentParser(torrentFile);
        return parser.getTorrent();
    }

    public Torrent() {
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Tracker Url: " + announce + "\n" + info;
    }
}


