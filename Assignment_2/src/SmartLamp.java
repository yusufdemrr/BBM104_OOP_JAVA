public class SmartLamp extends SmartDevices{

    private int kelvin = 4000;
    private int brightness = 100;
    private boolean suitablevalues = false;

    SmartLamp(String lName){
        deviceName = lName;
    }

    SmartLamp(String lName, boolean is_switch_on){
        deviceName = lName;
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
    }

    SmartLamp(String lName, boolean is_switch_on, int kelvin, int brightness){
        deviceName = lName;
        this.kelvin = kelvin;
        this.brightness = brightness;
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
    }

    public int getKelvin() {
        return kelvin;
    }

    /**
     * Sets the kelvin value of a SmartDevices object and returns a boolean indicating whether the operation was successful or not.
     * The kelvin value must be in the range of 2000K-6500K, otherwise the method returns false and prints an error message.
     *
     * @param kelvin an integer representing the kelvin value to be set
     * @return a boolean indicating whether the kelvin value was set successfully (true) or not (false)
     */
    public boolean setKelvin(int kelvin) {
        if (kelvin >= 2000 && kelvin <= 6500) {
            this.kelvin = kelvin;
            return true;
        } else {
            FileOutput.writeToFile(Main.output,"ERROR: Kelvin value must be in range of 2000K-6500K!",true,true);
            return false;
        }
    }

    /**
     * Sets the brightness value of a SmartDevices object and returns a boolean indicating whether the operation was successful or not.
     * The brightness value must be in the range of 0%-100%, otherwise the method returns false and prints an error message.
     *
     * @param brightness an integer representing the brightness value to be set
     * @return a boolean indicating whether the brightness value was set successfully (true) or not (false)
     */
    public boolean setBrightness(int brightness) {
        if (brightness >= 0 && brightness <= 100) {
            this.brightness = brightness;
            return true;
        } else {
            FileOutput.writeToFile(Main.output,"ERROR: Brightness must be in range of 0%-100%!",true,true);
            return false;
        }
    }

    /**
     * Checks whether the kelvin and brightness values of a SmartDevices object are suitable and sets the suitablevalues variable accordingly.
     * Returns a boolean indicating whether both values are suitable or not.
     * The kelvin value must be in the range of 2000K-6500K and the brightness value must be in the range of 0%-100%.
     *
     * @return a boolean indicating whether both the kelvin and brightness values are suitable (true) or not (false)
     */
    public boolean suitable_values() {
        this.suitablevalues = setKelvin(this.kelvin) && setBrightness(this.brightness);
        return suitablevalues;
    }

    public boolean suitable_values(int kelvin, int brightness) {
        this.suitablevalues = setKelvin(kelvin) && setBrightness(brightness);
        return suitablevalues;
    }


    public int getBrightness() {
        return brightness;
    }



    @Override
    public String toString() {
        String onOff = getIs_switch_on() ? "on" : "off";
        String time;
        if (getSwitchTime() == null){
            time = "null";
        } else {
            time = timeConverter(getSwitchTime());
        }
        return "Smart Lamp " + deviceName + " is " + onOff +
                    " and its kelvin value is " + kelvin + "K with " + brightness + "% brightness, and its " +
                    "time to switch its status is " + time + "." ;
    }
}
