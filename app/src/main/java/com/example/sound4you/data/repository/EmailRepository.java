package com.example.sound4you.data.repository;

import android.content.Context;
import com.example.sound4you.data.dao.EmailDao;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.data.model.Email;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class EmailRepository {
    private EmailDao emailDao;

    public EmailRepository(Context context) {
        emailDao = ApiClient.getClient().create(EmailDao.class);
    }

    public void sendCode(Email email, Callback<Map<String, String>> callback) {
        Call<Map<String, String>> call = emailDao.sendVerificationCode(email);
        call.enqueue(callback);
    }

    public void verifyCode(Email email, Callback<Map<String, String>> callback) {
        Call<Map<String, String>> call = emailDao.verifyCode(email);
        call.enqueue(callback);
    }
}
