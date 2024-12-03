package com.example.roomdbdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.roomdbdemo.database.UserDatabase;

public class UpdateActivity extends AppCompatActivity {
    private EditText edt_username;
    private EditText edt_address;
    private Button btn_update;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        initUI();

        user = (User) getIntent().getExtras().get("object_user");
        if(user != null){
            edt_username.setText(user.getUserName());
            edt_address.setText(user.getAddress());
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void initUI(){
        edt_username = findViewById(R.id.edt_username);
        edt_address = findViewById(R.id.edt_address);
        btn_update = findViewById(R.id.btn_update);
    }

    private void updateUser() {
        String strUserName = edt_username.getText().toString().trim();
        String strAddress = edt_address.getText().toString().trim();

        if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strAddress)){
            return;
        }
        // thuc hien update user trong database
        user.setUserName(strUserName);
        user.setAddress(strAddress);

        UserDatabase.getInstance(this).userDAO().updateUser(user);

        Toast.makeText(this, "Update user successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}