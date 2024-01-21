public class SmartLampsWithColor extends SmartLamp{

    private String color;
    private boolean is_color_mode_on = false;
    private boolean suitablevalues1 = false;

    SmartLampsWithColor(String lName){
        super(lName);
    }

    SmartLampsWithColor(String lName, boolean is_switch_on){
        super(lName,is_switch_on);
    }

    SmartLampsWithColor(String lName, boolean is_switch_on, int kelvin, int brightness){
        super(lName,is_switch_on,kelvin,brightness);
    }

    SmartLampsWithColor(String lName, boolean is_switch_on, String color, int brightness){
        super(lName);
        this.color = color;
        is_color_mode_on = true;
        super.setBrightness(brightness);
        if(is_switch_on){
            turn_on();
        } else {
            turn_off();
        }
    }


    /**
     * Checks if the given hexadecimal color code is within the valid range.
     * @param hexNumber the hexadecimal color code to be checked
     * @return true if the hexadecimal color code is within the valid range, false otherwise
     */
    public boolean isHexadecimalInRange(String hexNumber) {

        int decimalValue = Integer.parseInt(hexNumber.substring(2), 16);

        if (decimalValue >= 0x000000 && decimalValue <= 0xFFFFFF) {
            return true;
        } else {
            FileOutput.writeToFile(Main.output,"ERROR: Color code value must be in range of 0x0-0xFFFFFF!",true,true);
            return false;
        }
    }

    public boolean suitable_values1() {
        this.suitablevalues1 = isHexadecimalInRange(this.color) && setBrightness(getBrightness());
        return suitablevalues1;
    }

    public boolean suitable_values1(String color, int brightness) {
        this.suitablevalues1 = isHexadecimalInRange(color) && setBrightness(brightness);
        return suitablevalues1;
    }


    public void turn_on_color_mode(){
        this.is_color_mode_on = true;
    }

    public void turn_off_color_mode(){
        this.is_color_mode_on = false;
    }
    public void setColor(String color) {
        this.color = color;
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
        if(is_color_mode_on){
            return "Smart Color Lamp " + deviceName + " is " + onOff +
                    " and its color value is " + color + " with " + getBrightness() + "% brightness, and its " +
                    "time to switch its status is " + time + "." ;
        } else {
            return "Smart Color Lamp " + deviceName + " is " + onOff +
                    " and its color value is " + getKelvin() + "K with " + getBrightness() + "% brightness, and its " +
                    "time to switch its status is " + time + "." ;
        }

    }
}
