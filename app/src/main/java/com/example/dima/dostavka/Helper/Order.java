package com.example.dima.dostavka.Helper;

public class Order {

    private String id;
   private String nameCustomer;
   private String addressCustomer;
   private String coastOrder;
   private String numberOfAddress;
   private String addressForDriver;




    public Order(String name, String address, String coast, String numOfAddress, String iD,
                 String addressForDriveR){
        id = iD;
        nameCustomer = name;
        addressCustomer = address;
        coastOrder = coast;
        numberOfAddress = numOfAddress;
        addressForDriver = addressForDriveR;

    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public String getNumberOfAddress() { return numberOfAddress; }

    public String getAddressCustomer() {
        return addressCustomer;
    }

    public String getCoastOrder() {
        return coastOrder;
    }

    public String getAddressForDriver() { return addressForDriver; }

    public String getIdOrder() {
        return id;
    }
}
