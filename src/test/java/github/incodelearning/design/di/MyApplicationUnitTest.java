package github.incodelearning.design.di;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;

public class MyApplicationUnitTest {
    private Injector injector;
    @Mock
    MessageService messageService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageService.sendMessage(anyString(), anyString())).thenReturn(true);
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MessageService.class).toInstance(messageService);
            }
        });
    }

    @After
    public void tearDown() {
        injector = null;
    }

    @Test
    public void test() {
        MyApplication appTest = injector.getInstance(MyApplication.class);
        Assert.assertEquals(true, appTest.sendMessage("Hi Pankaj", "pankaj@abc.com"));
    }
}
