package VideoAnalyser;

import java.text.DecimalFormat;

public class Writer {
    public static final String
            ANSI_RESET = "\u001B[0m",
            ANSI_BLACK = "\u001B[30m",
            ANSI_RED = "\u001B[31m",
            ANSI_GREEN = "\u001B[32m",
            ANSI_YELLOW = "\u001B[33m",
            ANSI_BLUE = "\u001B[34m",
            ANSI_PURPLE = "\u001B[35m",
            ANSI_CYAN = "\u001B[36m",
            ANSI_WHITE = "\u001B[37m",
            ANSI_BLACK_BACKGROUND = "\u001B[40m",
            ANSI_RED_BACKGROUND = "\u001B[41m",
            ANSI_GREEN_BACKGROUND = "\u001B[42m",
            ANSI_YELLOW_BACKGROUND = "\u001B[43m",
            ANSI_BLUE_BACKGROUND = "\u001B[44m",
            ANSI_PURPLE_BACKGROUND = "\u001B[45m",
            ANSI_CYAN_BACKGROUND = "\u001B[46m",
            ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static final String processLine = "##################################################";

    public static void println(String text, int textColor, int backgroundColor){
        if (textColor < 30 || textColor > 37){
            System.err.println("Wrong text color id");
            return;
        }
        if (backgroundColor < 40 || backgroundColor > 47){
            System.err.println("Wrong background color id");
            return;
        }
        String colorBcg = new StringBuilder("\u001B[").append(backgroundColor).append("m").toString();
        System.out.println(getTextColor(textColor) + colorBcg + text);
    }

    public static void println(String text, int textColor){
        if (textColor < 30 || textColor > 37){
            System.err.println("Wrong text color id");
            return;
        }
        String color = new StringBuilder("\u001B[").append(textColor).append("m").toString();
        System.out.println(color + text);
    }

    public static void setAnsiReset(){
        System.out.println(ANSI_RESET);
    }

    private static String getTextColor(int textColor){
        return new StringBuilder("\u001B[").append(textColor).append("m").toString();
    }

    public static void print(String s, int textColor) {
        System.out.print(getTextColor(textColor) + s + "\r");
    }

    public static void printProcess(int stage, int maxStage, int textColor){
        double val = stage*100.0/maxStage;
        print(new StringBuilder(new DecimalFormat("#.#").format(val)).append("% [").append(new StringBuilder(String.format("%1$50s", new StringBuilder(processLine).replace((int) (val/2.0), 50, ""))).reverse()).append("]").toString(), textColor);
    }
}
