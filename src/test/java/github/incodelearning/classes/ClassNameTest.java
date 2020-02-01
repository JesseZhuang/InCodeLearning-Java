package github.incodelearning.classes;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClassNameTest {

    class Car implements Comparable<Car> {
        private Double reviewScore;

        @Override
        public int compareTo(Car o) {
            return this.reviewScore.compareTo(o.reviewScore);
        }
    }

    class Book implements Comparable<Book> {

        private String author;

        @Override
        public int compareTo(Book o) {
            return this.author.compareTo(o.author);
        }
    }

    @Test
    public void testGetSimpleName() {
        assertEquals("ClassNameTest", new ClassNameTest().getClass().getSimpleName());
        System.out.println(new ClassNameTest().new Book().getClass().getName());
        System.out.println(new ClassNameTest().new Book().getClass().getSimpleName());
    }

    @Test
    public void testGetName() {
        assertEquals("github.incodelearning.classes.ClassNameTest", new ClassNameTest().getClass().getName());
    }

    @Test
    public void testGetCanonicalName() {
        assertEquals("github.incodelearning.classes.ClassNameTest", new ClassNameTest().getClass().getCanonicalName());
    }

    @Test
    public void testGetTypeName() {
        assertEquals("github.incodelearning.classes.ClassNameTest", new ClassNameTest().getClass().getTypeName());
    }

    @Test
    public void testGetInterfaceImplementationNames() {
        Comparable c1 = new ClassNameTest().new Book();
        Comparable c2 = new ClassNameTest().new Car();
        Class c1Class = c1.getClass();
        Class c2Class = c2.getClass();
        assertEquals("Book", c1Class.getSimpleName());
        assertEquals("github.incodelearning.classes.ClassNameTest$Book", c1Class.getName());
        assertEquals("Car", c2Class.getSimpleName());
        assertEquals("github.incodelearning.classes.ClassNameTest$Car", c2Class.getName());
    }

    @Test
    public void testDifferentTypeObjects() {
        List<Class<?>> classes = ImmutableList.of(void.class, int.class, String.class, Runnable.class,
                HashMap.SimpleEntry.class, new Serializable(){}.getClass());
        for(Class c : classes) showClass(c);
    }

    private void showClass(Class<?> c) {
        System.out.println("getName():          " + c.getName());
        System.out.println("getCanonicalName(): " + c.getCanonicalName());
        System.out.println("getSimpleName():    " + c.getSimpleName());
        System.out.println("getTypeName():         " + c.getTypeName());
        System.out.println("toString():         " + c.toString());
        System.out.println();
    }
}
