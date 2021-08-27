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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createaccountbutton;
    private EditText inputname,inputephonenumber,inputpassword;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createaccountbutton=findViewById(R.id.register_button);
        inputname=findViewById(R.id.register_username_input);
        inputephonenumber=findViewById(R.id.register_phone_number_input);
        inputpassword=findViewById(R.id.register_password_input);
        loadingbar=new ProgressDialog(this);
        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name=inputname.getText().toString();
        String phone=inputephonenumber.getText().toString();
        String password=inputpassword.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please write your name...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write a password...",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Please wait,while we are checking credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            ValidatephoneNumber(name,phone,password);
        }
    }

    private void ValidatephoneNumber(String name, String phone, String password) {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Congratulations, your account has been created successful.",Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent=new Intent(RegisterActivity.this,loginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                loadingbar.dismiss();
                                Toast.makeText(RegisterActivity.this,"Network Error,please try again.",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"This "+phone+" already exists.",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again with another number.",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}