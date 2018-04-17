package com.example.dima.dostavka.Helper;

public class Order {


     String nameCastomer;
     String townCastomer;
     String coastOrder;

    public Order(String name, String town, String coast){
        nameCastomer = name;
        townCastomer = town;
        coastOrder = coast;
    }

    public String getNameCastomer() {
        return nameCastomer;
    }



    public String getTownCastomer() {
        return townCastomer;
    }


    public String getCoastOrder() {
        return coastOrder;
    }


}
