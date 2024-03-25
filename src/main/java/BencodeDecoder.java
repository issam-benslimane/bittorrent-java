public class BencodeDecoder {
    private String encodedValue;
    private int current = 0;

    public BencodeDecoder(String encodedValue) {
        this.encodedValue = encodedValue;
    }

    public Object decode(){
        if (Character.isDigit(encodedValue.charAt(current))) return decodeString();
        if (encodedValue.charAt(current) == 'i') return decodeInteger();
        return null;
    }

    private String decodeString(){
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

    private Long decodeInteger(){
        int start = current+1, end = 0;
        for (int i = start; i < encodedValue.length(); i++) {
            if (encodedValue.charAt(i) == 'e') {
                end = i;
                break;
            }
        }
        current = end;
        return Long.parseLong(encodedValue.substring(start, end));
    }
}

