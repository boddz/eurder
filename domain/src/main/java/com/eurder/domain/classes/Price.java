package com.eurder.domain.classes;

import java.util.Objects;

public class Price {
    private double price;
    private String denomination;

    public Price() {
    }

    public Price(double price, String denomination) {
        this.price = price;
        this.denomination = denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Double.compare(price1.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, denomination);
    }

    public double getPrice() {
        return price;
    }
}
