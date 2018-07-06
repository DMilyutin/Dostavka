package com.milyutin.dima.dostavka.Helper;

public class Order {

    private String id;
    private String idForWorkBalashiha;
    private String nameCustomer;
    private String addressCustomer;
    private String coastOrder;
    private String numberOfAddress;
    private String addressForDriver;
    private String namesForDriver;
    private String phonesForDriver;
    private String timeFilingCustomer;






    public Order(String name, String address, String coast, String numOfAddress, String iD, String idForWorkBalashiha1) {
        id = iD;
        nameCustomer = name;
        addressCustomer = address;
        coastOrder = coast;
        numberOfAddress = numOfAddress;
        idForWorkBalashiha = idForWorkBalashiha1;
    }

    public String getTimeFilingCustomer() { return timeFilingCustomer; }

    public void setTimeFilingCustomer(String timeFilingCustomer) { this.timeFilingCustomer = timeFilingCustomer; }

    public String getIdForWorkBalashiha() {
        return idForWorkBalashiha;
    }

    public void setIdForWorkBalashiha(String idForWorkBalashiha) {
        this.idForWorkBalashiha = idForWorkBalashiha;
    }
    public void setNamesForDriver(String namesForDriver) {
        this.namesForDriver = namesForDriver;
    }

    public void setPhonesForDriver(String phonesForDriver) {
        this.phonesForDriver = phonesForDriver;
    }

    public String getNamesForDriver() {
        return namesForDriver;
    }

    public String getPhonesForDriver() {
        return phonesForDriver;
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
