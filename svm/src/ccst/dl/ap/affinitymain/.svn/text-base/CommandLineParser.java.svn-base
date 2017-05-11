package affinitymain;

import java.util.Map;
import java.util.TreeMap;

public class CommandLineParser {

    public static Map<String, String> parse(String line) {

        String[] tokens = line.split("\\s+");

        return parseTokens(tokens);

    }

    public static Map<String, String> parseTokens(String[] tokens) {
        Map<String, String> ret = new TreeMap<String, String>();

        for (String token : tokens) {
            System.out.println("TOKEN: " + token);
            String[] attr = token.split("[=]");
            if (attr.length == 2) {
                ret.put(attr[0], attr[1]);
            }
        }
        return ret;

    }
}
