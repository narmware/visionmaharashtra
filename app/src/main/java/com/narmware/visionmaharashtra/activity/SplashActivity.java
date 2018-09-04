package com.narmware.visionmaharashtra.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.support.SharedPreferencesHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static int TIMEOUT = 2000;

ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        logo=findViewById(R.id.logo);

        YoYo.with(Techniques.Landing)
                .duration(500)
                .playOn(logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SharedPreferencesHelper.getIsLogin(SplashActivity.this)==false)
                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, HomeActivityTab.class));
                    finish();
                }
            }
        }, TIMEOUT);
    }

}
