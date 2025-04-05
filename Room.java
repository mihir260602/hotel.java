import java.util.ArrayList;
import java.util.List;

public class Room {
    private String type;
    private boolean isVacant;
    private double pricePerNight;
    private String place;
    private int roomNumber;

    private List<Reservation> reservations; // ✅ NEW

    public Room(String type, boolean isVacant, String dateAvailable, double pricePerNight, String place, int roomNumber) {
        this.type = type;
        this.isVacant = isVacant;
        this.pricePerNight = pricePerNight;
        this.place = place;
        this.roomNumber = roomNumber;
        this.reservations = new ArrayList<>(); // ✅ Initialize list
    }

    public String getType() { return type; }
    public boolean isVacant() { return isVacant; }
    public void setVacant(boolean vacant) { this.isVacant = vacant; }
    public double getPricePerNight() { return pricePerNight; }
    public String getPlace() { return place; }
    public int getRoomNumber() { return roomNumber; }
    public List<Reservation> getReservations() {
        return reservations;
    }
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}