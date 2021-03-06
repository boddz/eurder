package com.eurder.domain.dto;

import com.eurder.domain.classes.Price;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class OrderDto {
    private List<ItemGroupDto> itemGroupDtoList;
    private Price totalPrice;

    public OrderDto() {
    }

    public OrderDto(List<ItemGroupDto> itemGroupList) {
        this.itemGroupDtoList = new ArrayList<>(itemGroupList);
        this.totalPrice = calculateTotalPrice();
    }

    public List<ItemGroupDto> getItemGroupDtoList() {
        return itemGroupDtoList;
    }


    public Price getTotalPrice() {
        return totalPrice;
    }


    public Price calculateTotalPrice() {
        double price = 0;
        for (ItemGroupDto itemGroupDto : itemGroupDtoList) {
            price += itemGroupDto.getPrice();
        }
        Price price1 = new Price(price, "eur");
        this.totalPrice =price1;
        return price1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(itemGroupDtoList, orderDto.itemGroupDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroupDtoList, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "itemGroupDtoList=" + itemGroupDtoList +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public void setItemGroupDtoList(List<ItemGroupDto> itemGroupDtoList) {
        this.itemGroupDtoList = itemGroupDtoList;
    }

    public void setTotalPrice(Price totalPrice) {
        this.totalPrice = totalPrice;
    }
}
