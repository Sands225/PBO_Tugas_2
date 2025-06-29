package validations;

import models.Room;

public class RoomValidation {

    public static void isRoomValid(Room room) {
        if (!isVillaIdValid(room.getVilla())) {
            throw new IllegalArgumentException("Villa ID must be a positive integer.");
        }
        if (!isNameValid(room.getName())) {
            throw new IllegalArgumentException("Room name is required and must be under 100 characters.");
        }
        if (!isQuantityValid(room.getQuantity())) {
            throw new IllegalArgumentException("Room quantity must be a non-negative integer.");
        }
        if (!isCapacityValid(room.getCapacity())) {
            throw new IllegalArgumentException("Room capacity must be a positive integer.");
        }
        if (!isPriceValid(room.getPrice())) {
            throw new IllegalArgumentException("Room price must be a non-negative integer.");
        }
        if (!isBedSizeValid(room.getBed_size())) {
            throw new IllegalArgumentException("Bed size is required and must be under 50 characters.");
        }

        if (!isBooleanInt(room.getHas_desk())) {
            throw new IllegalArgumentException("has_desk must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_ac())) {
            throw new IllegalArgumentException("has_ac must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_tv())) {
            throw new IllegalArgumentException("has_tv must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_wifi())) {
            throw new IllegalArgumentException("has_wifi must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_shower())) {
            throw new IllegalArgumentException("has_shower must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_hotwater())) {
            throw new IllegalArgumentException("has_hotwater must be 0 or 1.");
        }
        if (!isBooleanInt(room.getHas_fridge())) {
            throw new IllegalArgumentException("has_fridge must be 0 or 1.");
        }
    }

    public static boolean isVillaIdValid(int villaId) {
        return villaId > 0;
    }

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    public static boolean isQuantityValid(int quantity) {
        return quantity >= 0;
    }

    public static boolean isCapacityValid(int capacity) {
        return capacity > 0;
    }

    public static boolean isPriceValid(int price) {
        return price >= 0;
    }

    public static boolean isBedSizeValid(String bedSize) {
        return bedSize != null && !bedSize.trim().isEmpty() && bedSize.length() <= 50;
    }

    public static boolean isBooleanInt(int value) {
        return value == 0 || value == 1;
    }
}
