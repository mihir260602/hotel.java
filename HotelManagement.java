import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class HotelManagement {
    private ArrayList<User> users;
    private ArrayList<Reservation> reservations;
    private ArrayList<Complaint> complaints;
    private ArrayList<Room> rooms;
    private Scanner scanner;
    private User currentUser;
    private static final LocalDate CURRENT_DATE = LocalDate.of(2025, 4, 5);
    private int nextUserId = 1;

    public HotelManagement() {
        users = new ArrayList<>();
        reservations = new ArrayList<>();
        complaints = new ArrayList<>();
        rooms = new ArrayList<>();
        scanner = new Scanner(System.in);
        
        // Initialize sample rooms
        rooms.add(new Room("Single", true, "2025-04-05", 50.0, "Downtown", 101));
        rooms.add(new Room("Double", true, "2025-04-05", 80.0, "Downtown", 201));
        rooms.add(new Room("Suite", true, "2025-04-05", 150.0, "Downtown", 301));
        rooms.add(new Room("Single", true, "2025-04-05", 50.0, "Downtown", 102));
        rooms.add(new Room("Double", true, "2025-04-05", 80.0, "Downtown", 202));

        // Sample users with auto-generated IDs and emails
        users.add(new User("U001", "admin123", true, "Admin User", "admin@hotel.com"));
        users.add(new User("U002", "cust123", false, "Customer One", "customer1@hotel.com"));
        nextUserId = 3;
    }

    // Utility method to display formatted popups
    private void showPopup(String title, String message) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("|| " + title.toUpperCase() + " ||");
        System.out.println("=".repeat(50));
        System.out.println(message);
        System.out.println("-".repeat(50));
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        System.out.println("\n");
    }

    // Check if email is unique (case-insensitive)
    private boolean isEmailUnique(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    // Main menu with Register, Login, Exit options
    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=== HOTEL MANAGEMENT SYSTEM ===");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");
            
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) throw new IllegalArgumentException("Choice cannot be empty!");
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > 3) throw new IllegalArgumentException("Choice must be 1-3!");
                
                switch (choice) {
                    case 1: registerNewUser(); break;
                    case 2: if (login()) return; break; // Return to proceed to role-based menu
                    case 3: System.out.println("Thank you for using our system!"); System.exit(0);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number!");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Register a new user with auto-generated ID and email validation
    private void registerNewUser() {
        System.out.println("\n=== NEW USER REGISTRATION ===");
        String userId = String.format("U%03d", nextUserId++); // e.g., U003

        String password;
        while (true) {
            try {
                System.out.print("Enter Password (6-20 chars): ");
                password = scanner.nextLine();
                if (password.isEmpty()) throw new IllegalArgumentException("Password cannot be empty!");
                if (password.length() < 6 || password.length() > 20 ||
                        !password.matches(".*[a-z].*") ||
                        !password.matches(".*[A-Z].*") ||
                        !password.matches(".*\\d.*")) {
                    throw new IllegalArgumentException("Password must be 6-20 characters and include at least one lowercase letter, one uppercase letter, and one digit.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        String name;
        while (true) {
            try {
                System.out.print("Enter Full Name (2-50 chars): ");
                name = scanner.nextLine();
                if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty!");
                if (name.length() < 2 || name.length() > 50) 
                    throw new IllegalArgumentException("Name must be 2-50 characters!");
                if (!name.matches("[a-zA-Z ]+")) 
                    throw new IllegalArgumentException("Name must contain only letters and spaces!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String email;
        while (true) {
            try {
                System.out.print("Enter Email (e.g., user@domain.com): ");
                email = scanner.nextLine();
                if (email.isEmpty()) throw new IllegalArgumentException("Email cannot be empty!");
                if (email.length() < 5 || email.length() > 50) 
                    throw new IllegalArgumentException("Email must be 5-50 characters!");
                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
                    throw new IllegalArgumentException("Invalid email format! Use user@domain.com.");
                if (!isEmailUnique(email)) 
                    throw new IllegalArgumentException("Email is already registered!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        users.add(new User(userId, password, false, name, email));
        showPopup("Registration Success", 
                  "User '" + name + "' registered successfully!\n" +
                  "Your User ID: " + userId + "\n" +
                  "Email: " + email);
    }

    // Login with User ID and password
    private boolean login() {
        System.out.println("\n=== LOGIN ===");
        String userId;
        while (true) {
            try {
                System.out.print("Enter User ID (e.g., U001): ");
                userId = scanner.nextLine();
                if (userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be empty!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String password;
        while (true) {
            try {
                System.out.print("Enter Password: ");
                password = scanner.nextLine();
                if (password.isEmpty()) throw new IllegalArgumentException("Password cannot be empty!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        for (User user : users) {
            if (user.getUserId().equals(userId) && user.verifyPassword(password)) {
                currentUser = user;
                showPopup("Login Success", "Welcome, " + user.getName() + "!\nUser ID: " + userId);
                if (user.isAdmin()) displayAdminMenu();
                else displayCustomerMenu();
                return true;
            }
        }
        System.out.println("Error: Invalid credentials!");
        return false;
    }

    // Customer menu with 8 options including Update/Delete Profile
    private void displayCustomerMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("Welcome, " + currentUser.getName() + " (ID: " + currentUser.getUserId() + ")");
            System.out.println("1. Reservation");
            System.out.println("2. Booking History");
            System.out.println("3. Room Status");
            System.out.println("4. Check-out Billing");
            System.out.println("5. Complaint");
            System.out.println("6. Contact");
            System.out.println("7. Update Profile");
            System.out.println("8. Delete Profile");
            System.out.println("9. Update Reservation");
            System.out.println("10. View Upcoming Bookings");
            System.out.println("11. LOGOUT!");
            System.out.print("Enter your choice (1-11, or any other key to logout): ");

            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) throw new IllegalArgumentException("Choice cannot be empty!");
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > 11) {
                    System.out.println("Logging out...");
                    displayMainMenu();
                    return;
                }
                
                switch (choice) {
                    case 1: makeReservation(); break;
                    case 2: viewBookingHistory(); break;
                    case 3: viewRoomStatus(); break;
                    case 4: checkoutBilling(); break;
                    case 5: registerComplaint(); break;
                    case 6: showContactSupport(); break;
                    case 7: updateProfile(); break;
                    case 8: deleteProfile(); break;
                    case 9: updateReservation();break;
                    case 10: viewUpcomingBookings();break;
                    case 11: displayMainMenu(); break;
                    default: System.out.println("Select a valid number from 1 to 9");
                }
            } catch (NumberFormatException e) {
                System.out.println("Logging out...");
                displayMainMenu();
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Admin menu with exactly 6 options
    private void displayAdminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("Welcome, " + currentUser.getName() + " (ID: " + currentUser.getUserId() + ")");
            System.out.println("1. Boook Hotel Room for User");
            System.out.println("2. Booking History");
            System.out.println("3. Booking History by ID");
            System.out.println("4. Room Status");
            System.out.println("5. Checkout Billing (Invoice)");
            System.out.println("6. View Complaints");
            System.out.println("7. upcoming bookings");
            System.out.println("8. upcoming booking by user ID");
            System.out.print("Enter your choice (1-8, or any other key to logout): ");

            try {
                String input = scanner.nextLine();
                if (input.isEmpty()) throw new IllegalArgumentException("Choice cannot be empty!");
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > 8) {
                    System.out.println("Logging out...");
                    displayMainMenu();
                    return;
                }
                
                switch (choice) {
                    case 1: bookHotelServices(); break;
                    case 2: viewBookingHistory(); break;
                    case 3: viewBookingHistoryById(); break;
                    case 4: viewRoomStatus(); break;
                    case 5: generateInvoice(); break;
                    case 6: viewComplaints(); break;
                    case 7: viewUpcomingBookings();break;
                    case 8:viewUpcomingBookingsById();break;

                }
            } catch (NumberFormatException e) {
                System.out.println("Logging out...");
                displayMainMenu();
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Calculate earliest available date for a room based on reservations
    private LocalDate getEarliestAvailableDate(Room room) {
        LocalDate earliest = CURRENT_DATE;
        for (Reservation r : reservations) {
            if (r.getRoomNumber() == room.getRoomNumber()) {
                LocalDate checkOut = LocalDate.parse(r.getCheckOutDate());
                if (checkOut.isAfter(earliest)) earliest = checkOut.plusDays(1);
            }
        }
        return earliest;
    }

    public void updateReservation() {
        viewUpcomingBookings();
        try {
            System.out.print("Enter the Reservation ID you want to modify: ");
            int reservationId = Integer.parseInt(scanner.nextLine());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();

            boolean found = false;

            for (Reservation r : reservations) {
                LocalDate checkInDate = LocalDate.parse(r.getCheckInDate(), formatter);

                if (r.getReservationId() == reservationId &&
                        (r.getUserId().equals(currentUser.getUserId()) || currentUser.isAdmin()) &&
                        !checkInDate.isBefore(today)) {

                    found = true;

                    System.out.println("What would you like to do?");
                    System.out.println("1. Change dates");
                    System.out.println("2. Delete reservation");
                    System.out.print("Enter your choice (1 or 2): ");
                    int choice = Integer.parseInt(scanner.nextLine());

                    if (choice == 1) {
                        System.out.print("Enter new Check-in Date (yyyy-MM-dd): ");
                        String newCheckInStr = scanner.nextLine();
                        System.out.print("Enter new Check-out Date (yyyy-MM-dd): ");
                        String newCheckOutStr = scanner.nextLine();

                        LocalDate newCheckIn = LocalDate.parse(newCheckInStr, formatter);
                        LocalDate newCheckOut = LocalDate.parse(newCheckOutStr, formatter);

                        Room room = getRoomByNumber(r.getRoomNumber());

                        if (!isRoomAvailable(room, newCheckIn, newCheckOut, r.getReservationId())) {
                            System.out.println("Error: The room is not available for the selected dates.");
                            for (Reservation conflict : room.getReservations()) {
                                if (conflict.getReservationId() != r.getReservationId()) {
                                    System.out.printf("Already booked from %s to %s%n",
                                            conflict.getCheckInDate(), conflict.getCheckOutDate());
                                }
                            }
                            return;
                        }

                        r.setCheckInDate(newCheckInStr);
                        r.setCheckOutDate(newCheckOutStr);
                        System.out.println("Reservation updated successfully!");

                    } else if (choice == 2) {
                        reservations.remove(r);

                        Room room = getRoomByNumber(r.getRoomNumber());
                        if (room != null) room.getReservations().remove(r);

                        System.out.println("Reservation deleted successfully.");
                    } else {
                        System.out.println("Invalid choice.");
                    }

                    break; // reservation is handled, no need to continue loop
                }
            }

            if (!found) {
                System.out.println("Reservation not found or not an upcoming booking.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }




    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut, int reservationIdToExclude) {
        for (Reservation r : room.getReservations()) {
            if (r.getReservationId() != reservationIdToExclude) {
                LocalDate existingCheckIn = LocalDate.parse(r.getCheckInDate());
                LocalDate existingCheckOut = LocalDate.parse(r.getCheckOutDate());
                if (!(checkOut.isBefore(existingCheckIn) || checkIn.isAfter(existingCheckOut))) {
                    return false;
                }
            }
        }
        return true;
    }


    // Check if a room is available for given dates
    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) {
            if (r.getRoomNumber() == room.getRoomNumber()) {
                LocalDate existingCheckIn = LocalDate.parse(r.getCheckInDate());
                LocalDate existingCheckOut = LocalDate.parse(r.getCheckOutDate());
                if (!(checkOut.isBefore(existingCheckIn) || checkIn.isAfter(existingCheckOut))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void viewUpcomingBookings() {
        StringBuilder upcoming = new StringBuilder();
        boolean hasUpcoming = false;
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Reservation r : reservations) {
            if (r.getUserId().equals(currentUser.getUserId()) || currentUser.isAdmin()) {
                LocalDate checkIn = LocalDate.parse(r.getCheckInDate(), formatter);
                if (!checkIn.isBefore(today)) {
                    upcoming.append(String.format("ID: %d, Check-in: %s, Check-out: %s, Room: %d%n",
                            r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(), r.getRoomNumber()));
                    hasUpcoming = true;
                }
            }
        }

        if (!hasUpcoming) {
            showPopup("Upcoming Bookings", "No upcoming bookings found.");
        } else {
            showPopup("Upcoming Bookings", upcoming.toString());
        }
    }



    // Customer reservation process
    private void makeReservation() {
        try {
            LocalDate checkIn;
            while (true) {
                System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
                String checkInStr = scanner.nextLine();
                if (!checkInStr.matches("\\d{4}-\\d{2}-\\d{2}"))
                    throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD.");
                checkIn = LocalDate.parse(checkInStr);
                if (checkIn.isBefore(CURRENT_DATE))
                    throw new IllegalArgumentException("Check-in date cannot be in the past!");
                break;
            }

            LocalDate checkOut;
            while (true) {
                System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
                String checkOutStr = scanner.nextLine();
                if (!checkOutStr.matches("\\d{4}-\\d{2}-\\d{2}"))
                    throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD.");
                checkOut = LocalDate.parse(checkOutStr);
                if (checkOut.isBefore(CURRENT_DATE))
                    throw new IllegalArgumentException("Check-out date cannot be in the past!");
                if (!checkOut.isAfter(checkIn))
                    throw new IllegalArgumentException("Check-out date must be after check-in date!");
                break;
            }

            System.out.println("Available Room Types:");
            System.out.println("1. Single ($50/night)");
            System.out.println("2. Double ($80/night)");
            System.out.println("3. Suite ($150/night)");
            System.out.print("Select Room Type (1-3): ");
            String typeInput = scanner.nextLine();
            if (typeInput.isEmpty()) throw new IllegalArgumentException("Room type cannot be empty!");
            int typeChoice = Integer.parseInt(typeInput);
            String roomType;
            double pricePerNight;
            switch (typeChoice) {
                case 1: roomType = "Single"; pricePerNight = 50.0; break;
                case 2: roomType = "Double"; pricePerNight = 80.0; break;
                case 3: roomType = "Suite"; pricePerNight = 150.0; break;
                default: throw new IllegalArgumentException("Invalid room type! Choose 1-3.");
            }

            Room availableRoom = null;
            for (Room r : rooms) {
                if (r.getType().equals(roomType) && isRoomAvailable(r, checkIn, checkOut)) {
                    availableRoom = r;
                    break;
                }
            }

            if (availableRoom == null)
                throw new IllegalArgumentException("No vacant " + roomType + " rooms available for these dates!");

            int reservationId = reservations.size() + 1;
            Reservation newReservation = new Reservation(
                    reservationId,
                    checkIn.toString(),
                    checkOut.toString(),
                    availableRoom.getRoomNumber(),
                    0.0,
                    currentUser.getUserId()
            );
            newReservation.calculateBill(pricePerNight);
            reservations.add(newReservation);
            availableRoom.addReservation(newReservation); // ✅ Add to room's reservation list

            showPopup("Reservation Confirmed",
                    "Reservation ID: " + reservationId + "\n" +
                            "Room Number: " + availableRoom.getRoomNumber() + "\n" +
                            "Check-in: " + checkIn + "\n" +
                            "Check-out: " + checkOut + "\n" +
                            "Room Type: " + roomType);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Room type must be a valid number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View booking history for current user or all (admin)
    private void viewBookingHistory() {
        StringBuilder history = new StringBuilder();
        boolean hasBookings = false;
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Reservation r : reservations) {
            if (r.getUserId().equals(currentUser.getUserId()) || currentUser.isAdmin()) {
                LocalDate checkIn = LocalDate.parse(r.getCheckInDate(), formatter);
                if (checkIn.isBefore(today)) {
                    history.append(String.format("ID: %d, Check-in: %s, Check-out: %s, Room: %d, Bill: $%.2f%n",
                            r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(),
                            r.getRoomNumber(), r.getBillAmount()));
                    hasBookings = true;
                }
            }
        }

        if (!hasBookings) {
            showPopup("Previous Bookings", "No previous bookings found.");
        } else {
            showPopup("Previous Bookings", history.toString());
        }
    }


    // View room status with dynamic availability
    private void viewRoomStatus() {
        System.out.print("Enter date to check room status (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate queryDate;

        try {
            if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}"))
                throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD.");

            queryDate = LocalDate.parse(dateStr);

            // ✅ Check if the date is today or in the future
            if (queryDate.isBefore(CURRENT_DATE)) {
                throw new IllegalArgumentException("You can only check status for today or a future date!");
            }

        } catch (Exception e) {
            showPopup("Error", "Invalid date: " + e.getMessage());
            return;
        }

        StringBuilder status = new StringBuilder();
        if (rooms.isEmpty()) {
            showPopup("Room Status", "No rooms available.");
            return;
        }
        for (Room r : rooms) {
            boolean isOccupied = false;

            for (Reservation res : r.getReservations()) {
                LocalDate resCheckIn = LocalDate.parse(res.getCheckInDate());
                LocalDate resCheckOut = LocalDate.parse(res.getCheckOutDate());

                // ✅ If queryDate falls within a reservation range, it's occupied
                if (!queryDate.isBefore(resCheckIn) && queryDate.isBefore(resCheckOut)) {
                    isOccupied = true;
                    break;
                }
            }

            String occupancyStatus = isOccupied ? "Occupied" : "Vacant";
            LocalDate availableDate = getEarliestAvailableDate(r);

            status.append(String.format(
                    "Type: %s, Room: %d, Status on %s: %s, Available From: %s, Price: $%.2f/night, Place: %s%n",
                    r.getType(), r.getRoomNumber(), queryDate, occupancyStatus,
                    availableDate, r.getPricePerNight(), r.getPlace()));
        }

        showPopup("Room Status for " + queryDate, status.toString());
    }

    // Customer checkout and billing process
    private void checkoutBilling() {
        viewBookingHistory();
        try {
            System.out.print("Enter Reservation ID to checkout (1-" + reservations.size() + "): ");
            String idInput = scanner.nextLine();
            if (idInput.isEmpty()) throw new IllegalArgumentException("Reservation ID cannot be empty!");
            int id = Integer.parseInt(idInput);

            Reservation reservation = null;
            Room bookedRoom = null;
            for (Reservation r : reservations) {
                if (r.getReservationId() == id && r.getUserId().equals(currentUser.getUserId())) {
                    reservation = r;
                    for (Room room : rooms) {
                        if (room.getRoomNumber() == r.getRoomNumber()) {
                            bookedRoom = room;
                            break;
                        }
                    }
                    break;
                }
            }
            if (reservation == null) 
                throw new IllegalArgumentException("Reservation not found or not yours!");
            if (bookedRoom == null) 
                throw new IllegalStateException("Internal error: Room not found!");

            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.parse(reservation.getCheckInDate()), 
                LocalDate.parse(reservation.getCheckOutDate())
            );
            if (nights <= 0) nights = 1;
            String checkoutDetails = String.format(
                "Room Type: %s%nNights: %d%nTotal Bill: $%.2f",
                bookedRoom.getType(), nights, reservation.getBillAmount()
            );
            showPopup("Checkout Billing", checkoutDetails);

            System.out.print("Proceed with payment? (yes/no): ");
            if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                showPopup("Checkout Cancelled", "Checkout process cancelled.");
                return;
            }

            if (processPayment(reservation)) {
                bookedRoom.setVacant(true);
                reservations.remove(reservation);
                showPopup("Checkout Complete", "Room " + reservation.getRoomNumber() + " has been vacated.");
            } else {
                showPopup("Checkout Failed", "Payment was not successful. Room remains occupied.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Reservation ID must be a valid number!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Process payment for a reservation
    private boolean processPayment(Reservation r) {
        try {
            System.out.println("Payment Options:");
            System.out.println("1. Debit Card");
            System.out.println("2. Credit Card");
            System.out.print("Select payment option (1-2): ");
            String optionInput = scanner.nextLine();
            if (optionInput.isEmpty()) throw new IllegalArgumentException("Option cannot be empty!");
            int option = Integer.parseInt(optionInput);
            if (option != 1 && option != 2) 
                throw new IllegalArgumentException("Invalid payment option! Choose 1 or 2.");

            System.out.print("Card Holder Name (2-50 chars): ");
            String name = scanner.nextLine();
            if (name.isEmpty()) throw new IllegalArgumentException("Card holder name cannot be empty!");
            if (name.length() < 2 || name.length() > 50) 
                throw new IllegalArgumentException("Name must be 2-50 characters!");
            if (!name.matches("[a-zA-Z ]+")) 
                throw new IllegalArgumentException("Name must contain only letters and spaces!");

            System.out.print("Card Number (16 digits): ");
            String number = scanner.nextLine();
            if (!number.matches("\\d{16}")) 
                throw new IllegalArgumentException("Card number must be 16 digits!");

            System.out.print("CVV (3 digits): ");
            String cvv = scanner.nextLine();
            if (!cvv.matches("\\d{3}")) 
                throw new IllegalArgumentException("CVV must be 3 digits!");

            System.out.print("Expiry Date (MM/YY): ");
            String expiry = scanner.nextLine();
            if (!expiry.matches("\\d{2}/\\d{2}")) 
                throw new IllegalArgumentException("Expiry date must be in MM/YY format!");

            showPopup("Payment Success", 
                      "Reservation ID: " + r.getReservationId() + "\n" +
                      "Amount Paid: $" + String.format("%.2f", r.getBillAmount()));
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: Payment option must be a valid number!");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Register a customer complaint
    private void registerComplaint() {
        try {
            System.out.print("Enter Contact Number (10 digits): ");
            String contact = scanner.nextLine();
            if (!contact.matches("\\d{10}")) 
                throw new IllegalArgumentException("Contact number must be 10 digits!");

            System.out.print("Enter Room Number (1-999): ");
            String room = scanner.nextLine();
            if (!room.matches("\\d{1,3}")) 
                throw new IllegalArgumentException("Room number must be 1-999!");

            System.out.print("Enter Complaint Type (min 3 chars): ");
            String type = scanner.nextLine();
            if (type.isEmpty()) throw new IllegalArgumentException("Complaint type cannot be empty!");
            if (type.length() < 3) throw new IllegalArgumentException("Complaint type must be at least 3 characters!");

            System.out.print("Enter Rating (1-5): ");
            String ratingInput = scanner.nextLine();
            if (ratingInput.isEmpty()) throw new IllegalArgumentException("Rating cannot be empty!");
            int rating = Integer.parseInt(ratingInput);
            if (rating < 1 || rating > 5) 
                throw new IllegalArgumentException("Rating must be between 1 and 5!");

            int complaintId = complaints.size() + 1;
            complaints.add(new Complaint(complaintId, currentUser.getName(), contact, room, type, rating));
            showPopup("Complaint Registered", 
                      "Complaint ID: " + complaintId + "\n" +
                      "Contact: " + contact + "\n" +
                      "Room: " + room + "\n" +
                      "Complaint: " + type + "\n" +
                      "Rating: " + rating);
            showContactSupport();
        } catch (NumberFormatException e) {
            System.out.println("Error: Rating must be a valid number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display contact support details
    private void showContactSupport() {
        showPopup("Contact Support", 
                  "Contact Number: 1-800-HOTEL-HELP\n" +
                  "Email: support@hotel.com\n" +
                  "Address: 123 Hotel Street, City, Country");
    }

    // Admin booking on behalf of a user
    private void bookHotelServices() {
        try {
            LocalDate checkIn;
            while (true) {
                System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
                String checkInStr = scanner.nextLine();
                if (!checkInStr.matches("\\d{4}-\\d{2}-\\d{2}")) 
                    throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD.");
                checkIn = LocalDate.parse(checkInStr);
                if (checkIn.isBefore(CURRENT_DATE)) 
                    throw new IllegalArgumentException("Check-in date cannot be in the past!");
                break;
            }

            LocalDate checkOut;
            while (true) {
                System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
                String checkOutStr = scanner.nextLine();
                if (!checkOutStr.matches("\\d{4}-\\d{2}-\\d{2}")) 
                    throw new IllegalArgumentException("Invalid date format! Use YYYY-MM-DD.");
                checkOut = LocalDate.parse(checkOutStr);
                if (checkOut.isBefore(CURRENT_DATE)) 
                    throw new IllegalArgumentException("Check-out date cannot be in the past!");
                if (!checkOut.isAfter(checkIn)) 
                    throw new IllegalArgumentException("Check-out date must be after check-in date!");
                break;
            }

            System.out.println("Available Room Types:");
            System.out.println("1. Single ($50/night)");
            System.out.println("2. Double ($80/night)");
            System.out.println("3. Suite ($150/night)");
            System.out.print("Select Room Type (1-3): ");
            String typeInput = scanner.nextLine();
            if (typeInput.isEmpty()) throw new IllegalArgumentException("Room type cannot be empty!");
            int typeChoice = Integer.parseInt(typeInput);
            String roomType;
            double pricePerNight;
            switch (typeChoice) {
                case 1: roomType = "Single"; pricePerNight = 50.0; break;
                case 2: roomType = "Double"; pricePerNight = 80.0; break;
                case 3: roomType = "Suite"; pricePerNight = 150.0; break;
                default: throw new IllegalArgumentException("Invalid room type! Choose 1-3.");
            }

            System.out.print("Enter User ID for booking (e.g., U001): ");
            String userId = scanner.nextLine();
            if (userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be empty!");
            boolean userExists = false;
            for (User u : users) {
                if (u.getUserId().equals(userId)) {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) throw new IllegalArgumentException("User ID does not exist!");

            Room availableRoom = null;
            for (Room r : rooms) {
                if (r.getType().equals(roomType) && r.isVacant() && isRoomAvailable(r, checkIn, checkOut)) {
                    availableRoom = r;
                    break;
                }
            }
            if (availableRoom == null) 
                throw new IllegalArgumentException("No vacant " + roomType + " rooms available for these dates!");

            int reservationId = reservations.size() + 1;
            Reservation newReservation = new Reservation(reservationId, checkIn.toString(), checkOut.toString(), 
                                                        availableRoom.getRoomNumber(), 0.0, userId);
            newReservation.calculateBill(pricePerNight);
            reservations.add(newReservation);
            availableRoom.setVacant(false);
            showPopup("Booking Confirmed", 
                      "Reservation ID: " + reservationId + "\n" +
                      "Room Number: " + availableRoom.getRoomNumber() + "\n" +
                      "Check-in: " + checkIn + "\n" +
                      "Check-out: " + checkOut + "\n" +
                      "Room Type: " + roomType + "\n" +
                      "User ID: " + userId);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Room type must be a valid number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all complaints (admin only)
    private void viewComplaints() {
        StringBuilder complaintsList = new StringBuilder();
        if (complaints.isEmpty()) {
            showPopup("Complaints", "No complaints available.");
            return;
        }
        for (Complaint c : complaints) {
            complaintsList.append(String.format("ID: %d, User: %s, Contact: %s, Room: %s, Type: %s, Rating: %d%n",
                c.getComplaintId(), c.getUsername(), c.getContactNumber(), c.getRoomNumber(), 
                c.getComplaintType(), c.getRating()));
        }
        showPopup("Complaints", complaintsList.toString());
    }

    // View booking history by User ID (admin only)
    private void viewBookingHistoryById() {
        try {
            System.out.print("Enter User ID (e.g., U001): ");
            String userId = scanner.nextLine();
            if (userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be empty!");

            boolean userExists = false;
            for (User u : users) {
                if (u.getUserId().equals(userId)) {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) throw new IllegalArgumentException("User ID does not exist!");

            StringBuilder history = new StringBuilder();
            boolean hasBookings = false;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Reservation r : reservations) {
                if (r.getUserId().equals(userId)) {
                    LocalDate checkIn = LocalDate.parse(r.getCheckInDate(), formatter);
                    if (checkIn.isBefore(today)) {
                        history.append(String.format("ID: %d, Check-in: %s, Check-out: %s, Room: %d, Bill: $%.2f%n",
                                r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(),
                                r.getRoomNumber(), r.getBillAmount()));
                        hasBookings = true;
                    }
                }
            }

            if (!hasBookings) {
                showPopup("Booking History for " + userId, "No past bookings found for this user.");
            } else {
                showPopup("Booking History for " + userId, history.toString());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewUpcomingBookingsById() {
        try {
            System.out.print("Enter User ID (e.g., U001): ");
            String userId = scanner.nextLine();
            if (userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be empty!");

            boolean userExists = false;
            for (User u : users) {
                if (u.getUserId().equals(userId)) {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) throw new IllegalArgumentException("User ID does not exist!");

            StringBuilder upcoming = new StringBuilder();
            boolean hasUpcoming = false;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Reservation r : reservations) {
                if (r.getUserId().equals(userId)) {
                    LocalDate checkIn = LocalDate.parse(r.getCheckInDate(), formatter);
                    if (!checkIn.isBefore(today)) {
                        upcoming.append(String.format("ID: %d, Check-in: %s, Check-out: %s, Room: %d%n",
                                r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(),
                                r.getRoomNumber()));
                        hasUpcoming = true;
                    }
                }
            }

            if (!hasUpcoming) {
                showPopup("Upcoming Bookings for " + userId, "No upcoming bookings found for this user.");
            } else {
                showPopup("Upcoming Bookings for " + userId, upcoming.toString());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // Generate invoice for a reservation (admin only)
    private void generateInvoice() {
        try {
            System.out.print("Enter User ID (e.g., U001): ");
            String userId = scanner.nextLine();
            if (userId.isEmpty()) throw new IllegalArgumentException("User ID cannot be empty!");
            boolean userExists = false;
            for (User u : users) {
                if (u.getUserId().equals(userId)) {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) throw new IllegalArgumentException("User ID does not exist!");

            viewBookingHistoryById();
            System.out.print("Enter Reservation ID for invoice (1-" + reservations.size() + "): ");
            String idInput = scanner.nextLine();
            if (idInput.isEmpty()) throw new IllegalArgumentException("Reservation ID cannot be empty!");
            int id = Integer.parseInt(idInput);

            for (Reservation r : reservations) {
                if (r.getReservationId() == id && r.getUserId().equals(userId)) {
                    Room bookedRoom = null;
                    for (Room room : rooms) {
                        if (room.getRoomNumber() == r.getRoomNumber()) {
                            bookedRoom = room;
                            break;
                        }
                    }
                    String invoice = String.format(
                        "Invoice - ID: %d%nCheck-in: %s%nCheck-out: %s%nRoom: %d%nType: %s%nAmount: $%.2f",
                        r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(), 
                        r.getRoomNumber(), bookedRoom != null ? bookedRoom.getType() : "Unknown", 
                        r.getBillAmount()
                    );
                    showPopup("Invoice", invoice);
                    return;
                }
            }
            throw new IllegalArgumentException("Reservation not found for this user!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Reservation ID must be a valid number!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Update profile for normal users
    private void updateProfile() {
        System.out.println("\n=== UPDATE PROFILE ===");
        System.out.println("Current Name: " + currentUser.getName());
        System.out.println("User ID: " + currentUser.getUserId() + " (cannot be changed)");
        System.out.println("Current Email: " + currentUser.getEmail());
        
        String newName;
        while (true) {
            try {
                System.out.print("Enter New Name (2-50 chars, leave blank to keep current): ");
                newName = scanner.nextLine();
                if (newName.isEmpty()) {
                    newName = currentUser.getName();
                    break;
                }
                if (newName.length() < 2 || newName.length() > 50) 
                    throw new IllegalArgumentException("Name must be 2-50 characters!");
                if (!newName.matches("[a-zA-Z ]+")) 
                    throw new IllegalArgumentException("Name must contain only letters and spaces!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String newEmail;
        while (true) {
            try {
                System.out.print("Enter New Email (e.g., user@domain.com, leave blank to keep current): ");
                newEmail = scanner.nextLine();
                if (newEmail.isEmpty()) {
                    newEmail = currentUser.getEmail();
                    break;
                }
                if (newEmail.length() < 5 || newEmail.length() > 50) 
                    throw new IllegalArgumentException("Email must be 5-50 characters!");
                if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
                    throw new IllegalArgumentException("Invalid email format! Use user@domain.com.");
                if (!newEmail.equals(currentUser.getEmail()) && !isEmailUnique(newEmail)) 
                    throw new IllegalArgumentException("Email is already registered!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String newPassword;
        while (true) {
            try {
                System.out.print("Enter New Password (6-20 chars, leave blank to keep current): ");
                newPassword = scanner.nextLine();
                if (newPassword.isEmpty()) {
                    newPassword = null; // Keep current password
                    break;
                }
                if (newPassword.length() < 6 || newPassword.length() > 20) 
                    throw new IllegalArgumentException("Password must be 6-20 characters!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        User updatedUser = new User(currentUser.getUserId(), 
                                  newPassword == null ? currentUser.verifyPassword("dummy") ? "dummy" : currentUser.getUserId() : newPassword, // Hack to keep password
                                  currentUser.isAdmin(), newName, newEmail);
        users.remove(currentUser);
        users.add(updatedUser);
        currentUser = updatedUser;
        showPopup("Profile Updated", 
                  "Name: " + newName + "\n" +
                  "User ID: " + currentUser.getUserId() + "\n" +
                  "Email: " + newEmail);
    }

    // Delete profile for normal users
    private void deleteProfile() {
        try {
            System.out.println("\n=== DELETE PROFILE ===");
            System.out.println("WARNING: This will delete your account and all associated data!");
            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine().toLowerCase();
            if (confirmation.isEmpty()) throw new IllegalArgumentException("Response cannot be empty!");
            if (!confirmation.equals("yes") && !confirmation.equals("no")) 
                throw new IllegalArgumentException("Please enter 'yes' or 'no'!");
            
            if (confirmation.equals("yes")) {
                reservations.removeIf(r -> r.getUserId().equals(currentUser.getUserId()));
                complaints.removeIf(c -> c.getUsername().equals(currentUser.getName()));
                String deletedUser = currentUser.getName();
                users.remove(currentUser);
                currentUser = null;
                showPopup("Profile Deleted", "User '" + deletedUser + "' has been deleted.");
                displayMainMenu();
            } else {
                showPopup("Deletion Cancelled", "Profile deletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}