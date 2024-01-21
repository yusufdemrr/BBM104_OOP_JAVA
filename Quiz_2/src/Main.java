import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static int i = 0;
    public static int len;
    public static String ofile;
    public static void main(String[] args) {
        String ifile;
        ifile = args[0];
        ofile = args[1];
        FileInput file = new FileInput();
        String lines[] = file.readFile(ifile,true,true);
//        System.out.println(Arrays.toString(lines));

        len = lines.length;
        Trip[] trips = new Trip[len];

        for (String line : lines){
            String[] SplittedLine = line.split("\t");
//            System.out.println(Arrays.toString(SplittedLine));
            LocalTime t = LocalTime.parse(SplittedLine[1]);
            int durat = Integer.parseInt(SplittedLine[2]);

            Trip trip = new Trip(SplittedLine[0],t,durat);
//            System.out.println(trip.arrivalTime);
            trips[i] = new Trip(trip.tripName, trip.departureTime, trip.duration);
            i++;
        }
        TripSchedule schedule = new TripSchedule(trips);

        // Create a TripController object with the TripSchedule
        TripController tripController = new TripController(schedule);

        // Output the departure schedule
//        System.out.println("Departure Schedule:");
        FileOutput.writeToFile(ofile,"Departure order:",false,true);
        tripController.DepartureSchedule(schedule.trips);
        FileOutput.writeToFile(ofile,"",true,true);
        // Output the arrival schedule
//        System.out.println("Arrival Schedule:");
        FileOutput.writeToFile(ofile,"Arrival order:",true,true);
        tripController.ArrivalSchedule(schedule.trips);


// Yusuf Demir b2210356074

    }
}