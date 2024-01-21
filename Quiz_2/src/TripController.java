import java.util.Arrays;

public class TripController implements DepartureController,ArrivalController{
    protected TripSchedule trip_schedule;

    public static int leng;

    public static int sayac = 1;
    public static int sayac2 = 1;
    public TripController(TripSchedule tripSchedule) {
        this.trip_schedule = tripSchedule;
    }


    @Override
    public void ArrivalSchedule(Trip[] trips) {
        Arrays.sort(trips, (t1, t2) -> t1.getArrivalTime().compareTo(t2.getArrivalTime()));
        leng = trips.length;
        for (Trip trip : trips) {
            if (trip.arrivalTime.equals(trips[sayac].arrivalTime)) {
                trip.state = "DELAYED";
                trips[sayac].state = "DELAYED";
//                System.out.println(trip.tripName + " - " + trip.getArrivalTime() + " - " + trip.state);
                FileOutput.writeToFile(Main.ofile,trip.tripName + " arrive at " + trip.getArrivalTime() + "   Trip State:" + trip.state,true,true);
            } else {
//                System.out.println(trip.tripName + " - " + trip.getArrivalTime() + " - " + trip.state);
                FileOutput.writeToFile(Main.ofile,trip.tripName + " arrive at " + trip.getArrivalTime() + "   Trip State:" + trip.state,true,true);
            }
            if (sayac == leng-1){
//                System.out.println(trips[sayac].tripName + ", " + trips[sayac].getArrivalTime() + ", " + trips[sayac].state);
                FileOutput.writeToFile(Main.ofile,trips[sayac].tripName + " arrive at " + trips[sayac].getArrivalTime() + "   Trip State:" + trips[sayac].state,true,true);
                break;
            } else {
                sayac++;
            }
        }
    }

    @Override
    public void DepartureSchedule(Trip[] trips) {
        Arrays.sort(trips, (t1, t2) -> t1.getDepartureTime().compareTo(t2.getDepartureTime()));

        for (Trip trip : trips) {
//            String status = trip.state;
            leng = trips.length;
            if (trip.departureTime.equals(trips[sayac2].departureTime) ) {
                trip.state = "DELAYED";
                trips[sayac2].state = "DELAYED";
//                System.out.println(trip.tripName + ", " + trip.getDepartureTime() + ", " + trip.state);
                FileOutput.writeToFile(Main.ofile,trip.tripName + " depart at " + trip.getDepartureTime() + "   Trip State:" + trip.state,true,true);
            } else {
//                System.out.println(trip.tripName + ", " + trip.getDepartureTime() + ", " + trip.state);
                FileOutput.writeToFile(Main.ofile,trip.tripName + " depart at " + trip.getDepartureTime() + "   Trip State:" + trip.state,true,true);
            }
            if (sayac2 == leng-1){
//                System.out.println(trips[sayac2].tripName + ", " + trips[sayac2].getDepartureTime() + ", " + trips[sayac2].state);
                FileOutput.writeToFile(Main.ofile,trips[sayac2].tripName + " depart at " + trips[sayac2].getDepartureTime() + "   Trip State:" + trips[sayac2].state,true,true);
                break;
            } else {
                sayac2++;
            }

        }
    }
}






