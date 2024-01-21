import java.sql.SQLOutput;
import java.text.DecimalFormat;

public class SmartCamera extends SmartDevices{
    private float mb_per_minute;
    private float usedMB = 0f;

    SmartCamera(String cName, float mps){
        deviceName = cName;
        this.mb_per_minute = mps;
    }

    SmartCamera(String cName, float mps, boolean is_switch_on){
        deviceName = cName;
        this.mb_per_minute = mps;
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
    }

    /**
     * Checks if the megabyte per minute value is a positive number.
     *
     * @return true if the megabyte per minute value is positive, false otherwise
     */
    public boolean checkMPS(){
        if(mb_per_minute <= 0){
            FileOutput.writeToFile(Main.output,"ERROR: Megabyte value must be a positive number!",true,true);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calculates the used megabytes by multiplying the time worked with
     * megabytes per minute and adding the result to usedMB field.
     *
     * @return the used megabytes
     */
    @Override
    public float calculate() {
        usedMB += getTimeWorked() * mb_per_minute ;
        return usedMB;
    }

    @Override
    public String toString() {
        String time;
        DecimalFormat deci = new DecimalFormat("0.00");
        String formatted = deci.format(usedMB);
        formatted = formatted.replace('.',',');
        if (getSwitchTime() == null){
            time = "null";
        } else {
            time = timeConverter(getSwitchTime());
        }
        String onOff = getIs_switch_on() ? "on" : "off";
        return "Smart Camera " + deviceName + " is " + onOff +
                " and used " + formatted + " MB of storage so far (excluding current status), and its " +
                "time to switch its status is " + time + "." ;
    }


}
