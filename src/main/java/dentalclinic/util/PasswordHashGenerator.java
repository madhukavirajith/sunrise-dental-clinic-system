package dentalclinic.util;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        String plainTextPassword = "password123"; // change this if you like

        String hash = PasswordUtil.hashPassword(plainTextPassword);

        System.out.println("Plain text password: " + plainTextPassword);
        System.out.println("Hashed value to paste into staff_user.password_hash:");
        System.out.println(hash);
    }
}