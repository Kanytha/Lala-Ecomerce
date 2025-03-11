package com.example.lalaecomerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText productName, productPrice, productDescription;
    private ImageView productImage;
    private Button addImageButton, submitProductButton;

    private Uri selectedImageUri; // To store the URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        productName = findViewById(R.id.addProductName);
        productPrice = findViewById(R.id.addProductPrice);
        productDescription = findViewById(R.id.addProductDescription);
        productImage = findViewById(R.id.addProductImage);
        addImageButton = findViewById(R.id.addImageButton);
        submitProductButton = findViewById(R.id.submitProductButton);

        // Set up click listeners
        addImageButton.setOnClickListener(v -> showImageOptionsDialog());
        submitProductButton.setOnClickListener(v -> submitProduct());
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
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                productImage.setImageBitmap(imageBitmap);

                // Save the bitmap to a file and get its URI
                try {
                    File imageFile = saveBitmapToFile(imageBitmap);
                    selectedImageUri = Uri.fromFile(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) throws IOException {
        File file = new File(getCacheDir(), "temp_image.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return file;
    }

    private void submitProduct() {
        // Retrieve input values
        String name = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || priceString.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Upload the product data (including the image)
        boolean success = uploadProduct(name, price, description, selectedImageUri);

        if (success) {
            Toast.makeText(this, "Product uploaded successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to upload product", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean uploadProduct(String name, double price, String description, Uri imageUri) {
        // Simulate uploading the product to a server or saving it locally
        // Replace this with your actual implementation (e.g., API call or database insertion)

        // Example: Convert image to base64 string or upload as a multipart request
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        // For demonstration purposes, return true
        return true;
    }
}