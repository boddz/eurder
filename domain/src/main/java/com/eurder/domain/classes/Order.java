package com.eurder.domain.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final int id;
    private List<ItemGroup> itemGroupList;
    private Price totalPrice;
    private Customer customer;
    private static int counter=1;

    public Order(List<ItemGroup> itemGroupList, Price totalPrice, Customer customer) {
        this.id = counter++;
        this.itemGroupList = new ArrayList<>(itemGroupList);
        this.totalPrice = totalPrice;
        this.customer = customer;
    }

    public List<ItemGroup> getItemGroupList() {
        return itemGroupList;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(itemGroupList, order.itemGroupList) &&
                Objects.equals(totalPrice, order.totalPrice) &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroupList, totalPrice, customer);
    }
}
