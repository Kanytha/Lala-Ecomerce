package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TopManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter; // Use your existing RecyclerAdapter
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_management);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = getSampleProducts(); // Replace with actual data source
        adapter = new RecyclerAdapter(productList, this); // Use your RecyclerAdapter
        recyclerView.setAdapter(adapter);

        // Set click listener for the Add Button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(TopManagementActivity.this, RecyclerAdapter.class);
            intent.putExtra("category", "Tops"); // Pass the category
            startActivity(intent); // Navigate to the Add Product screen
        });

    }

    // Sample data for testing
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product("Top 1", 19.99, "https://example.com/top1.jpg"));
        list.add(new Product("Top 2", 24.99, "https://example.com/top2.jpg"));
        list.add(new Product("Top 3", 29.99, "https://example.com/top3.jpg"));
        return list;
    }
}