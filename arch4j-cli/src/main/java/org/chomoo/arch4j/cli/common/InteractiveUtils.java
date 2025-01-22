package org.oopscraft.arch4j.cli.common;

import java.util.Map;
import java.util.Scanner;

public class InteractiveUtils {

    public static String askInput(String question) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(question + ':');
            String answer = scanner.nextLine();
            if(answer != null && answer.trim().length() > 0) {
                return answer;
            }
        }
    }

    public static String askSelect(String question, Map<String,String> options) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println(question);
            options.forEach((key, value) -> System.out.printf("[%s]: %s%n", key, value));

            System.out.print("Select:");
            String answer = scanner.nextLine();
            if(answer != null && answer.trim().length() > 0) {
                if(options.containsKey(answer)) {
                    return answer;
                }
            }
        }
    }

    public static void askConfirm(String message) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(message + "[Y/n]:");
            String answer = scanner.nextLine();
            if("Y".equals(answer)) {
                break;
            }else if("n".equals(answer)) {
                System.exit(0);
            }
        }
    }

}
