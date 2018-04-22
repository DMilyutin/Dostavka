package com.example.dima.dostavka.Helper;

public class Driver {
    private String id;
    private String balanceDriver;
    private String carDriver;
    private String carNumber;
    private String lastNameDriver;
    private String nameDriver;
    private String telephonNamber;

    public Driver(String id, String nameDriver,String lastNameDriver,  String carDriver,
                  String carNumber, String telephonNamber ,String balanceDriver) {
        this.id = id;
        this.nameDriver = nameDriver;
        this.lastNameDriver = lastNameDriver;
        this.carDriver = carDriver;
        this.carNumber = carNumber;
        this.telephonNamber = telephonNamber;
        this.balanceDriver = balanceDriver;

        }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalanceDriver() {
        return balanceDriver;
    }

    public void setBalanceDriver(String balanceDriver) {
        this.balanceDriver = balanceDriver;
    }

    public String getCarDriver() {
        return carDriver;
    }

    public void setCarDriver(String carDriver) {
        this.carDriver = carDriver;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getLastNameDriver() {
        return lastNameDriver;
    }

    public void setLastNameDriver(String lastNameDriver) {
        this.lastNameDriver = lastNameDriver;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getTelephonNamber() {
        return telephonNamber;
    }

    public void setTelephonNamber(String telephonNamber) {
        this.telephonNamber = telephonNamber;
    }
}
