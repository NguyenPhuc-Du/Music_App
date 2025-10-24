package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Email;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmailDao {
    @POST("api/email/send-code")
    Call<Map<String, String>> sendVerificationCode(@Body Email email);

    @POST("api/email/verify-code")
    Call<Map<String, String>> verifyCode(@Body Email email);
}
