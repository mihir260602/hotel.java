import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private int reservationId;
    private String checkInDate;
    private String checkOutDate;
    private int roomNumber;
    private double billAmount;
    private String userId;

    public Reservation(int reservationId, String checkInDate, String checkOutDate, int roomNumber, double billAmount, String userId) {
        this.reservationId = reservationId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomNumber = roomNumber;
        this.billAmount = billAmount;
        this.userId = userId;
    }

    public void calculateBill(double pricePerNight) {
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkInDate), LocalDate.parse(checkOutDate));
        if (nights <= 0) nights = 1;
        this.billAmount = nights * pricePerNight;
    }

    public int getReservationId() { return reservationId; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public int getRoomNumber() { return roomNumber; }
    public double getBillAmount() { return billAmount; }
    public String getUserId() { return userId; }
}