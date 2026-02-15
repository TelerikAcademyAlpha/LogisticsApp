package util;

import enums.Location;
import exception.InvalidInputException;

public class InputValidator {
    private InputValidator(){}

    public static String requireNonEmpty(String value, String fieldName){
        if (value == null || value.trim().isEmpty()) throw new InvalidInputException(fieldName + " cannot be empty.");
        return value.trim();
    }

    public static double requirePositiveDouble(String input, String fieldName){
        try {
            double value = Double.parseDouble(input.trim());
            if (value <= 0) throw new InvalidInputException(fieldName + "must be positive.");
            return value;
        } catch (NumberFormatException e){
            throw new InvalidInputException(fieldName + " must be a valid number");
        }
    }

    public static int requirePositiveInt(String input, String fieldName){
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) throw new InvalidInputException(fieldName + " must be positive.");
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a valid integer.");
        }
    }

    public static int parseInt(String input, String fieldName){
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a valid integer.");
        }
    }

    public static Location requireValidLocation(String input) {
        return Location.fromString(input);
    }
}
