package trivia;

public enum GameCategoryEnum {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock");

    private final String category;

    GameCategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
