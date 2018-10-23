package cn.yejh.mobilelearning.util;

public class StringUtils {
    private StringUtils() {
        throw new AssertionError();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
