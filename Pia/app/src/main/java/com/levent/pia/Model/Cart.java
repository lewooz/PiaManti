package com.levent.pia.Model;

public class Cart {

    private String date, name, porsiyon, price, quantity, time, sarimsakdurumu, sosdurumu,teslimat;

    public  Cart(){}

    public Cart(String date, String name, String porsiyon, String price, String quantity, String time, String sarimsakdurumu, String sosdurumu, String teslimat) {
        this.date = date;
        this.name = name;
        this.porsiyon = porsiyon;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.sarimsakdurumu = sarimsakdurumu;
        this.sosdurumu = sosdurumu;
        this.teslimat = teslimat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPorsiyon() {
        return porsiyon;
    }

    public void setPorsiyon(String porsiyon) {
        this.porsiyon = porsiyon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSarimsakdurumu() {
        return sarimsakdurumu;
    }

    public void setSarimsakdurumu(String sarimsakdurumu) {
        this.sarimsakdurumu = sarimsakdurumu;
    }

    public String getSosdurumu() {
        return sosdurumu;
    }

    public void setSosdurumu(String sosdurumu) {
        this.sosdurumu = sosdurumu;
    }

    public String getTeslimat() {
        return teslimat;
    }

    public void setTeslimat(String teslimat) {
        this.teslimat = teslimat;
    }
}
