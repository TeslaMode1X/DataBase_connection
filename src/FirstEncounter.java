

public class FirstEncounter {

    private int hours;
    private int minutes;
    private int seconds;

    public FirstEncounter(int hours, int minutes, int seconds) {
        if (hours < 0 || hours >= 24 || minutes < 0 || minutes >= 60 || seconds < 0 || seconds >= 60) {
            throw new IllegalArgumentException("hour, minute, and/or second was out of range");
        }

        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String toString() {
        return String.format("%02d:%02d:%02d %s",
                ((hours == 0 || hours == 12) ? 12 : hours % 12),
                minutes,
                seconds,
                (hours < 12 ? "AM" : "PM"));
    }

    public String toUniversalString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

