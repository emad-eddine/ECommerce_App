package com.kichou.imad.e_commerceapp.model.clientModel;

public interface ClientData {

    String getClientFirstName();
    String getClientLastName();
    String getClientPhone();
    String getClientPassword();

    int isValid();

    int isLoginValid();

}
