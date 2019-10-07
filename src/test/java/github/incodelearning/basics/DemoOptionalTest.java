package github.incodelearning.basics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DemoOptionalTest {

    DemoOptional tbt;

    @Before
    public void setup() {
        this.tbt = new DemoOptional();
    }

    @Test
    public void testFlatMap3() {
        Integer n = 1;
        Optional<Optional<Optional<Integer>>> optionalInteger = Optional.of(Optional.of(Optional.ofNullable(n)));
        System.out.println(optionalInteger.flatMap(n1 -> n1.flatMap(n2 -> n2.map(n3 -> n3 * 2))));
    }

    @Test
    public void testIfPresent() {
        Optional<DemoOptional.USB> optionalUsb = Optional.of(tbt.new USB("3.0"));
        optionalUsb.ifPresent(System.out::println);
    }

    @Test
    public void testFilterMatching() {
        Optional<DemoOptional.USB> optionalUsb = Optional.ofNullable(tbt.new USB("3.0"));
        optionalUsb.filter(usb -> "3.0".equals(usb.version.orElse("unknown"))).ifPresent(System.out::println);
        List<Optional<DemoOptional.USB>> maybeUSBs = new ArrayList<>();
        maybeUSBs.add(optionalUsb);
        maybeUSBs.add(Optional.of(tbt.new USB("2.0")));
        assertEquals(1, maybeUSBs.stream().filter(usbo -> usbo.filter(
                usb -> "3.0".equals(usb.version.orElse("unknown"))).isPresent()).count());
    }

    @Test
    public void testFilterNonMatching() {
        Optional<DemoOptional.USB> maybeUSB = Optional.ofNullable(null);
        assertEquals(Optional.empty(), maybeUSB.filter(usb -> "3.0".equals(usb.version.orElse("unknown"))));
        maybeUSB = Optional.ofNullable(tbt.new USB("2.0"));
        assertEquals(Optional.empty(), maybeUSB.filter(usb -> "3.0".equals(usb.version.orElse("unknown"))));
    }

    @Test
    public void testMap() {
        Optional<DemoOptional.USB> optionalUsb = Optional.of(tbt.new USB("3.0"));
        System.out.println(optionalUsb.map(DemoOptional.USB::getVersion));//embedded/nested optional
        System.out.println(Optional.of("test").map(String::toUpperCase));
    }

    @Test
    public void testCascading() {
        // null computer
        DemoOptional.Computer computer = null;
        assertEquals(DemoOptional.UNKNOWN, tbt.getComputerSoundCardUSBVersion(Optional.ofNullable(computer)));

        String version = "2.3";
        computer = tbt.new Computer(tbt.new SoundCard(tbt.new USB(version)));
        assertEquals(version, tbt.getComputerSoundCardUSBVersion(Optional.of(computer)));
        // null usb
        computer = tbt.new Computer(tbt.new SoundCard(tbt.new USB(null)));
        assertEquals(DemoOptional.UNKNOWN, tbt.getComputerSoundCardUSBVersion(Optional.ofNullable(computer)));
        // null sound card
        computer = tbt.new Computer(null);
        assertEquals(DemoOptional.UNKNOWN, tbt.getComputerSoundCardUSBVersion(Optional.ofNullable(computer)));
    }

    @Test(expected = NullPointerException.class)
    public void testOptionalDefaultValue() {
        DemoOptional.SoundCard soundCard = tbt.new SoundCard();
        // calling .ifPresent throws NullPointer since the Optional<USB> is null, not Optional.empty()
        soundCard.getUsb().ifPresent(usb -> System.out.println(usb));
    }
}
