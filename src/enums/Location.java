package enums;

public enum Location {
    SYD("Sydney"),
    MEL("Melbourne"),
    ADL("Adelaide"),
    ASP("Alice Springs"),
    BRI("Brisbane"),
    DAR("Darwin"),
    PER("Perth");

    private final String fullName;

    Location(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName;

    /**
     * Resolves user input (abbreviation or full name) to a Location enum.
     * Case-insensitive matching.
     *
     * @param input the user input string
     * @return the matching Location
     * @throws IllegalArgumentException if no match is found
     */
    public static Location fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Location can not be empty.");
        }

        String trimmedString = input.trim().toUpperCase();

        // matching if input is equals to enum name like ex. SYD
        for (Location l : values()) {
            if (l.name().equals(trimmedString)) {
                return l;
            }
        }

        // the opposite site if input is equals to full name
        for (Location l : values()) {
            if (l.getFullName.equals(trimmedString)) {
                return l;
            }
        }

        throw new IllegalArgumentException("Unknown location " + input.trim());
    }

    @Override
    public String toString() {
        return fullName + " (" + name() + ")";
    }
}
