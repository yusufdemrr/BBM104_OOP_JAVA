import java.text.DecimalFormat;

public class SmartPlug extends SmartDevices {
    private boolean is_plugged_on = false;
    private int voltage = 220;
    private float ampere;
    private int volt = 220;
    private float consumedEnergy = 0;

    SmartPlug(String pName) {
        deviceName = pName;
    }

    SmartPlug(String pName, boolean is_switch_on){
        deviceName = pName;
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
    }

    SmartPlug(String pName, boolean is_switch_on, float ampere){
        deviceName = pName;
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
        this.ampere = ampere;
        this.is_plugged_on = true;
    }

    public boolean checkAmpere(){
        if(ampere <= 0){
            FileOutput.writeToFile(Main.output,"ERROR: Ampere value must be a positive number!",true,true);
            return false;
        } else {
            return true;
        }

    }

    public boolean checkAmpere(Float ampere){
        if(ampere <= 0){
            FileOutput.writeToFile(Main.output,"ERROR: Ampere value must be a positive number!",true,true);
            return false;
        } else {
            return true;
        }

    }

    public void setAmpere(float ampere) {
        this.ampere = ampere;
    }

    public void setIs_plugged_on(boolean is_plugged_on) {
        this.is_plugged_on = is_plugged_on;
    }

    public boolean checkPlug(){       //3'lü koşul yapılabilir.
        if (is_plugged_on){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Calculates the consumed energy in kilowatt hours if the device is both plugged in and switched on.
     *
     * @return the consumed energy in kilowatt hours
     */
    @Override
    public float calculate(){
        if(is_plugged_on && getIs_switch_on()){
            float totaltime = getTimeWorked();
            totaltime = totaltime /60;
           consumedEnergy += (totaltime) * volt * ampere ;
           return consumedEnergy;
        }
        return consumedEnergy;
    }

    @Override
    public String toString() {
        String time;
        DecimalFormat deci = new DecimalFormat("0.00");
        String formatted = deci.format(consumedEnergy);
        formatted = formatted.replace(".",",");
        if (getSwitchTime() == null ){
            time = "null";
        } else {
            time = timeConverter(getSwitchTime());
        }
        String onOff = getIs_switch_on() ? "on" : "off";
        return "Smart Plug " + deviceName + " is " + onOff +
                " and consumed " + formatted  + "W so far (excluding current device), and its " +
                "time to switch its status is " + time + "." ;
    }



}
