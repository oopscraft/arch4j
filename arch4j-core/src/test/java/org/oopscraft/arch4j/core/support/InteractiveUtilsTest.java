package org.oopscraft.arch4j.core.support;

import org.apache.tools.ant.taskdefs.modules.Link;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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