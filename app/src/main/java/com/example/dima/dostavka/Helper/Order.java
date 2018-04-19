package com.example.dima.dostavka.Helper;

public class Order {

    private String id;
   private String nameCastomer;
   private String townCastomer;
   private String coastOrder;
   private String numberOfAddress;


    public Order(String name, String town, String coast, String adress, String iD){
        id = iD;
        nameCastomer = name;
        townCastomer = town;
        coastOrder = coast;
        numberOfAddress = adress;
    }

    public String getNameCastomer() {
        return nameCastomer;
    }

    public String getNumberOfAddress() { return numberOfAddress; }

    public String getTownCastomer() {
        return townCastomer;
    }


    public String getCoastOrder() {
        return coastOrder;
    }


    public String getIdOrder() {
        return id;
    }
}
