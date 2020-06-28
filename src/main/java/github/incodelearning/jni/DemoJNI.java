package github.incodelearning.jni;

public class DemoJNI {
    static {
        System.loadLibrary("native");
    }

    public static void main(String[] args) {
        new DemoJNI().sayHello();
    }

    /**
     * {@code javac -h . DemoJNI.java} to create header file github_incodelearning_jni_DemoJNI.h.
     */
    private native void sayHello();

}
