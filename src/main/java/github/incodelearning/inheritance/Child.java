package github.incodelearning.inheritance;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true, of = {}) // of = {} will exclude every field in child class
@ToString(callSuper = true)
public class Child extends Parent {
    private String school;

    public Child(String name, int age, String unimportantInfo, String school) {
        super(name, age, unimportantInfo);
        this.school = school;
    }
}
