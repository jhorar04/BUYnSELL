package com.example.buynsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynsell.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private EditText InputNumber,InputPassword;
    Button LoginButton;
    private ProgressDialog loadingbar;
     private TextView Adminlink,NotAdminlink;
    private String parentDbname="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputNumber=findViewById(R.id.login_phone_number_input);
        InputPassword=findViewById(R.id.login_password_input);
        LoginButton=findViewById(R.id.login_button);
        Adminlink=findViewById(R.id.admin_panel_link);
        NotAdminlink=findViewById(R.id.notadmin_panel_link);

        loadingbar=new ProgressDialog(this);

        Adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                Adminlink.setVisibility(v.INVISIBLE);
                NotAdminlink.setVisibility(v.VISIBLE);
                parentDbname="Admins";
            }
        });
        NotAdminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                Adminlink.setVisibility(v.VISIBLE);
                NotAdminlink.setVisibility(v.INVISIBLE);
                parentDbname="Users";
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone=InputNumber.getText().toString();
        String password=InputPassword.getText().toString();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write a password...",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Logging In");
            loadingbar.setMessage("Please wait,while we are checking credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {
        final DatabaseReference Rootreference;
        Rootreference= FirebaseDatabase.getInstance().getReference();
        Rootreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.child(parentDbname).child(phone).exists()){
                    Users usersData=snapshot.child(parentDbname).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            if(parentDbname.equals("Admins")){
                                Toast.makeText(loginActivity.this,"Logged In successfully...",Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent=new Intent(loginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbname.equals("Users")){
                                Toast.makeText(loginActivity.this,"Logged In successfully...",Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent=new Intent(loginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            loadingbar.dismiss();
                            Toast.makeText(loginActivity.this,"Incorrect password.",Toast.LENGTH_SHORT).show();
                        }
                    }



//                    else if(usersData.getPhone().equals(InputNumber)){
//                        if(usersData.getPassword().equals(InputPassword)){
//                            Toast.makeText(loginActivity.this,"Logged In successfully...",Toast.LENGTH_SHORT).show();
//                            loadingbar.dismiss();
//                            Intent intent=new Intent(loginActivity.this,AdminAddNewProductActivity.class);
//                            startActivity(intent);
//                        }
//                    }




                    else {
                        loadingbar.dismiss();
                        Toast.makeText(loginActivity.this,"Incorrect phone number.",Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(loginActivity.this,"Account with this "+phone+" number do not exists.",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(loginActivity.this,"You need to create a new account.",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }
}