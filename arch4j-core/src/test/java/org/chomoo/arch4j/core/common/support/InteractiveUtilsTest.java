package org.chomoo.arch4j.core.common.support;

import java.util.LinkedHashMap;
import java.util.Map;

class InteractiveUtilsTest {

    public static void main(String[] args) {
        Map<String,String> selectMap = new LinkedHashMap<>(){{
            put("I", "(Re)Install");
            put("U", "Update");
        }};
        String answer = InteractiveUtils.askSelect("select", selectMap);
        System.out.println("answer:" + answer);
    }

}