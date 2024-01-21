import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmartDevices {

    public String deviceName;
    private boolean is_switch_on = false;
    private LocalDateTime switchOntime = null;
    private LocalDateTime switchTime = null;
    private LocalDateTime switchOffTime = null;
    private int timeWorked = 0;

    public float calculate() {
        return 0;
    }

    /**
     * This method calculates the amount of time worked between the switch on time and switch off time
     * of a SmartDevices object and returns it in minutes.
     * If both switch on and switch off times are null, it returns 0.
     *
     * @return an integer representing the amount of time worked in minutes
     */
    public int getTimeWorked() {
        if(switchOffTime == null && switchOntime == null){
            return 0;
        } else {
            Duration duration = Duration.between(switchOntime, switchOffTime);
            long minutes = duration.toMinutes();
            timeWorked = (int) minutes;
            return timeWorked;
        }
    }

    public LocalDateTime getSwitchOffTime() {
        return switchOffTime;
    }

    public LocalDateTime getSwitchOntime() {
        return switchOntime;
    }

    public void setSwitchOntime(LocalDateTime switchOntime) {
        this.switchOntime = switchOntime;
    }

    public void setSwitchOffTime(LocalDateTime switchOffTime) {
        this.switchOffTime = switchOffTime;
    }

    public boolean getIs_switch_on() {
        return is_switch_on;
    }

    public void setIs_switch_on(boolean is_switch_on) {
        this.is_switch_on = is_switch_on;
    }

    public void turn_on(){
        this.is_switch_on = true;
    }

    public void turn_off(){
        this.is_switch_on = false;
    }

    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public String timeConverter(LocalDateTime localtime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formatDateTime = localtime.format(formatter);
        return formatDateTime;
    }
}
