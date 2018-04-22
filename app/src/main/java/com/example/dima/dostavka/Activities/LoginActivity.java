package com.example.dima.dostavka.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.dima.dostavka.Helper.Driver;
import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.myEnum;
import com.example.dima.dostavka.R;

import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser;
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin;
import ru.profit_group.scorocode_sdk.ScorocodeSdk;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;
import ru.profit_group.scorocode_sdk.scorocode_objects.User;

public class LoginActivity extends AppCompatActivity {

    private static final String APPLICATION_ID = "c312b5a7b1794220a85b89079250e64e";
    private static final String CLIENT_KEY = "aec9813472954766897c74a55815d4e1";
    private static final String MASTER_KEY = "ec67c7fce9fb4f63a234d2d708f3a9c6";

    private static final int PERMISSIONS_INTERNET = 60;

    private SharedPreferences preferences;

    final String SAVED_TEXT_NUMBER = "saved_number";
    private final String TEXT_PASS = "1";

    private EditText edLogin;

    private Button btLogin;
    private ProgressBar prBarLogin;
    private Boolean checkPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activiti);

           edLogin =  findViewById(R.id.edLogin);
           btLogin = findViewById(R.id.btLobin);
           prBarLogin = findViewById(R.id.prBarLogin);

           checkPermissions();

        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY,MASTER_KEY,
                null,null,null,null);

        checkPref = checkPreferences();



        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPref){
                    newPreferences();
                }

                btLogin.setClickable(false);

                prBarLogin.setVisibility(ProgressBar.VISIBLE);

                loging(TEXT_PASS);


            }
        });


    }

    private void loging(String pass){
        User user = new User();
        final String textLogin = edLogin.getText().toString();
        user.login(textLogin, pass, new CallbackLoginUser() {


            @Override
            public void onLoginSucceed(ResponseLogin responseLogin) {
                prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                btLogin.setClickable(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login", textLogin);
                startActivity(intent);


            }

            @Override
            public void onLoginFailed(String errorCode, String errorMessage) {
                prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                Helper.showToast(getBaseContext(), errorMessage);
                btLogin.setClickable(true);
            }
        });
    }

    private void checkPermissions(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSIONS_INTERNET);
            }

    }

    private void newPreferences(){
            String login = edLogin.getText().toString();
            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SAVED_TEXT_NUMBER, login);

            editor.apply();

    }

    private boolean checkPreferences(){
        preferences = getPreferences(MODE_PRIVATE);
        String login = preferences.getString(SAVED_TEXT_NUMBER, "");
        if (!login.isEmpty()){
            edLogin.setText(login);
            //chSave.setChecked(true);
            return true;
        }
        else return false;
    }


}

