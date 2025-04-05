public class Room {
    private String type;
    private boolean isVacant;
    private String dateAvailable; // Static initial date, updated dynamically via reservations
    private double pricePerNight;
    private String place;
    private int roomNumber;

    public Room(String type, boolean isVacant, String dateAvailable, double pricePerNight, String place, int roomNumber) {
        this.type = type;
        this.isVacant = isVacant;
        this.dateAvailable = dateAvailable;
        this.pricePerNight = pricePerNight;
        this.place = place;
        this.roomNumber = roomNumber;
    }

    public String getType() { return type; }
    public boolean isVacant() { return isVacant; }
    public void setVacant(boolean vacant) { this.isVacant = vacant; }
    public String getDateAvailable() { return dateAvailable; }
    public double getPricePerNight() { return pricePerNight; }
    public String getPlace() { return place; }
    public int getRoomNumber() { return roomNumber; }
}