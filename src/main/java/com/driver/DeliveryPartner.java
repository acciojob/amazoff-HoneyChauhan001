package com.driver;

public class DeliveryPartner {

    private String id;
    private int numberOfOrders;

    public DeliveryPartner() {
    }

    public DeliveryPartner(String id) {
        this.id = id;
        this.numberOfOrders = 0;
    }
    public DeliveryPartner(String id, int numberOfOrders){
        this.id = id;
        this.numberOfOrders = numberOfOrders;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfOrders(){
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
    public void setId(String id){
        this.id = id;
    }
}