public class Complaint {
    private int complaintId;
    private String username;
    private String contactNumber;
    private String roomNumber;
    private String complaintType;
    private int rating;

    public Complaint(int complaintId, String username, String contactNumber, String roomNumber, 
                     String complaintType, int rating) {
        this.complaintId = complaintId;
        this.username = username;
        this.contactNumber = contactNumber;
        this.roomNumber = roomNumber;
        this.complaintType = complaintType;
        this.rating = rating;
    }

    public int getComplaintId() { return complaintId; }
    public String getUsername() { return username; }
    public String getContactNumber() { return contactNumber; }
    public String getRoomNumber() { return roomNumber; }
    public String getComplaintType() { return complaintType; }
    public int getRating() { return rating; }
}