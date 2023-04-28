package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private Map<String,Order> orderMap = new HashMap<>();
    private Map<String,DeliveryPartner> partnerMap = new HashMap<>();
    private Map<String,String> orderPartnerMap = new HashMap<>();
    private Map<String, List<String>> partnerOrderMap = new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(DeliveryPartner partner) {
        partnerMap.put(partner.getId(),partner);
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orderMap.containsKey(orderId)){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId)){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPartnerMap.put(orderId,partnerId);
        if(partnerOrderMap.containsKey(partnerId)){
            List<String> oldList = partnerOrderMap.get(partnerId);
            oldList.add(orderId);
            partnerOrderMap.put(partnerId,oldList);
        }
        else {
            List<String> newList = new ArrayList<>();
            newList.add(orderId);
            partnerOrderMap.put(partnerId,newList);
        }

    }

    public Map<String, String> getAllOrderPartnerMappings() {
        return orderPartnerMap;
    }

    public List<String> getAllOrderForPartner(String partnerId) {
        if(partnerOrderMap.containsKey(partnerId)){
            return partnerOrderMap.get(partnerId);
        }
        else return new ArrayList<>();
    }

    public List<String> getAllOrders() {
        List<String> allOrders = (List<String>) orderMap.keySet();
        return allOrders;
    }
    public List<String> getAllAssignedOrders(){
        List<String> allOrders = (List<String>) orderPartnerMap.keySet();
        return allOrders;

    }

    public void deleteOrderPartnerPair(String orderId) {
        orderPartnerMap.remove(orderId);
    }

    public void deletePartnerOrderPair(String partnerId) {
        partnerOrderMap.remove(partnerId);
    }

    public void deletePartner(String partnerId) {
        partnerMap.remove(partnerId);
    }

    public void deleteOrder(String orderId) {
        orderMap.remove(orderId);
    }

    public String getPartnerForOrder(String orderId) {
        return orderPartnerMap.get(orderId);
    }

    public void deleteOrderForPartner(String partnerId, String orderId) {
        List<String> orderIds = partnerOrderMap.get(partnerId);
        orderIds.remove(orderId);
        partnerOrderMap.put(partnerId,orderIds);
    }
}
