package github.incodelearning.basics;

public class DemoJDK12Switch {

    public enum Size {
        S, M, L, XL
    }

    public static int calcHeight(Size size) {
        int height = 0;
        switch (size) {
            case S:
                height = 18;
            case M:
                height = 20;
                break;
            case L:
                height = 25;
                break;
            case XL:
                height = 30;
                break;
            default:
                throw new IllegalArgumentException("unexpected size: " + size);
        }
        return height;
    }

    public static int calcHeightSwitchExp(Size size) {
        return switch (size) {
            case S -> 18;
            case M -> {
                System.out.println("Returning 20 for size M.");
                yield 20; // break 20; syntax change since java 13
            }
            case L -> 25;
            case XL -> 30;
            default -> throw new IllegalArgumentException("Unexpected value: " + size);
        };
    }
}
