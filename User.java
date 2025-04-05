public class User {
    private String userId;
    private String password;
    private boolean isAdmin;
    private String name;
    private String email;

    public User(String userId, String password, boolean isAdmin, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.isAdmin = isAdmin;
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public boolean isAdmin() { return isAdmin; }
    public String getEmail() { return email; }
    public boolean verifyPassword(String inputPassword) { return password.equals(inputPassword); }
}