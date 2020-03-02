package github.incodelearning.interfaces;

/**
 * https://stackoverflow.com/questions/8537500/java-the-meaning-of-t-extends-comparablet
 * @param <Key> the generic type of key, can be any interface or class that implements Comparable<Key>.
 */
public class GenericComparable<Key extends Comparable<Key>> {
    Key key;

    public GenericComparable(Key key) {
        this.key = key;
    }
}
