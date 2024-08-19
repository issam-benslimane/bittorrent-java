package Torrent;

import org.apache.commons.codec.binary.Hex;

public class  Info{
    private String name;
    private long length;
    private int pieceLength;
    private byte[][] pieces;
    private byte[] hash;

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[][] getPieces() {
        return pieces;
    }

    public void setPieces(byte[][] pieces) {
        this.pieces = pieces;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    public void setPieceLength(int pieceLength) {
        this.pieceLength = pieceLength;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Length: ").append(length).append("\n");
        sb.append("Info Hash: ").append(new String(Hex.encodeHex(hash))).append("\n");
        sb.append("Piece Length: ").append(pieceLength).append("\n");
        sb.append("Piece Hashes:");
        for (byte[] piece: pieces){
            sb.append("\n").append(Hex.encodeHex(piece));
        }
        return sb.toString();
    }
}
