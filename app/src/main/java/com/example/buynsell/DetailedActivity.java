package com.example.buynsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buynsell.Model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    TextView quantity;
    int totalQuantity=1;
    //int totalPrice=0;

    ImageView detailedImg,addItem,removeItem;
    TextView price,description,proname;
    Button addToCart;
    String productID,productRandomkey;

    ProductModel productModel;
    //refre
    DatabaseReference productRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        quantity=findViewById(R.id.quantity);

        productID=getIntent().getStringExtra("pid");
        detailedImg=findViewById(R.id.detailed_image);
        addItem=findViewById(R.id.add_item);
        proname=findViewById(R.id.pname);
        removeItem=findViewById(R.id.remove_item);
        price=findViewById(R.id.detailed_price);
        description=findViewById(R.id.detailed_des);
        addToCart=findViewById(R.id.add_to_cart);
        getProductsDetail(productID);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity<10){
                    totalQuantity++;
                    //totalPrice= Integer.parseInt(productModel.getPrice())*totalQuantity;
                    quantity.setText(String.valueOf(totalQuantity));
                }

            }
        });




        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>1){
                    totalQuantity--;
                    //totalPrice= Integer.parseInt(productModel.getPrice())*totalQuantity;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

        //refer
        productRef=FirebaseDatabase.getInstance().getReference().child("AddToCart");

    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());
        productRandomkey=saveCurrentDate+saveCurrentTime;

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("productName",productModel.getPname());
        cartMap.put("productPrice",productModel.getPrice());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("pid",productRandomkey);
        //cartMap.put("totalPrice",totalPrice);

        //refer
        productRef.child(productRandomkey).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Toast.makeText(DetailedActivity.this,"Added To Cart",Toast.LENGTH_SHORT).show();
            }
        });


    }


     private void getProductsDetail(String productID) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    productModel=snapshot.getValue(ProductModel.class);
                    price.setText("Price :- "+productModel.getPrice()+" $");
                    //totalPrice= Integer.parseInt(productModel.getPrice())*totalQuantity;
                    description.setText(productModel.getDescription());
                    proname.setText(productModel.getPname());
                    Glide.with(getApplicationContext()).load(productModel.getImage()).into(detailedImg);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}