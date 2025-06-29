package validations;

import models.Customer;
import java.util.regex.Pattern;

public class CustomerValidation {
    // Precompiled regex patterns
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z\\-' ]{0,49}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");

    public static void isCustomerValid(Customer customer) {
        if (!isNameValid(customer.getName())) {
            throw new IllegalArgumentException("Invalid name: must contain only letters, dashes, and spaces (max 50 chars).");
        }
        if (!isEmailValid(customer.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!isPhoneValid(customer.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number: must contain 10 to 15 digits.");
        }
    }

    public static boolean isNameValid(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isEmailValid(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null.");
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isPhoneValid(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null.");
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
}
