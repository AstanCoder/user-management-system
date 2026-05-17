package com.example.contact.domain.valueobject;

import java.util.Objects;

/**
 * Immutable postal address value object.
 */
public final class Address {

    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;

    private Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * Creates an address from optional parts.
     *
     * @param street street line
     * @param city city
     * @param postalCode postal code
     * @param country country
     * @return address or null if all blank
     */
    public static Address createOptional(String street, String city, String postalCode, String country) {
        if (isBlank(street) && isBlank(city) && isBlank(postalCode) && isBlank(country)) {
            return null;
        }
        return new Address(trim(street), trim(city), trim(postalCode), trim(country));
    }

    /**
     * Rehydrates address from persistence.
     *
     * @param street street
     * @param city city
     * @param postalCode postal code
     * @param country country
     * @return address or null
     */
    public static Address fromStored(String street, String city, String postalCode, String country) {
        return createOptional(street, city, postalCode, country);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public String street() {
        return street;
    }

    public String city() {
        return city;
    }

    public String postalCode() {
        return postalCode;
    }

    public String country() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(street, address.street)
                && Objects.equals(city, address.city)
                && Objects.equals(postalCode, address.postalCode)
                && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }
}
