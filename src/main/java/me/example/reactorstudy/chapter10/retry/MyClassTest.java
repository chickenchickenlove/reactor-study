package me.example.reactorstudy.chapter10.retry;

import java.util.List;

import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;

public class MyClassTest {

    public static void test() {
        StringBuilder errorMsgBuilder = null;
        List<String> lists = List.of("a", "b");
        String httpElementName = "Hello";

        for (String s : lists) {
            if (errorMsgBuilder == null) {
                errorMsgBuilder = new StringBuilder(
                        String.format("No matching value for '%s' in the attributes: [", httpElementName));
                errorMsgBuilder.append(String.format("{key=%s, value=%s}", s, s));
            } else {
                errorMsgBuilder.append(", ");
                errorMsgBuilder.append(String.format("{key=%s, value=%s}", s, s));
            }
        }

        if (errorMsgBuilder != null && !StringUtil.isNullOrEmpty(errorMsgBuilder.toString())) {
            errorMsgBuilder.append("].");
            throw new IllegalStateException(errorMsgBuilder.toString());
        }
    }


    public static void main(String[] args) {
        test();




    }
}
