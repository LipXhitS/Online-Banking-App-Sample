public class User {
    private final String userId;
    private String username;
    private String userNumber;
    private int userPIN;

    public User(String userId, String username, String userNumber, int userPin) {
        this.userId = userId;
        this.username = username;
        this.userNumber = userNumber;
        this.userPIN = userPin;
    }

    public String userIdGetter() {
        return this.userId;
    }

    public String usernameGetter() {
        return this.username;
    }

    public void usernameSetter(String username) {
        this.username = username;
    }

    public String userNumberGetter() {
        return this.userNumber;
    }

    public void userNumberSetter(String userNumber) {
        this.userNumber = userNumber;
    }

    public int userPINGetter() {
        return this.userPIN;
    }

    public void userPINSetter(int userPIN) {
        this.userPIN = userPIN;
    }
}