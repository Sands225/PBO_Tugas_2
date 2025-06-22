package validations;

import models.Customer;
import java.util.regex.Pattern;

public class CustomerValidation {

    public static boolean isCustomerValid(Customer customer) {
        return isNameValid(customer.getName()) &&
                isEmailValid(customer.getEmail()) &&
                isPhoneValid(customer.getPhone());
    }

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isEmailValid(String email) {
        if (email == null) return false;

        // Regex sederhana untuk validasi email
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean isPhoneValid(String phone) {
        if (phone == null) return false;

        // Nomor telepon hanya angka dan minimal 10 digit
        String phoneRegex = "\\d{10,}";
        return Pattern.matches(phoneRegex, phone);
    }
}
