package github.incodelearning.mockito;

import github.incodelearning.design.di.MyApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Mocking {
    private static final String version = "3.0";

    @Mock
    private Optional<String> tbt;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Foo foo;

    @Test
    public void testMockOptionalFinalClass() {
        USB usb = new USB(tbt);
        when(tbt.get()).thenReturn(version);

        assertEquals(version, usb.getVersion().get());
        verify(tbt).get();
    }

    /**
     * See <a href="https://tuhrig.de/everytime-a-mock-returns-a-mock-a-fairy-dies/">fairy dies</a>.
     */
    @Test
    public void testDeepStubs() {
        FooWrapper fooWrapper = new FooWrapper(foo);
        when(foo.getBar().getName()).thenReturn("deep");

        assertEquals("deep", fooWrapper.getBarName());
    }
}

@Data
@RequiredArgsConstructor
class USB {
    private final Optional<String> version;
}

@AllArgsConstructor
class FooWrapper {
    Foo foo;

    String getBarName() {
        return foo.getBar().getName();
    }
}

@Data
class Foo {
    Bar bar;
}

@Data
class Bar {
    String name;
}
