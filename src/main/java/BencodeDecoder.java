public class BencodeDecoder {
    private String encodedValue;
    private int current = 0;

    public BencodeDecoder(String encodedValue) {
        this.encodedValue = encodedValue;
    }

    public String decode(){
        return decodeString();
    }

    private String decodeString(){
        if (!Character.isDigit(encodedValue.charAt(current))) return null;
        int length = encodedValue.charAt(current) - '0';
        int start = current+2, end = start+length;
        current = end;
        return encodedValue.substring(start, end);
    }
}

