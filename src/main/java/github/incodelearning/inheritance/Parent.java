package github.incodelearning.inheritance;

import lombok.*;

@EqualsAndHashCode(of = {"id"})
@ToString
public class Parent {
    private String id;
    @NonNull
    private String name;
    private int age;
    private String unimportantInfo;

    @Builder
    public Parent(String name, int age, String unimportantInfo) {
        id = String.join("#", name, String.valueOf(age));
        this.name = name;
        this.age = age;
        this.unimportantInfo = unimportantInfo;
    }
}
