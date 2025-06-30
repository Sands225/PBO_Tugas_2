package validations;

import models.Voucher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class VoucherValidation {

    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Z0-9\\-]{4,20}$");

    public static void isVoucherValid(Voucher voucher) {
        if (!isCodeValid(voucher.getCode())) {
            throw new IllegalArgumentException("Voucher code must be 4–20 characters (uppercase letters, numbers, or dashes).");
        }
        if (!isDescriptionValid(voucher.getDescription())) {
            throw new IllegalArgumentException("Description must not exceed 255 characters.");
        }
        if (!isDiscountValid(voucher.getDiscount())) {
            throw new IllegalArgumentException("Discount must be > 0 and <= 100.");
        }
        if (!areDatesValid(voucher.getStart_date(), voucher.getEnd_date())) {
            throw new IllegalArgumentException("Start and end dates must be valid, and end date must not be before start date.");
        }
    }

    public static boolean isCodeValid(String code) {
        return code != null && CODE_PATTERN.matcher(code.trim()).matches();
    }

    public static boolean isDescriptionValid(String description) {
        return description == null || description.length() <= 255;
    }

    public static boolean isDiscountValid(double discount) {
        return discount > 0 && discount <= 100;
    }

    public static boolean areDatesValid(String start, String end) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(start, formatter);
            LocalDateTime endDate = LocalDateTime.parse(end, formatter);
            return !endDate.isBefore(startDate);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
}
