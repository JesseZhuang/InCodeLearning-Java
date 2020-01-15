package github.incodelearning.basics;

public class DemoEnum {

    enum DayOfWeek {
        MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday");

        String day;

        DayOfWeek(String day) {
            this.day = day;
        }

        @Override
        public String toString() {
            return this.day;
        }
    }
}
