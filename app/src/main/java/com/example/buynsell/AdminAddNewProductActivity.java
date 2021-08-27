package com.example.buynsell;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime,productRandomKey,downLoadImageUrl;
    private Button AddNewProductButton;
    private ImageView InputProdectImage;
    private TextView InputProdectName,InputProdectDescription,InputProdectPrice;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        CategoryName=getIntent().getExtras().get("category").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton=findViewById(R.id.add_new_product);
        InputProdectImage=findViewById(R.id.select_product_image);
        InputProdectName=findViewById(R.id.product_name);
        InputProdectDescription=findViewById(R.id.product_description);
        InputProdectPrice=findViewById(R.id.product_price);

        loadingbar=new ProgressDialog(this);

        InputProdectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    //edited

    ActivityResultLauncher<String> mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result!=null){
                ImageUri=result;
                InputProdectImage.setImageURI(ImageUri);
            }
        }
    });
    //edited



    private void openGallery() {
//        Intent galleryIntent=new Intent();
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//
//        startActivityForResult(galleryIntent,GalleryPick);
        mGetContent.launch("image/*");


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==GalleryPick&&requestCode==RESULT_OK&&data!=null){
//            ImageUri=data.getData();
//            InputProdectImage.setImageURI(ImageUri);
//        }
//    }


    private void ValidateProductData() {
        Description=InputProdectDescription.getText().toString();
        Price=InputProdectPrice.getText().toString();
        Pname=InputProdectName.getText().toString();

        if(ImageUri==null){
            Toast.makeText(AdminAddNewProductActivity.this,"Product Image Is Mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(AdminAddNewProductActivity.this,"Please write Product Description.",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(AdminAddNewProductActivity.this,"Please write Product Price.",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname)){
            Toast.makeText(AdminAddNewProductActivity.this,"Please write Product Name.",Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductDescription();
        }
    }

    private void StoreProductDescription() {
        loadingbar.setTitle("Adding New Product");
        loadingbar.setMessage("Please wait, we are adding new product.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar=Calendar.getInstance();
        //takinf time to give unique path ref
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate =currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;
        StorageReference filePath=ProductImagesRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Product Image Uploaded Successfully!!",Toast.LENGTH_SHORT).show();
                //OnCompleteListener<TResult> tResultOnCompleteListener = ;
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        downLoadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull  Task<Uri> task) {
                        if(task.isSuccessful()){
                            downLoadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Got Product Image Successfully",Toast.LENGTH_SHORT).show();

                            SaveProductInfotoDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfotoDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downLoadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);

        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);

                    startActivity(intent);
                    loadingbar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this,"Product is Added Successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingbar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this,"Error "+message,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}