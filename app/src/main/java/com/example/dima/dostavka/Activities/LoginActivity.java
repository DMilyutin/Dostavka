package com.example.dima.dostavka.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.R;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser;
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin;
import ru.profit_group.scorocode_sdk.ScorocodeSdk;
import ru.profit_group.scorocode_sdk.scorocode_objects.User;

public class LoginActivity extends AppCompatActivity {

    private final String APPLICATION_ID = "c312b5a7b1794220a85b89079250e64e";
    private final String CLIENT_KEY = "aec9813472954766897c74a55815d4e1";
    private static final String MASTER_KEY = "ec67c7fce9fb4f63a234d2d708f3a9c6";

    private static final int PERMISSIONS_INTERNET = 60;


    private String saveLogin ;
    private String savePass ;

    private EditText edLogin;
    private EditText edPass;
    private CheckBox chSave;
    private Button btLogin;
    private ProgressBar prBarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activiti);

           edLogin =  findViewById(R.id.edLogin);
           edPass =  findViewById(R.id.edPass);
           chSave = findViewById(R.id.chSaveLogPass);
           btLogin = findViewById(R.id.btLobin);
           prBarLogin = findViewById(R.id.prBarLogin);

           checkPermissions();

        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY,MASTER_KEY, null,null,null,null);

        btLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                btLogin.setClickable(false);
                prBarLogin.setVisibility(ProgressBar.VISIBLE);
                User user = new User();
                user.login(edLogin.getText().toString(), edPass.getText().toString(), new CallbackLoginUser() {

                    @Override
                    public void onLoginSucceed(ResponseLogin responseLogin) {
                        prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                        btLogin.setClickable(true);
                        MainActivity.display(LoginActivity.this);
                    }

                    @Override
                    public void onLoginFailed(String errorCode, String errorMessage) {
                        prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                        Helper.showToast(getBaseContext(), R.string.error_login);
                        btLogin.setClickable(true);
                    }
                });

            }
        });


    }


    private void checkPermissions(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSIONS_INTERNET);
            }

    }


}

