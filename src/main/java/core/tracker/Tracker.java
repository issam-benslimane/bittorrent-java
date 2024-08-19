package Tracker;

import Torrent.Torrent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Tracker {

    public Tracker() {}

    public TrackerResponse sendRequest(Torrent torrent){
        TrackerRequest request = new TrackerRequest(torrent);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(request.getUri());
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            return new TrackerResponse(httpResponse.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TrackerRequest {
        private String announce;
        private byte[] infoHash;
        private byte[] peerId;
        private int port;
        private int uploaded;
        private int downloaded;
        private long left;
        private int compact;

        public TrackerRequest(Torrent torrent) {
            announce = torrent.getAnnounce();
            infoHash = torrent.getInfo().getHash();
            peerId = "00112233445566778899".getBytes();
            port = 6881;
            uploaded = 0;
            downloaded = 0;
            left = torrent.getInfo().getLength();
            compact = 1;
        }

        public URI getUri(){
            StringBuilder uri = new StringBuilder(announce + '/');
            uri.append("?info_hash=").append(urlEncode(infoHash));
            uri.append("&peer_id=").append(urlEncode(peerId));
            uri.append("&port=").append(port);
            uri.append("&uploaded=").append(uploaded);
            uri.append("&downloaded=").append(downloaded);
            uri.append("&left=").append(left);
            uri.append("&compact=").append(compact);
            try{
                return new URI(uri.toString());
            } catch (URISyntaxException e){
                e.printStackTrace();
                return null;
            }
        }

        private String urlEncode(byte[] bytes){
            StringBuilder encoded = new StringBuilder();
            encoded.append("%");
            for (int i = 0; i < bytes.length; i++) {
                encoded.append(URLEncoder.encode(String.format("%02X", bytes[i]), StandardCharsets.UTF_8));
                if (i < bytes.length - 1) {
                    encoded.append("%");
                }
            }
            return encoded.toString();
        }
    }
}
