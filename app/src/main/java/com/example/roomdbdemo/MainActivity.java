package com.example.roomdbdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdbdemo.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private EditText edt_username;
    private EditText edt_address;
    private Button btn_add;
    private RecyclerView rcv_user;
    private UserAdapter userAdapter;
    private List<User> userList;
    private TextView tv_delete_all;
    private EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initUI();
        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        userList = new ArrayList<>();
        userAdapter.setData(userList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_user.setLayoutManager(linearLayoutManager);
        rcv_user.setAdapter(userAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        loadData();

        tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllUser();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    // logic search
                    handelSearchUser();
                }
                return false;
            }
        });
    }

    private void initUI(){
        edt_username = findViewById(R.id.edt_username);
        edt_address = findViewById(R.id.edt_address);
        btn_add = findViewById(R.id.btn_add);
        rcv_user = findViewById(R.id.rcv_user);
        tv_delete_all = findViewById(R.id.tv_delete_all);
        edt_search = findViewById(R.id.edt_search);
    }

    private void addUser() {
        String strUserName = edt_username.getText().toString().trim();
        String strAddress = edt_address.getText().toString().trim();

        if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strAddress)){
            return;
        }
        User user = new User(strUserName, strAddress);

        if(isUserExist(user)){
            Toast.makeText(this, "User exist", Toast.LENGTH_SHORT).show();
            return;
        }
        UserDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this, "Add user successfully", Toast.LENGTH_SHORT).show();

        edt_address.setText("");
        edt_username.setText("");

        hideSofKeyboard();

        loadData();
    }

    public void hideSofKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void loadData(){
        userList = UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(userList);
    }

    public boolean isUserExist(User user){
        List<User> list = UserDatabase.getInstance(this).userDAO().checkUser(user.getUserName());
        return list != null && !list.isEmpty();
    }

    private void clickUpdateUser(User user) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);

      startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }

    private void clickDeleteUser(User user){
        new AlertDialog.Builder(this).setTitle("Cảnh báo!")
                .setMessage("Bạn có muốn xóa user này ? ")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // delete user
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(MainActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                }).setNegativeButton("Không", null)
                .show();
    }

    private void clickDeleteAllUser(){
        new AlertDialog.Builder(this).setTitle("Cảnh báo!")
                .setMessage("Bạn muốn xóa tất cả các user ?")
                .setPositiveButton("Đúng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteAllUser();
                        Toast.makeText(MainActivity.this, "Delete all user successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                }).setNegativeButton("Không", null)
                .show();
    }

    private void handelSearchUser(){
        String strKeyword = edt_search.getText().toString().trim();
        userList.clear();
        userList = UserDatabase.getInstance(MainActivity.this).userDAO().searchUser(strKeyword);
        userAdapter.setData(userList);
        hideSofKeyboard();
    }
}