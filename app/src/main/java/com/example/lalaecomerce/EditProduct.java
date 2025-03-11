package com.example.lalaecomerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EditProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText productName, productPrice, productDescription;
    private ImageView productImage;
    private Switch soldOutSwitch;
    private Button saveButton, changeImageButton;

    private Uri selectedImageUri; // To store the URI of the selected image
    private String productId; // ID of the product being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize views
        productName = findViewById(R.id.editProductName);
        productPrice = findViewById(R.id.editProductPrice);
        productDescription = findViewById(R.id.editProductDescription);
        productImage = findViewById(R.id.editProductImage);
        soldOutSwitch = findViewById(R.id.soldOutSwitch);
        saveButton = findViewById(R.id.saveButton);
        changeImageButton = findViewById(R.id.changeImageButton);

        // Retrieve product ID from intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");

        // Fetch product details (e.g., from a database or API)
        Product product = fetchProductDetails(productId);

        // Populate fields with existing product data
        populateProductDetails(product);

        // Set up click listeners
        changeImageButton.setOnClickListener(v -> showImageOptionsDialog());
        saveButton.setOnClickListener(v -> saveProductChanges());
    }

    private void populateProductDetails(Product product) {
        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));
            productDescription.setText(product.getDescription());
            soldOutSwitch.setChecked(product.isSoldOut());

            // Load the product image using Glide
            Glide.with(this)
                    .load(product.getImageUrl()) // Replace with your image URL or local path
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(productImage);
        }
    }

    private Product fetchProductDetails(String productId) {
        // Simulate fetching product details from a database or API
        // Replace this with your actual data source
        return new Product(
                "Sample Product",
                99.99,
                "This is a sample product description.",
                false,
                "https://example.com/sample-product-image.jpg"
        );
    }

    private void showImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Gallery", "Camera"}, (dialog, which) -> {
                    if (which == 0) {
                        openGallery();
                    } else {
                        openCamera();
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Get the image URI from the gallery
                selectedImageUri = data.getData();
                productImage.setImageURI(selectedImageUri);
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                // Get the image from the camera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productImage.setImageBitmap(imageBitmap);
            }
        }
    }

    private void saveProductChanges() {
        // Retrieve updated values from the input fields
        String updatedName = productName.getText().toString().trim();
        double updatedPrice = Double.parseDouble(productPrice.getText().toString().trim());
        String updatedDescription = productDescription.getText().toString().trim();
        boolean isSoldOut = soldOutSwitch.isChecked();

        // Validate input
        if (updatedName.isEmpty() || updatedDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an updated product object
        Product updatedProduct = new Product(
                updatedName,
                updatedPrice,
                updatedDescription,
                isSoldOut,
                selectedImageUri != null ? selectedImageUri.toString() : null
        );

        // Save the updated product details (e.g., to a database or API)
        boolean success = saveProductDetails(updatedProduct);

        if (success) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to the previous screen
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean saveProductDetails(Product product) {
        // Simulate saving product details to a database or API
        // Replace this with your actual implementation
        return true; // Return true if successful, false otherwise
    }

    // Sample Product class
    public static class Product {
        private String name;
        private double price;
        private String description;
        private boolean isSoldOut;
        private String imageUrl;

        public Product(String name, double price, String description, boolean isSoldOut, String imageUrl) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.isSoldOut = isSoldOut;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public boolean isSoldOut() {
            return isSoldOut;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}