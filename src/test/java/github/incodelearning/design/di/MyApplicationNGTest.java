package github.incodelearning.design.di;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.inject.Inject;

@Guice(modules = MessageServiceModule.class)
public class MyApplicationNGTest {
    @Inject
    MyApplication tbt;

    @Test
    void testSendMessage() {
        tbt.sendMessage("test Message", "John Doe");
    }
}