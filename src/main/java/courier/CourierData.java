package courier;

import java.util.Random;



public class CourierData {
    private final String existingLogin;
    private final String nonExistentLogin;
    private final String existingPassword;
    private final String nonExistentPassword;
    private final String firstName;

    public CourierData() {
        this.existingLogin = randomLoginOrPass(9);
        this.nonExistentLogin = randomLoginOrPass(9) + "kek";
        this.existingPassword = "existPass";
        this.nonExistentPassword = randomLoginOrPass(8);
        this.firstName = "firstName";
    }

    public static String randomLoginOrPass(int length) {
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String getExistingLogin() {
        return existingLogin;
    }

    public String getNonExistLogin() {
        return nonExistentLogin;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public String getNonExistPassword() {
        return nonExistentPassword;
    }

    public String getFirstName() {
        return firstName;
    }
}