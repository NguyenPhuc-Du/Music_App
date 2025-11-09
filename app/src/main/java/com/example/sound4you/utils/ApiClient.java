package com.example.sound4you.utils;

import retrofit2.Retrofit;

// Giup chuyen doi giua json va object trong java
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.10:3000/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getService() {
        // Nếu apiService chưa được tạo (còn null)
        if (apiService == null) {
            // Thì gọi getClient() để tạo nó
            apiService = getClient().create(ApiService.class);
        }
        // Trả về service đã được tạo
        return apiService;
    }
}
