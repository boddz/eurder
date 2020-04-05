package com.eurder.service;

import com.eurder.domain.classes.Customer;
import com.eurder.domain.classes.ItemGroup;
import com.eurder.domain.classes.ItemGroupWithadress;
import com.eurder.domain.classes.Order;
import com.eurder.domain.dto.OrderDto;
import com.eurder.domain.dto.ReportDto;
import com.eurder.domain.mapper.ItemGroupMapper;
import com.eurder.domain.mapper.OrderMapper;
import com.eurder.domain.repository.CustomerRepository;
import com.eurder.domain.repository.ItemRepository;
import com.eurder.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemGroupMapper itemGroupMapper;


    @Autowired
    public OrderService(OrderMapper orderMapper, OrderRepository orderRepository, ItemRepository itemRepository, CustomerRepository customerRepository, ItemGroupMapper itemGroupMapper) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.itemGroupMapper = itemGroupMapper;
    }

    public OrderDto placeOrder(OrderDto orderDto, String username) {
        if (hasAnyEmptyFields(orderDto)) {
            throw new NotEverythingFilledInExeption("fill in everything");
        }

        Customer customerThatOrdered = customerRepository.getCustomerBasedOnName(username);
        Order order = orderMapper.toOrder(orderDto, customerThatOrdered);
        customerThatOrdered.getReportDto().addOrder(order);
        orderRepository.placeOrder(order);
        return orderDto;
    }

    public Order placeExistingOrder(int orderID, String username) {
        Order order = orderRepository.getOrderList().stream().filter(x -> x.getId() == orderID).findFirst().orElse(null);
        Order updatedOrder = orderMapper.orderUpdateToCurrentItems(order);
//        OrderDto updatedOrderDto = orderMapper.toOrderDto(updatedOrder);
        Customer customerThatOrdered = customerRepository.getCustomerBasedOnName(username);
        customerThatOrdered.getReportDto().addOrder(updatedOrder);
        orderRepository.placeOrder(updatedOrder);
        return updatedOrder;
    }


    public boolean hasAnyEmptyFields(OrderDto orderDto) {
        return orderDto.getItemGroupDtoList().isEmpty();
    }


    public OrderRepository getOrderRepository() {
        return orderRepository;
    }


    public List<OrderDto> getAllOrders() {
        ArrayList<OrderDto> returnList = new ArrayList<>();
        for (Order order : orderRepository.getOrderList()) {
            returnList.add(orderMapper.toOrderDto(order));
        }
        return returnList;
    }

    public ReportDto getOrderReportByID(int id) {
        return Objects.requireNonNull(getCustomerById(id).getReportDto());
    }

    public List<ItemGroupWithadress> getToShipToday() {
        List<ItemGroupWithadress> itemGroupWithadressArrayList = new ArrayList<>();

        for (Order order : orderRepository.getOrderList()) {
            for (ItemGroup itemGroup : order.getItemGroupList()) {
                itemGroupWithadressArrayList.add(itemGroupMapper.toItemGroupWithadress(itemGroup, order.getCustomer()));
            }
        }
        return itemGroupWithadressArrayList.stream().filter(x -> x.getItemGroup().getShippingdate().equals(LocalDate.now())).collect(Collectors.toList());
    }

    public Customer getCustomerById( int id) {
        return customerRepository.getCustomerList().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public Order getOrder( int orderID) {
        return orderRepository.getOrderList().stream().filter(x -> x.getId() == orderID).findFirst().orElse(null);
    }

    public Customer getCustomer(Principal principal) {
        return customerRepository.getCustomerList().stream().filter(x -> x.getFirstname().equals(principal.getName())).findFirst().orElse(null);
    }
}
