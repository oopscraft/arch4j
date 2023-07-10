package org.oopscraft.arch4j.core.install;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public abstract class CoreInstaller {

    private final String[] args;

    public CoreInstaller(String[] args) {
        this.args = args;
    }

    public final String[] cloneArgs() {
        return Arrays.copyOf(this.args, this.args.length);
    }

    public abstract void install();

    public final String askInput(String question) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(question + ':');
            String answer = scanner.nextLine();
            if(answer != null && answer.trim().length() > 0) {
                return answer;
            }
        }
    }

    public final String askSelect(String question, Map<String,String> options) {
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

    public void askConfirm(String message) {
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
