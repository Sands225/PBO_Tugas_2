package validations;

import models.Customer;
import java.util.regex.Pattern;

public class CustomerValidation {
    // Precompiled regex patterns
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z\\-' ]{0,49}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");

    public static boolean isCustomerValid(Customer customer) {
        return isNameValid(customer.getName()) &&
                isEmailValid(customer.getEmail()) &&
                isPhoneValid(customer.getPhone());
    }

    public static boolean isNameValid(String name) {
        if (name == null) return false;
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isEmailValid(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isPhoneValid(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
}
