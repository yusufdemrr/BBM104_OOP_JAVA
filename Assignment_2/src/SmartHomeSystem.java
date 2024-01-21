import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmartHomeSystem {

    public class ObjectComparator implements Comparator<Object> {

        @Override
        public int compare(Object obj1, Object obj2) {
            LocalDateTime dateTime1 = getDateTime(obj1);
            LocalDateTime dateTime2 = getDateTime(obj2);

            if (dateTime1 == null && dateTime2 == null) {
                return 0;
            }
            if (dateTime1 == null) {
                return 1;
            }
            if (dateTime2 == null) {
                return -1;
            }
            return dateTime1.compareTo(dateTime2);
        }

        private LocalDateTime getDateTime(Object obj) {
            SmartDevices device = (SmartDevices) obj;
            if(((SmartDevices) obj).getSwitchTime() == null){
                return null;
            } else {
                return ((SmartDevices) obj).getSwitchTime();
            }
        }
    }

    public LocalDateTime generalTime;


    public void commandReader(){
        String[] inputFile1 = FileInput.readFile(Main.input,true,true);

        LinkedHashMap<String,SmartDevices> alldevices = new LinkedHashMap<>();
        List<SmartDevices> alldevicesList = new ArrayList<>();

        FileOutput.writeToFile(Main.output,"",false,false);

        if (inputFile1.length == 0) {
            FileOutput.writeToFile(Main.output,"COMMAND: ",true,true);
            FileOutput.writeToFile(Main.output,"ERROR: First command must be set initial time! Program is going to terminate!",true,true);
        }

        int initialTimeSayac = 0;
        for (String line : inputFile1){
            String formattedCommmand = String.format("COMMAND: %s\n",line);
            FileOutput.writeToFile(Main.output,formattedCommmand,true,false);
            String[] modifiedLine = line.split("\t");
            if(!inputFile1[0].split("\t")[0].equals("SetInitialTime") || inputFile1.length == 1){
                FileOutput.writeToFile(Main.output,"ERROR: First command must be set initial time! Program is going to terminate!",true,true);
                break;
            }
            if (modifiedLine[0].equals("SetInitialTime")){
                if (initialTimeSayac == 0){
                    try {
                        generalTime = commandTimeToLocalDateTime(modifiedLine[1]);
                        String formattedTime = String.format("SUCCESS: Time has been set to %s!\n",modifiedLine[1]);
                        FileOutput.writeToFile(Main.output,formattedTime,true,false);
                        initialTimeSayac++;
                    } catch (Exception e){
                        FileOutput.writeToFile(Main.output,"ERROR: Format of the initial date is wrong! Program is going to terminate!",true,true);
                        break;
                    }
                } else {
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetTime")) {
                try{
                    if(modifiedLine.length != 2){
                        throw new ArrayIndexOutOfBoundsException();
                    }
                    LocalDateTime targetTime = commandTimeToLocalDateTime(modifiedLine[1]);
                    if (targetTime.isBefore(generalTime)){
                        FileOutput.writeToFile(Main.output,"ERROR: Time cannot be reversed!",true,true);
                    } else {
                        if(targetTime.equals(generalTime)){
                            FileOutput.writeToFile(Main.output,"ERROR: There is nothing to change!",true,true);
                        } else {
                            Duration duration = Duration.between(generalTime,targetTime);
                            long skipMinutes = duration.toMinutes();
                            int skipM = (int) skipMinutes;
                            skipTime(alldevicesList,generalTime,skipM,targetTime);
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException a){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                } catch (Exception e) {
                    FileOutput.writeToFile(Main.output,"ERROR: Time format is not correct!",true,true);
                }
            } else if (modifiedLine[0].equals("SkipMinutes")){
                try {
                    if(modifiedLine.length != 2){
                        throw new Exception();
                    }
                    int skipMinute = Integer.parseInt(modifiedLine[1]);
                    if(skipMinute < 0){
                        FileOutput.writeToFile(Main.output,"ERROR: Time cannot be reversed!",true,true);
                    } else if (skipMinute == 0) {
                        FileOutput.writeToFile(Main.output,"ERROR: There is nothing to skip!",true,true);
                    } else {
                        LocalDateTime targetTime = generalTime.plusMinutes(skipMinute);
                        skipTime(alldevicesList,generalTime,skipMinute,targetTime);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetSwitchTime")) {
                try {
                    if(modifiedLine.length != 3){
                        throw new IllegalArgumentException();
                    }
                    if(alldevices.containsKey(modifiedLine[1])){
                        if(commandTimeToLocalDateTime(modifiedLine[2]).isBefore(generalTime)){
                            FileOutput.writeToFile(Main.output,"ERROR: Switch time cannot be in the past!",true,true);
                        } else if(generalTime.equals(commandTimeToLocalDateTime(modifiedLine[2]))) {
                            SmartDevices device = alldevices.get(modifiedLine[1]);
                            device.setSwitchTime(commandTimeToLocalDateTime(modifiedLine[2]));
                            Collections.sort(alldevicesList, new ObjectComparator());
                            device.setSwitchTime(null);
                            if(device.getIs_switch_on()){
                                device.setSwitchOffTime(generalTime);
                                device.calculate();
                                device.setIs_switch_on(false);
                            } else {
                                device.setSwitchOntime(generalTime);
                                device.setIs_switch_on(true);
                            }
                        } else {
                            SmartDevices device = alldevices.get(modifiedLine[1]);
                            device.setSwitchTime(commandTimeToLocalDateTime(modifiedLine[2]));
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                    }
                }catch (IllegalArgumentException a){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                } catch (Exception e) {
                    FileOutput.writeToFile(Main.output,"ERROR: Time format is not correct!",true,true);
                }
            } else if (modifiedLine[0].equals("Nop")) {
                try{
                    if(modifiedLine.length != 1){
                        throw new Exception();
                    }
                    if(alldevicesList.size() == 0 || alldevicesList.get(0).getSwitchTime() == null){
                        FileOutput.writeToFile(Main.output,"ERROR: There is nothing to switch!",true,true);
                    } else {
                        LocalDateTime targetTime = alldevicesList.get(0).getSwitchTime();
                        Duration duration = Duration.between(generalTime,targetTime);
                        long skipMinutes = duration.toMinutes();
                        int skipM = (int) skipMinutes;
                        skipTime(alldevicesList,generalTime,skipM,targetTime);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("Remove")) {
                try {
                    if(modifiedLine.length != 2){
                        throw new Exception();
                    }
                    if(alldevices.containsKey(modifiedLine[1])){
                        FileOutput.writeToFile(Main.output,"SUCCESS: Information about removed smart device is as follows:",true,true);
                        if(alldevices.get(modifiedLine[1]).getIs_switch_on()) {
                            alldevices.get(modifiedLine[1]).setSwitchOffTime(generalTime);
                            alldevices.get(modifiedLine[1]).calculate();
                            alldevices.get(modifiedLine[1]).setIs_switch_on(false);
                        }
                        FileOutput.writeToFile(Main.output, alldevices.get(modifiedLine[1]).toString(),true,true);
                        alldevicesList.remove(alldevices.get(modifiedLine[1]));
                        alldevices.remove(modifiedLine[1]);
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }

            } else if (modifiedLine[0].equals("Switch")) {
                try {
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    if (alldevices.containsKey(deviceName)){
                        if(modifiedLine[2].equals("On")){
                            if(alldevices.get(deviceName).getIs_switch_on()){
                                FileOutput.writeToFile(Main.output,"ERROR: This device is already switched on!",true,true);
                            } else {
                                alldevices.get(deviceName).setIs_switch_on(true);
                                alldevices.get(deviceName).setSwitchOntime(generalTime);
                                if(alldevices.get(deviceName).getSwitchTime() != null){
                                    alldevices.get(deviceName).setSwitchTime(null);
                                }
                            }
                        } else if (modifiedLine[2].equals("Off")) {
                            if(alldevices.get(deviceName).getIs_switch_on()){
                                alldevices.get(deviceName).setSwitchOffTime(generalTime);
                                alldevices.get(deviceName).calculate();
                                alldevices.get(deviceName).setIs_switch_on(false);
                                if(alldevices.get(deviceName).getSwitchTime() != null){
                                    alldevices.get(deviceName).setSwitchTime(null);
                                }
                            } else {
                                FileOutput.writeToFile(Main.output,"ERROR: This device is already switched off!",true,true);
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("ChangeName")) {
                try {
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String oldName = modifiedLine[1];
                    String newName = modifiedLine[2];
                    if(!oldName.equals(newName)){
                        if (alldevices.containsKey(oldName)){
                            if(!alldevices.containsKey(newName)){
                                alldevices.get(oldName).deviceName = newName; //aynı cihaz olduğu için listede de adı değişiyor.
                                SmartDevices device = alldevices.remove(oldName);
                                alldevices.put(newName,device);
                            } else{
                                FileOutput.writeToFile(Main.output,"ERROR: There is already a smart device with same name!",true,true);
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: Both of the names are the same, nothing changed!",true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("PlugIn")) {
                try {
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String pName = modifiedLine[1];
                    Float ampere = Float.parseFloat(modifiedLine[2]);
                    if (alldevices.containsKey(pName)) {
                        SmartDevices device = alldevices.get(pName);
                        if (device instanceof SmartPlug) {
                            SmartPlug device1 = (SmartPlug) alldevices.get(pName);
                            if (device1.checkAmpere(ampere)){
                                if(device1.checkPlug()){
                                    FileOutput.writeToFile(Main.output,"ERROR: There is already an item plugged in to that plug!",true,true);
                                } else {
                                    device1.setAmpere(ampere);
                                    ((SmartPlug) device).setIs_plugged_on(true);
                                }
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart plug!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("PlugOut")) {
                try {
                    if(modifiedLine.length != 2){
                        throw new Exception();
                    }
                    String pName = modifiedLine[1];
                    if (alldevices.containsKey(pName)) {
                        SmartDevices device = alldevices.get(pName);
                        if (device instanceof SmartPlug) {
                            SmartPlug device1 = (SmartPlug) alldevices.get(pName);
                            if(device1.checkPlug()){
                                if(device1.getIs_switch_on()){
                                    device1.setSwitchOffTime(generalTime);
                                    device1.calculate();
                                    ((SmartPlug) device).setIs_plugged_on(false);
                                } else {
                                    ((SmartPlug) device).setIs_plugged_on(false);
                                }
                            } else {
                                FileOutput.writeToFile(Main.output,"ERROR: This plug has no item to plug out from that plug!",true,true);
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart plug!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: There is not such a device!",true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetKelvin")) {
                try{
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    int kelvin = Integer.parseInt(modifiedLine[2]);
                    if(alldevices.get(deviceName) instanceof SmartLamp){
                        SmartLamp device1 = (SmartLamp) alldevices.get(deviceName);
                        device1.setKelvin(kelvin);
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart lamp!",true,true);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetBrightness")){
                try{
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    int brightness = Integer.parseInt(modifiedLine[2]);
                    if(alldevices.get(deviceName) instanceof SmartLamp){
                        SmartLamp device1 = (SmartLamp) alldevices.get(deviceName);
                        device1.setBrightness(brightness);
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart lamp!",true,true);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if(modifiedLine[0].equals("SetColorCode")){
                try{
                    if(modifiedLine.length != 3){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    String colorCode = modifiedLine[2];
                    if(alldevices.get(deviceName) instanceof SmartLampsWithColor){
                        SmartLampsWithColor device1 = (SmartLampsWithColor) alldevices.get(deviceName);
                        if(colorCode.substring(0,2).equals("0x")){
                            if(device1.isHexadecimalInRange(colorCode)){
                                device1.setColor(colorCode);
                                device1.turn_on_color_mode();
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart color lamp!",true,true);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetWhite")) {
                try{
                    if(modifiedLine.length != 4){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    int kelvin = Integer.parseInt(modifiedLine[2]);
                    int brightness = Integer.parseInt(modifiedLine[3]);
                    if(alldevices.get(deviceName) instanceof SmartLamp){
                        if (alldevices.get(deviceName) instanceof SmartLampsWithColor){
                            SmartLampsWithColor device1 = (SmartLampsWithColor) alldevices.get(deviceName);
                            int oldKelvin = device1.getKelvin();
                            if(device1.suitable_values(kelvin,brightness)){
                                device1.setKelvin(kelvin);
                                device1.setBrightness(brightness);
                                device1.turn_off_color_mode();
                            } else {
                                device1.setKelvin(oldKelvin);
                            }
                        } else {
                            SmartLamp device1 = (SmartLamp) alldevices.get(deviceName);
                            int oldKelvin = device1.getKelvin();
                            if(device1.suitable_values(kelvin,brightness)){
                                device1.setKelvin(kelvin);
                                device1.setBrightness(brightness);
                            } else {
                                device1.setKelvin(oldKelvin);
                            }
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart lamp!",true,true);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("SetColor")) {
                try{
                    if(modifiedLine.length != 4){
                        throw new Exception();
                    }
                    String deviceName = modifiedLine[1];
                    String colorCode = modifiedLine[2];
                    int brightness = Integer.parseInt(modifiedLine[3]);
                    if(alldevices.get(deviceName) instanceof SmartLampsWithColor){
                        SmartLampsWithColor device1 = (SmartLampsWithColor) alldevices.get(deviceName);
                        if(colorCode.substring(0,2).equals("0x")){
                            if (device1.suitable_values1(colorCode,brightness)){
                                device1.setBrightness(brightness);
                                device1.setColor(colorCode);
                                device1.turn_on_color_mode();
                            }
                        } else {
                            FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                        }
                    } else {
                        FileOutput.writeToFile(Main.output,"ERROR: This device is not a smart color lamp!",true,true);
                    }
                } catch (Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("ZReport")) {
                try {
                    if(modifiedLine.length != 1){
                        throw new Exception();
                    }
                    FileOutput.writeToFile(Main.output,"Time is:\t"+timeConverter(generalTime),true,true);
                    Collections.sort(alldevicesList, new ObjectComparator());
                    for (SmartDevices device : alldevicesList){
                        FileOutput.writeToFile(Main.output, device.toString(),true,true);
                    }
                } catch(Exception e){
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else if (modifiedLine[0].equals("Add")) {

                String device = modifiedLine[1];
                if(device.equals("SmartLamp")){
                    try {
                        if((modifiedLine.length > 6) || (modifiedLine.length < 3)) {
                            throw new Exception();
                        }
                        int len = modifiedLine.length;
                        switch (len){
                            case 3:
                                SmartLamp lamp = new SmartLamp(modifiedLine[2]);
                                checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,lamp,alldevicesList);
                                break;
                            case 4:
                                if (modifiedLine[3].equals("On")){
                                    SmartLamp lamp1 = new SmartLamp(modifiedLine[2],true);
                                    lamp1.setSwitchOntime(generalTime);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,lamp1,alldevicesList);
                                } else if (modifiedLine[3].equals("Off")) {
                                    SmartLamp lamp1 = new SmartLamp(modifiedLine[2],false);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,lamp1,alldevicesList);
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                            case 6:
                                int kelvin = Integer.parseInt(modifiedLine[4]);
                                int brightness = Integer.parseInt(modifiedLine[5]);
                                if (modifiedLine[3].equals("On")){
                                    SmartLamp lamp1 = new SmartLamp(modifiedLine[2],true,kelvin,brightness);
                                    if(lamp1.suitable_values()){
                                        lamp1.setSwitchOntime(generalTime);
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,lamp1,alldevicesList);
                                    }
                                } else if (modifiedLine[3].equals("Off")) {
                                    SmartLamp lamp1 = new SmartLamp(modifiedLine[2],false,kelvin,brightness);
                                    if(lamp1.suitable_values()){
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,lamp1,alldevicesList);
                                    }
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                        }
                    } catch (Exception e){
                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                    }

                } else if (device.equals("SmartColorLamp")) {
                    try {
                        if((modifiedLine.length > 6) || (modifiedLine.length < 3)) {
                            throw new Exception();
                        }
                        int len = modifiedLine.length;
                        switch (len){
                            case 3:
                                SmartLampsWithColor cLamp = new SmartLampsWithColor(modifiedLine[2]);
                                checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,cLamp,alldevicesList);
                                break;
                            case 4:
                                if (modifiedLine[3].equals("On")){
                                    SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],true);
                                    clamp1.setSwitchOntime(generalTime);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                } else if (modifiedLine[3].equals("Off")) {
                                    SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],false);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                            case 6:
                                if(modifiedLine[4].substring(0,2).equals("0x")){
                                    String color = modifiedLine[4];
                                    int brightness = Integer.parseInt(modifiedLine[5]);
                                    if (modifiedLine[3].equals("On")){
                                        SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],true,color,brightness);
                                        if(clamp1.suitable_values1()){
                                            clamp1.setSwitchOntime(generalTime);
                                            checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                        }
                                    } else if (modifiedLine[3].equals("Off")) {
                                        SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],false,color,brightness);
                                        if(clamp1.suitable_values1()){
                                            checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                        }
                                    } else {
                                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                    }
                                } else {
                                    int kelvin = Integer.parseInt(modifiedLine[4]);
                                    int brightness = Integer.parseInt(modifiedLine[5]);
                                    if (modifiedLine[3].equals("On")){
                                        SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],true,kelvin,brightness);
                                        if(clamp1.suitable_values()){
                                            clamp1.setSwitchOntime(generalTime);
                                            checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                        }
                                    } else if (modifiedLine[3].equals("Off")) {
                                        SmartLampsWithColor clamp1 = new SmartLampsWithColor(modifiedLine[2],false,kelvin,brightness);
                                        if(clamp1.suitable_values()){
                                            checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,clamp1,alldevicesList);
                                        }
                                    } else {
                                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                    }
                                }
                                break;
                        }
                    } catch(Exception e){
                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                    }
//
                } else if (device.equals("SmartCamera")) {
                    try {
                        if((modifiedLine.length > 5) || (modifiedLine.length < 4)) {
                            throw new Exception();
                        }
                        float mps = Float.parseFloat(modifiedLine[3]);
                        int len = modifiedLine.length;
                        switch (len){
                            case 4:
                                SmartCamera cam = new SmartCamera(modifiedLine[2],mps);
                                if(cam.checkMPS()){
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,cam,alldevicesList);
                                }
                                break;
                            case 5:
                                if (modifiedLine[4].equals("On")){
                                    SmartCamera cam1 = new SmartCamera(modifiedLine[2],mps,true);
                                    if(cam1.checkMPS()){
                                        cam1.setSwitchOntime(generalTime);
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,cam1,alldevicesList);
                                    }
                                } else if (modifiedLine[4].equals("Off")) {
                                    SmartCamera cam1 = new SmartCamera(modifiedLine[2],mps,false);
                                    if(cam1.checkMPS()){
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,cam1,alldevicesList);
                                    }
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                        }
                    } catch(Exception e) {
                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                    }
                } else if (device.equals("SmartPlug")){
                    try {
                        if((modifiedLine.length > 5) || (modifiedLine.length < 3)) {
                            throw new Exception();
                        }
                        int len = modifiedLine.length;
                        switch (len){
                            case 3:
                                SmartPlug plug = new SmartPlug(modifiedLine[2]);
                                checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,plug,alldevicesList);
                                break;
                            case 4:
                                if (modifiedLine[3].equals("On")){
                                    SmartPlug plug1 = new SmartPlug(modifiedLine[2],true);
                                    plug1.setSwitchOntime(generalTime);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,plug1,alldevicesList);
                                } else if (modifiedLine[3].equals("Off")) {
                                    SmartPlug plug1 = new SmartPlug(modifiedLine[2],false);
                                    checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,plug1,alldevicesList);
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                            case 5:
                                float ampere = Float.parseFloat(modifiedLine[4]);
                                if (modifiedLine[3].equals("On")){
                                    SmartPlug plug1 = new SmartPlug(modifiedLine[2],true,ampere);
                                    if(plug1.checkAmpere()){
                                        plug1.setSwitchOntime(generalTime);
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,plug1,alldevicesList);
                                    }
                                } else if (modifiedLine[3].equals("Off")) {
                                    SmartPlug plug1 = new SmartPlug(modifiedLine[2],false,ampere);
                                    if(plug1.checkAmpere()){
                                        checkSmartDeviceNameInHashmapAndAdd(modifiedLine[2],alldevices,plug1,alldevicesList);
                                    }
                                } else {
                                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                                }
                                break;
                        }
                    } catch (Exception e){
                        FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                    }
                } else {
                    FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
                }
            } else {
                FileOutput.writeToFile(Main.output,"ERROR: Erroneous command!",true,true);
            }
        }

        String lastInput = inputFile1[inputFile1.length-1];
        if(!lastInput.equals("ZReport")){
            FileOutput.writeToFile(Main.output,"ZReport:",true,true);
            FileOutput.writeToFile(Main.output,"Time is:\t"+timeConverter(generalTime),true,true);
            Collections.sort(alldevicesList, new ObjectComparator());
            for (SmartDevices device : alldevicesList){
                FileOutput.writeToFile(Main.output, device.toString(),true,true);
            }
        }

    }

    /**
     * This method converts the given command time string to a LocalDateTime object.
     * The command time string should be in the format of "yyyy-MM-dd_HH:mm:ss".
     *
     * @param commandTime a string representing a command time in the format of "yyyy-MM-dd_HH:mm:ss"
     * @return a LocalDateTime object representing the given command time
     */
    public static LocalDateTime commandTimeToLocalDateTime(String commandTime) {
        String[] commandTimes = commandTime.split("[_ :-]");

        LocalDateTime localDateTime = LocalDateTime.of(
                Integer.parseInt(commandTimes[0]),
                Integer.parseInt(commandTimes[1]),
                Integer.parseInt(commandTimes[2]),
                Integer.parseInt(commandTimes[3]),
                Integer.parseInt(commandTimes[4]),
                Integer.parseInt(commandTimes[5])
        );

        return localDateTime;
    }

    /**
     * This method checks whether the provided new device name already exists in the given hashmap,
     * and adds the given SmartDevices object to both the hashmap and the provided list if it does not.
     *
     * @param newDeviceName the name of the new smart device to be checked and added to the hashmap
     * @param alldevices the hashmap containing all smart devices
     * @param device the SmartDevices object to be added to the hashmap and the list if the name is not already in the hashmap
     * @param alldevicesList the list containing all smart devices
     */
    public void checkSmartDeviceNameInHashmapAndAdd(String newDeviceName, HashMap alldevices, SmartDevices device,List alldevicesList){
        if(alldevices.containsKey(newDeviceName)){
            FileOutput.writeToFile(Main.output,"ERROR: There is already a smart device with same name!",true,true);
        } else {
            alldevices.put(newDeviceName,device);
            alldevicesList.add(device);
        }
    }

    /**
     * This method converts the given LocalDateTime object to a string in the "yyyy-MM-dd_HH:mm:ss" format.
     *
     * @param localtime the LocalDateTime object to be converted to a string
     * @return a string representing the given LocalDateTime object in the "yyyy-MM-dd_HH:mm:ss" format
     */
    public String timeConverter(LocalDateTime localtime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formatDateTime = localtime.format(formatter);
        return formatDateTime;
    }


    /**
     * This method skips the time by a given number of minutes for all smart devices
     * in the provided list between the specified general time and the target time.
     *
     * @param alldeviceslist the list of all smart devices to be processed
     * @param generalTime the general time to start skipping from
     * @param skipMinutes the number of minutes to skip
     * @param targetTime the target time to skip to
     */
    public void skipTime(List<SmartDevices> alldeviceslist, LocalDateTime generalTime, int skipMinutes, LocalDateTime targetTime) {
        for (SmartDevices device : alldeviceslist) {
            LocalDateTime deviceTime = device.getSwitchTime();
            if (deviceTime != null && deviceTime.isAfter(generalTime) && (deviceTime.isBefore(targetTime) || deviceTime.isEqual(targetTime))) {
                if(device.getIs_switch_on()){
                    device.setSwitchOffTime(device.getSwitchTime());
                    device.calculate();
                    device.setSwitchTime(null);
                    device.setIs_switch_on(false);
                } else {
                    device.setIs_switch_on(true);
                    device.setSwitchOntime(device.getSwitchTime());
                    device.setSwitchTime(null);
                }
            }
        }
        this.generalTime = targetTime;

    }

}