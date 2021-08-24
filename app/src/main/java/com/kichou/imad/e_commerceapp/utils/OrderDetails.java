package com.kichou.imad.e_commerceapp.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetails implements Serializable {

    public ArrayList<String> itemKey =new ArrayList<>();
    public ArrayList<String> itemPrice = new ArrayList<>();
    public ArrayList<String> itemQte = new ArrayList<>();
    public String  orderAmount = "";



}
