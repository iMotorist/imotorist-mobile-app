/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.madushanka.imotorist.controllers;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.madushanka.imotorist.Utils;
import com.madushanka.imotorist.entities.ApiError;
import com.madushanka.imotorist.network.ApiService;
import com.madushanka.imotorist.network.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";
    Call<String> firebase_call;
    ApiService authService;
    TokenManager tokenManager;


    @Override
    public void onTokenRefresh() {

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        updateFirebase(token);
    }


    void updateFirebase(String token) {


        firebase_call = authService.firebase(token);


        firebase_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> firebase_call, Response<String> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    //Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_LONG).show();

                } else {
                    if (response.code() == 422) {


                        //  Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        // Toast.makeText(getApplicationContext(), apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<String> firebase_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}
