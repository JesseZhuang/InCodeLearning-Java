package github.incodelearning.design;

public enum EnumSingleton3 {
    INSTANCE;

    private static Singleton singleton;

    static {
        singleton = Singleton.getInstance();
        System.out.println("static block executed in EnumSingleton3.");
    }

    public Singleton getSingleton() {
        return singleton;
    }
}
