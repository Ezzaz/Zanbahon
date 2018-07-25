package com.orionitinc.uber.Common;

import com.orionitinc.uber.Remote.IGoogleAPI;
import com.orionitinc.uber.Remote.RetrofitClient;

public class Common {
    public static final String baseURL="https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient (baseURL).create (IGoogleAPI.class);
    }
}
