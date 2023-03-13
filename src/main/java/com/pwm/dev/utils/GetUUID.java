package com.pwm.dev.utils;

import java.util.UUID;

public class GetUUID {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
