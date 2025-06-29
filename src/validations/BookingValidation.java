package validations;

import models.Booking;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BookingValidation {

    public static void isBookingValid(Booking booking) {
        if (!isCustomerIdValid(booking.getCustomer())) {
            throw new IllegalArgumentException("Customer ID must be a positive integer.");
        }
        if (!isRoomTypeIdValid(booking.getRoom_type())) {
            throw new IllegalArgumentException("Room type ID must be a positive integer.");
        }
        if (!areDatesValid(booking.getCheckin_date(), booking.getCheckout_date())) {
            throw new IllegalArgumentException("Check-in date must be today or later and before or equal to check-out date.");
        }
        if (!isPriceValid(booking.getPrice())) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }
        if (!isFinalPriceValid(booking.getFinal_price())) {
            throw new IllegalArgumentException("Final price must be non-negative.");
        }
        if (!isPaymentStatusValid(booking.getPayment_status())) {
            throw new IllegalArgumentException("Payment status is required.");
        }
        if (!isBooleanInt(booking.getHas_checkedin())) {
            throw new IllegalArgumentException("Check-in flag must be 0 or 1.");
        }
        if (!isBooleanInt(booking.getHas_checkedout())) {
            throw new IllegalArgumentException("Check-out flag must be 0 or 1.");
        }
    }

    public static boolean isCustomerIdValid(int id) {
        return id > 0;
    }

    public static boolean isRoomTypeIdValid(int id) {
        return id > 0;
    }

    public static boolean areDatesValid(String checkin, String checkout) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkin);
            LocalDate checkOutDate = LocalDate.parse(checkout);
            return !checkInDate.isBefore(LocalDate.now()) && !checkInDate.isAfter(checkOutDate);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isPriceValid(int price) {
        return price >= 0;
    }

    public static boolean isFinalPriceValid(int finalPrice) {
        return finalPrice >= 0;
    }

    public static boolean isPaymentStatusValid(String status) {
        return status != null && !status.trim().isEmpty();
    }

    public static boolean isBooleanInt(int value) {
        return value == 0 || value == 1;
    }
}
