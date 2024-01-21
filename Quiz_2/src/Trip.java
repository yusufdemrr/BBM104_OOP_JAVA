import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Trip {
    public String tripName, state;
    public LocalTime departureTime, arrivalTime;
    public int duration;



    public Trip(String tripName, LocalTime departureTime, int duration) {
        this.tripName = tripName;
        this.departureTime = departureTime;
        this.duration = duration;
        this.arrivalTime = calculateArrivalTime();
        this.state = "IDLE";
    }

    public LocalTime calculateArrivalTime() {
        LocalTime ArrivalTime = departureTime.plus(duration, ChronoUnit.MINUTES);
        return ArrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

}
