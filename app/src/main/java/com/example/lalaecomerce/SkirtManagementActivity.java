package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SkirtManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skirt_management);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);


        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = getSampleProducts(); // Replace with actual data source
        adapter = new RecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

    }

    // Method to generate sample product data
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product("Skirt 1", 29.99, "https://example.com/skirt1.jpg"));
        list.add(new Product("Skirt 2", 39.99, "https://example.com/skirt2.jpg"));
        list.add(new Product("Skirt 3", 49.99, "https://example.com/skirt3.jpg"));
        return list;
    }
}