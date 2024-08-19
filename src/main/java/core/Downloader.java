package core;

import core.torrent.Torrent;
import core.tracker.Tracker;
import core.tracker.TrackerResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PieceDownloader {
    private Torrent torrent;
    private Peer[] peers;

    public PieceDownloader(File torrentFile) {
        this.torrent = Torrent.read(torrentFile);
        Tracker tracker = new Tracker();
        TrackerResponse response = tracker.sendRequest(torrent);
        this.peers = response.getPeers();
    }

    private void connect(Peer peer){
        try {
            ServerSocket serverSocket = new ServerSocket(peer.getPort());
            Socket clientSocket = serverSocket.accept();
            InputStream inputStream = clientSocket.getInputStream();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Handshack{
        private byte[] infoHash;
        private byte[] peerId;
        private String ptsr = "BitTorrent protocol";
        private int ptsrLen = ptsr.length();
        private byte[] reserved = {0, 0, 0, 0, 0, 0, 0, 0};

        public Handshack(byte[] infoHash, byte[] peerId) {
            this.infoHash = infoHash;
            this.peerId = peerId;
        }

        public byte[] handshack(){
            byte[] bytes = new byte[len()];
            bytes[0] = (byte) ptsrLen;
            int offset = 1;
            offset += merge(bytes, ptsr.getBytes(), offset);
            offset += merge(bytes, reserved, offset);
            offset += merge(bytes, infoHash, offset);
            merge(bytes, peerId,  offset);
            return bytes;
        }

        private int merge(byte[] a, byte[] b, int offset){
            for (int i = offset; i < offset + b.length; i++) {
                a[i] = b[i - offset];
            }
            return b.length;
        }

        private int len(){
            return infoHash.length + peerId.length + ptsrLen + reserved.length + 1;
        }
    }
}
