package com.example.buynsell.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buynsell.Adapters.ProductAdapter;
import com.example.buynsell.Model.ProductModel;
import com.example.buynsell.R;
import com.example.buynsell.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
//    private FragmentHomeBinding binding;
    RecyclerView productRec;
    //FirebaseFirestore db;
    DatabaseReference database;
    List<ProductModel> productModelList;
    ProductAdapter productAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_home,container,false);
        //db=FirebaseFirestore.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Products");


        productRec=root.findViewById(R.id.recview);
        productRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        productModelList=new ArrayList<>();
        productAdapter=new ProductAdapter(getActivity(),productModelList);
        productRec.setAdapter(productAdapter);



        //new implement
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ProductModel productModel=dataSnapshot.getValue(ProductModel.class);
                    productModelList.add(productModel);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return root;
    }
}