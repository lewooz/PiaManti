package com.levent.pia.Model;

public class AdminOrders {

    private String username, telno, toplamucret, adres, siparistarihi,siparisdurumu,mail, odemesekli, siparisnotu, mahalle,siparissayi;

    public AdminOrders()
    {

    }

    public AdminOrders(String username, String telno, String toplamucret, String adres, String siparistarihi, String siparisdurumu, String mail, String odemesekli, String siparisnotu, String mahalle, String siparissayi) {
        this.username = username;
        this.telno = telno;
        this.toplamucret = toplamucret;
        this.adres = adres;
        this.siparistarihi = siparistarihi;
        this.siparisdurumu = siparisdurumu;
        this.mail = mail;
        this.odemesekli = odemesekli;
        this.siparisnotu = siparisnotu;
        this.mahalle = mahalle;
        this.siparissayi = siparissayi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getToplamucret() {
        return toplamucret;
    }

    public void setToplamucret(String toplamucret) {
        this.toplamucret = toplamucret;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getSiparistarihi() {
        return siparistarihi;
    }

    public void setSiparistarihi(String siparistarihi) {
        this.siparistarihi = siparistarihi;
    }

    public String getSiparisdurumu() {
        return siparisdurumu;
    }

    public void setSiparisdurumu(String siparisdurumu) {
        this.siparisdurumu = siparisdurumu;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getOdemesekli() {
        return odemesekli;
    }

    public void setOdemesekli(String odemesekli) {
        this.odemesekli = odemesekli;
    }

    public String getSiparisnotu() {
        return siparisnotu;
    }

    public void setSiparisnotu(String siparisnotu) {
        this.siparisnotu = siparisnotu;
    }

    public String getMahalle() {
        return mahalle;
    }

    public void setMahalle(String mahalle) {
        this.mahalle = mahalle;
    }

    public String getSiparissayi() {
        return siparissayi;
    }

    public void setSiparissayi(String siparissayi) {
        this.siparissayi = siparissayi;
    }
}
