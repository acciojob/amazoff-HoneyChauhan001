package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> partnerOpt = orderRepository.getPartnerById(partnerId);
        if(orderOpt.isPresent() && partnerOpt.isPresent()){
            DeliveryPartner p = partnerOpt.get();
            Integer initialOrders = p.getNumberOfOrders();
            initialOrders++;
            p.setNumberOfOrders(initialOrders);
            orderRepository.addPartner(p);
            orderRepository.addOrderPartnerPair(orderId,partnerId);
        }

    }

    public Order getOrderById(String orderId) {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        if(orderOpt.isPresent()){
            return orderOpt.get();
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        Optional<DeliveryPartner> partnetOpt = orderRepository.getPartnerById(partnerId);
        if(partnetOpt.isPresent()){
            return partnetOpt.get();
        }
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Optional<DeliveryPartner> partnerOpt = orderRepository.getPartnerById(partnerId);
        if(partnerOpt.isPresent()){
            return partnerOpt.get().getNumberOfOrders();
        }
        return null;
    }

    public List<String> getOrderByPartnerId(String partnerId) {
//        Map<String ,String> orderPartnerMap = orderRepository.getAllOrderPartnerMappings();
//        List<String> orderIds = new ArrayList<>();
//        for(var entry : orderPartnerMap.entrySet()){
//            if(entry.getValue().equals(partnerId)){
//                orderIds.add(entry.getKey());
//            }
//        }
//        return orderIds;

        return orderRepository.getAllOrderForPartner(partnerId);

    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        int ans = orderRepository.getAllOrders().size() - orderRepository.getAllAssignedOrders().size();
        if(ans>0)return ans;
        return null;

    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int currTime = TimeUtils.convertTime(time);
        int ordersLeft = 0;

//        Map<String ,String> orderPartnerMap = orderRepository.getAllOrderPartnerMappings();
//        for(var entry : orderPartnerMap.entrySet()){
//            if(entry.getValue().equals(partnerId)){
//                int deliveryTime = orderRepository.getOrderById(entry.getKey()).get().getDeliveryTime();
//                if(deliveryTime>currTime){
//                    ordersLeft++;
//                }
//            }
//        }

        List<String> orderIds = orderRepository.getAllOrderForPartner(partnerId);
        for(String orderId : orderIds){
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if(deliveryTime>currTime){
                ordersLeft++;
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int max = 0;

//        Map<String ,String> orderPartnerMap = orderRepository.getAllOrderPartnerMappings();
//        for(var entry : orderPartnerMap.entrySet()){
//            if(entry.getValue().equals(partnerId)){
//                int deliveryTime = orderRepository.getOrderById(entry.getKey()).get().getDeliveryTime();
//                if(deliveryTime>max){
//                    max = deliveryTime;
//                }
//            }
//        }

        List<String> orderIds = orderRepository.getAllOrderForPartner(partnerId);
        for(String orderId : orderIds){
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if(deliveryTime>max){
                max = deliveryTime;
            }
        }

        return TimeUtils.convertTime(max);
    }

    public void deletePartnerById(String partnerId) {

//        Map<String ,String> orderPartnerMap = orderRepository.getAllOrderPartnerMappings();
//        for(var entry : orderPartnerMap.entrySet()){
//            if(entry.getValue().equals(partnerId)){
//                orderRepository.deleteOrderPartnerPair(entry.getKey());
//            }
//        }
//        orderRepository.deletePartner(partnerId);

        List<String> orderIds = orderRepository.getAllOrderForPartner(partnerId);
        for(String orderId : orderIds){
            orderRepository.deleteOrderPartnerPair(orderId);
        }
        orderRepository.deletePartnerOrderPair(partnerId);
        orderRepository.deletePartner(partnerId);
    }

    public void deleteOrderById(String orderId) {

//        orderRepository.deleteOrder(orderId);
//        orderRepository.deletePartnerOrderPair(orderId);

        String partnerId = orderRepository.getPartnerForOrder(orderId);
        if(Objects.nonNull(partnerId)){
            DeliveryPartner p = orderRepository.getPartnerById(partnerId).get();
            Integer initialOrders = p.getNumberOfOrders();
            initialOrders--;
            p.setNumberOfOrders(initialOrders);
            orderRepository.addPartner(p);
            orderRepository.deleteOrderForPartner(partnerId,orderId);

        }
        orderRepository.deleteOrder(orderId);
        orderRepository.deleteOrderPartnerPair(orderId);
    }
}
