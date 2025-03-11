package com.example.lalaecomerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LalaEcommerce.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_FULL_NAME = "full_name";

//    product
// Add a table for skirts
    private static final String TABLE_SKIRTS = "skirts";
    private static final String COLUMN_SKIRT_ID = "id";
    private static final String COLUMN_SKIRT_NAME = "name";
    private static final String COLUMN_SKIRT_PRICE = "price";
    private static final String COLUMN_SKIRT_IMAGE_URL = "image_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_FULL_NAME + " TEXT)");

        // Insert default admin user
        insertAdminIfNotExists(db);

//        product
        db.execSQL("CREATE TABLE " + TABLE_SKIRTS + " (" +
                COLUMN_SKIRT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SKIRT_NAME + " TEXT, " +
                COLUMN_SKIRT_PRICE + " REAL, " +
                COLUMN_SKIRT_IMAGE_URL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    // Insert admin if not exists
    private void insertAdminIfNotExists(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_EMAIL + " = ?",
                new String[]{"admin@example.com"}, null, null, null);
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, "admin@example.com");
            values.put(COLUMN_PASSWORD, hashPassword("adminpassword"));
            values.put(COLUMN_ROLE, "admin");
            values.put(COLUMN_FULL_NAME, "Admin User");
            db.insert(TABLE_NAME, null, values);
            Log.d("DatabaseHelper", "Admin inserted");
        }
        cursor.close();
    }

    // Check if user exists
    public boolean checkUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Validate login credentials
    public boolean validateLogin(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_PASSWORD},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            cursor.close();
            return hashPassword(password).equals(storedHash);
        }
        cursor.close();
        return false;
    }

    // Get user role
    public String getUserRole(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ROLE},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return "user"; // Default to user if not found
    }

    // Insert new user (for signup)
    public boolean insertUser(String email, String password, String fullName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashPassword(password));
        values.put(COLUMN_ROLE, "user");
        values.put(COLUMN_FULL_NAME, fullName);
        return db.insert(TABLE_NAME, null, values) != -1;
    }

    // Password hashing (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


//    skirt Management
// Example: Skirt table operations
    public boolean addSkirt(String name, double price, String imageUrl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image_url", imageUrl);
        return db.insert("skirts", null, values) != -1;
    }

//    product
// Add a skirt
public boolean addSkirt(Product product) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_SKIRT_NAME, product.getName());
    values.put(COLUMN_SKIRT_PRICE, product.getPrice());
    values.put(COLUMN_SKIRT_IMAGE_URL, product.getImageUrl());
    return db.insert(TABLE_SKIRTS, null, values) != -1;
}

    // Get all skirts
    public ArrayList<Product> getAllSkirts() {
        ArrayList<Product> skirts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SKIRTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_SKIRT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_SKIRT_PRICE));
                String imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_SKIRT_IMAGE_URL));
                skirts.add(new Product(name, price, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return skirts;
    }

}