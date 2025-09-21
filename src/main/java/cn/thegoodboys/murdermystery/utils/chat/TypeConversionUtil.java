package cn.thegoodboys.murdermystery.utils.chat;

import java.util.List;
import java.util.StringJoiner;

public class TypeConversionUtil {

    public static String StringList(List<String> strings) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (String s1 : strings) {
            stringJoiner.add(s1);
        }
        return stringJoiner.toString();
    }

    public static String StringList(String... strings) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (String s1 : strings) {
            stringJoiner.add(s1);
        }
        return stringJoiner.toString();
    }

}
