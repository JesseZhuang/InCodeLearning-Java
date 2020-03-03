package github.incodelearning.basics;

import java.util.Scanner;

public class DemoScanner {
    static Scanner stdIn = new Scanner(System.in);
    static final String SPACE = " ";

    /**
     * hacker rank, java end of file.
     */
    static void readStdInputLines() {
        int count = 1;
        while (stdIn.hasNextLine()) {
            String line = stdIn.nextLine();
            System.out.println(count + SPACE + line);
            count++;
        }
    }

    public static void main(String[] args) {
        readStdInputLines();
    }
}
