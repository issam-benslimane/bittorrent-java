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
        int delimeterIndex = 0;
        for (int i = current; i < encodedValue.length(); i++) {
            if (encodedValue.charAt(i) == ':') {
                delimeterIndex = i;
                break;
            }
        }
        int length = Integer.parseInt(encodedValue.substring(current, delimeterIndex));
        int start = delimeterIndex+1, end = start + length;
        current = end;
        return encodedValue.substring(start, end);
    }
}

