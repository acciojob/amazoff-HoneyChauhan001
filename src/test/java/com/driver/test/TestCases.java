package com.driver.test;

import com.driver.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = Application.class)
public class TestCases {

    OrderRepository orderRepository;
    OrderService orderService;
    OrderController orderController;

    @BeforeEach
    public void setup(){
        orderRepository = new OrderRepository();
        orderService = new OrderService(orderRepository);
        orderController = new OrderController(orderService);
    }

    @Test
    public void expectToAddOrder(){
        Order order = new Order("123","12:23");
        orderController.addOrder(order);

        ResponseEntity<List<String>> response = orderController.getAllOrders();
        assertEquals(1,response.getBody().size());
        assertEquals("123",response.getBody().get(0));
    }

    @Test
    public void expectToAddPartner(){
        ResponseEntity<String> response = orderController.addPartner("12");
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("New delivery partner added successfully",response.getBody());
        Optional<DeliveryPartner> opt = orderRepository.getPartnerById("12");
        assertTrue(opt.isPresent());
        assertFalse(opt.isEmpty());
    }

    @Test
    public void expectToAddOrderPartnerSuccessfully(){
        Order order = new Order("123","12:23");
        orderController.addOrder(order);
        orderController.addPartner("12");
        orderController.addOrderPartnerPair("123","12");
        ResponseEntity<List<String>> response = orderController.getOrdersByPartnerId("12");
        assertEquals(1,response.getBody().size());
        assertEquals("123",response.getBody().get(0));
    }

    @Test
    public void expectToDeletePartner(){
        Order order = new Order("123","12:23");
        orderController.addOrder(order);
        orderController.addPartner("12");
        orderController.addOrderPartnerPair("123","12");
        ResponseEntity response = orderController.deletePartnerById("12");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Optional<DeliveryPartner> opt = orderRepository.getPartnerById("12");
        String partnerIdForOrder = orderRepository.getPartnerForOrder("123");
        assertNull(partnerIdForOrder);
        assertTrue(opt.isEmpty());
        assertFalse(opt.isPresent());
    }

}