package com.unina.natour.controllers.utils;

import android.location.Address;

public class AddressUtils {

    public static String getAddressName(Address address) {
        if (address == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        int n = address.getMaxAddressLineIndex();
        for (int i = 0; i <= n; i++) {
            if (i != 0) stringBuilder.append(", ");
            stringBuilder.append(address.getAddressLine(i));
        }

        return stringBuilder.toString();
    }

}
