package com.kichou.imad.e_commerceapp.model.clientModel;


import android.text.TextUtils;


public class Client implements ClientData {

    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private String clientPassword;

    public Client()
    {

    }

    public Client(String clientFirstName, String clientLastName, String clientPhone, String clientPassword) {
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientPhone = clientPhone;
        this.clientPassword = clientPassword;
    }

    public Client(String phone , String password)
    {
        this.clientPhone = phone;
        this.clientPassword = password;
    }

    @Override
    public String getClientFirstName() {
        return clientFirstName;
    }

    @Override
    public String getClientLastName() {
        return clientLastName;
    }

    @Override
    public String getClientPhone() {
        return clientPhone;
    }

    @Override
    public String getClientPassword() {
        return clientPassword;
    }

    @Override
    public int isValid() {
        // 0. Check for fields empty
        // 1. Check for Password > 6
        // 2 if phone more than 10 degit
        if(TextUtils.isEmpty(getClientFirstName()) || TextUtils.isEmpty(getClientLastName())
                || TextUtils.isEmpty(getClientPhone()) || TextUtils.isEmpty(getClientPassword()))
            return  0;
        else if(getClientPassword().length()<=6)
            return 1;
        else if(getClientPhone().length() > 10)
            return 2;
        else
            return -1;
    }

    @Override
    public int isLoginValid() {
        // 0. Check for fields empty
        // 1. Check for Password > 6
        // 2 if phone more than 10 degit
        // -1 else
        if(TextUtils.isEmpty(getClientPhone()) || TextUtils.isEmpty(getClientPassword()))
            return  0;
        else if(getClientPassword().length()<=6)
            return 1;
        else if(getClientPhone().length() > 10)
            return 2;
        return -1;
    }


}
