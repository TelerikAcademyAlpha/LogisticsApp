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
}
