package com.example.buynsell.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buynsell.DetailedActivity;
import com.example.buynsell.Model.ProductModel;
import com.example.buynsell.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHoder> {

    private Context context;
    private List<ProductModel> productModelList;



    public ProductAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHoder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImage()).into(holder.imageView);
        holder.txtProductName.setText(productModelList.get(position).getPname());
        holder.txtProductDescription.setText(productModelList.get(position).getDescription());
        holder.txtProductPrice.setText("Price "+productModelList.get(position).getPrice()+"$");


        ///detailed activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailedActivity.class);
                intent.putExtra("pid",productModelList.get(position).getPid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{

        public TextView txtProductName, txtProductDescription, txtProductPrice;
        public ImageView imageView;
        public ViewHoder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.product_image);
            txtProductName = (TextView) itemView.findViewById(R.id.product_name);
            txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
            txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        }
    }
}
