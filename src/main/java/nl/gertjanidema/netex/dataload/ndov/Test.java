package nl.gertjanidema.netex.dataload.ndov;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
//    private static Pattern pattern = Pattern.compile("(NeTEx_)?(.+?)_(20\\d{6})(.+)");
    private static Pattern pattern = Pattern.compile("NeTEx_(.+?)_(\\d{8})_(\\d{8})_(\\d{4}).+");
    private static String value = "NeTEx_ARR_NL_20250805_20250806_1404.xml.gz";
    
    public static void main(String[] args) {
        Matcher m = pattern.matcher(value);
        if (m.matches()) {
            var matchResult = m.toMatchResult();
            for (var i = 0; i <= matchResult.groupCount(); i++) {
                System.out.println(matchResult.group(i));
            }
        }
        System.out.println(m.toString());
    }
}
