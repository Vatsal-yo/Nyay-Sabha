package nyaysabha;

import java.util.*;

public final class Json {

    private Json() {
    }

    public static String quote(String s) {
        if (s == null) {
            return "null";
        }
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':
                    b.append("\\\"");
                    break;
                case '\\':
                    b.append("\\\\");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                case '\r':
                    b.append("\\r");
                    break;
                case '\t':
                    b.append("\\t");
                    break;
                default:
                    if (c < 32) {
                        b.append(String.format("\\u%04x", (int) c));
                    } else {
                        b.append(c);
                    }
            }
        }
        return b.append('"').toString();
    }

    public static Object parse(String source) {
        return new Parser(source).parse();
    }

    @SuppressWarnings("unchecked")
    public static String stringAt(Object root, String... path) {
        Object value = root;
        for (String key : path) {
            if (!(value instanceof Map)) {
                return null;
            }
            value = ((Map<String, Object>) value).get(key);
        }
        return value instanceof String ? (String) value : null;
    }

    private static final class Parser {

        private final String s;
        private int i = 0;

        Parser(String s) {
            this.s = s;
        }

        Object parse() {
            Object v = value();
            ws();
            if (i < s.length()) {
                throw new IllegalArgumentException("Unexpected JSON data");
            
            }return v;
        }

        private Object value() {
            ws();
            if (i >= s.length()) {
                throw new IllegalArgumentException("Unexpected end of JSON");
            
            }char c = s.charAt(i);
            if (c == '{') {
                return object();
            
            }if (c == '[') {
                return array();
            
            }if (c == '\"') {
                return string();
            }
            if (c == 't') {
                word("true");
                return true;
            }
            if (c == 'f') {
                word("false");
                return false;
            }
            if (c == 'n') {
                word("null");
                return null;
            }
            int start = i;
            while (i < s.length() && "-+0123456789.eE".indexOf(s.charAt(i)) >= 0) {
                i++;
            
            }if (start == i) {
                throw new IllegalArgumentException("Invalid JSON value");
            }
            String n = s.substring(start, i);
            try {
                return n.contains(".") || n.contains("e") || n.contains("E") ? Double.valueOf(n) : Long.valueOf(n);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid JSON number");
            }
        }

        private Map<String, Object> object() {
            i++;
            Map<String, Object> m = new LinkedHashMap<>();
            ws();
            if (take('}')) {
                return m;
            
            }do {
                ws();
                String k = string();
                ws();
                need(':');
                m.put(k, value());
                ws();
                if (take('}')) {
                    return m;
                
                }need(',');
            } while (true);
        }

        private List<Object> array() {
            i++;
            List<Object> a = new ArrayList<>();
            ws();
            if (take(']')) {
                return a;
            
            }do {
                a.add(value());
                ws();
                if (take(']')) {
                    return a;
                
                }need(',');
            } while (true);
        }

        private String string() {
            need('\"');
            StringBuilder b = new StringBuilder();
            while (i < s.length()) {
                char c = s.charAt(i++);
                if (c == '\"') {
                    return b.toString();
                
                }if (c == '\\') {
                    if (i >= s.length()) {
                        break;
                    
                    }char e = s.charAt(i++);
                    switch (e) {
                        case '\"':
                            b.append('\"');
                            break;
                        case '\\':
                            b.append('\\');
                            break;
                        case '/':
                            b.append('/');
                            break;
                        case 'b':
                            b.append('\b');
                            break;
                        case 'f':
                            b.append('\f');
                            break;
                        case 'n':
                            b.append('\n');
                            break;
                        case 'r':
                            b.append('\r');
                            break;
                        case 't':
                            b.append('\t');
                            break;
                        case 'u':
                            if (i + 4 > s.length()) {
                                throw new IllegalArgumentException("Bad unicode escape");
                            
                            }b.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                            i += 4;
                            break;
                        default:
                            throw new IllegalArgumentException("Bad escape");
                    }
                } else {
                    b.append(c);
            
                }}
            throw new IllegalArgumentException("Unclosed JSON string");
        }

        private void word(String w) {
            if (!s.startsWith(w, i)) {
                throw new IllegalArgumentException("Invalid JSON token");
            
            }i += w.length();
        }

        private void ws() {
            while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
                i++;
        
            }}

        private boolean take(char c) {
            if (i < s.length() && s.charAt(i) == c) {
                i++;
                return true;
            }
            return false;
        }

        private void need(char c) {
            ws();
            if (!take(c)) {
                throw new IllegalArgumentException("Expected " + c);
        
            }}
    }

    public static String array(Collection<String> values) {
        StringJoiner j = new StringJoiner(",", "[", "]");
        for (String s : values) {
            j.add(quote(s));
        }
        return j.toString();
    }
}
