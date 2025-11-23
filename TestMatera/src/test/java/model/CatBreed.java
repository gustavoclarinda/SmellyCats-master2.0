package model;

public class CatBreed {
    private final String breed;
    private final String country;
    private final String origin;
    private final String coat;
    private final String pattern;

    public CatBreed(String breed, String country, String origin, String coat, String pattern) {
        this.breed = breed;
        this.country = country;
        this.origin = origin;
        this.coat = coat;
        this.pattern = pattern;
    }

    public String getBreed() {
        return breed;
    }

    public String getCountry() {
        return country;
    }

    public String getOrigin() {
        return origin;
    }

    public String getCoat() {
        return coat;
    }

    public String getPattern() {
        return pattern;
    }
}
