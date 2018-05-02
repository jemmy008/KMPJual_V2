package com.getective.kmpjual;

import com.getective.kmpjual.Model.Produk;
import com.getective.kmpjual.Model.User;

import java.util.List;

public class StaticList {
    private static List<Produk> listProduk;
    private static User user;
    private static Produk produk;


    public static List<Produk> getListProduk() {
        return listProduk;
    }

    public static void setListProduk(List<Produk> listProduk) {
        StaticList.listProduk = listProduk;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        StaticList.user = user;
    }

    public static Produk getProduk() {
        return produk;
    }

    public static void setProduk(Produk produk) {
        StaticList.produk = produk;
    }
}
