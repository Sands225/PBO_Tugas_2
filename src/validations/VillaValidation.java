package validations;

import models.Villa;

public class VillaValidation {

    public static void isVillaValid(Villa villa) {
        if (!isNameValid(villa.getName())) {
            throw new IllegalArgumentException("Villa name is required and must be under 100 characters.");
        }
        if (!isDescriptionValid(villa.getDescription())) {
            throw new IllegalArgumentException("Villa description must not exceed 1000 characters.");
        }
        if (!isAddressValid(villa.getAddress())) {
            throw new IllegalArgumentException("Villa address is required and must be under 255 characters.");
        }
    }

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    public static boolean isDescriptionValid(String description) {
        return description == null || description.length() <= 1000;
    }

    public static boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 255;
    }
}
