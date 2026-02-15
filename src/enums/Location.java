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

    public String getFullName() {
        return fullName;
    }

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

        String trimmedString = input.trim();

        for (Location l : values()) {
            if (l.name().equalsIgnoreCase(trimmedString) ||
                    l.getFullName().equalsIgnoreCase(trimmedString)) {
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
