package com.example.digtai_codriver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    private EditText amountEditText, phoneNumberEditText;
    private Button payNowButton;
    private FirebaseFirestore db;

    private final String USERS_COLLECTION = "Users"; // Collection name in Firestore
    private final String TRANSACTIONS_COLLECTION = "Transactions"; // Transaction history collection

    private final String SENDER_PHONE = "+1234567890"; // Hardcoded sender (change dynamically)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        amountEditText = findViewById(R.id.amountEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        payNowButton = findViewById(R.id.payNowButton);

        // Handle payment on button click
        payNowButton.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        String amountStr = amountEditText.getText().toString().trim();
        String receiverPhone = phoneNumberEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(receiverPhone) || receiverPhone.length() != 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (SENDER_PHONE.equals(receiverPhone)) {
            Toast.makeText(this, "Cannot send money to yourself", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform Firestore transaction
        executeTransaction(SENDER_PHONE, receiverPhone, amount);
    }

    private void executeTransaction(String senderPhone, String receiverPhone, double amount) {
        DocumentReference senderRef = db.collection(USERS_COLLECTION).document(senderPhone);
        DocumentReference receiverRef = db.collection(USERS_COLLECTION).document(receiverPhone);
        DocumentReference transactionRef = db.collection(TRANSACTIONS_COLLECTION).document();

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            // Fetch sender's balance
            Double senderBalance = Objects.requireNonNullElse(
                    transaction.get(senderRef).getDouble("balance"), 0.0);

            // Fetch receiver's balance
            Double receiverBalance = Objects.requireNonNullElse(
                    transaction.get(receiverRef).getDouble("balance"), 0.0);

            // Validate sender's balance
            if (senderBalance < amount) {
                throw new RuntimeException("Insufficient balance.");
            }

            // Update sender's balance
            transaction.update(senderRef, "balance", senderBalance - amount);

            // Update receiver's balance
            transaction.update(receiverRef, "balance", receiverBalance + amount);

            // Store transaction details
            Map<String, Object> transactionData = new HashMap<>();
            transactionData.put("sender", senderPhone);
            transactionData.put("receiver", receiverPhone);
            transactionData.put("amount", amount);
            transactionData.put("timestamp", System.currentTimeMillis());

            transaction.set(transactionRef, transactionData);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(PaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            Log.d("PaymentActivity", "Transaction completed.");

            // Redirect to MainActivity
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(PaymentActivity.this, "Transaction Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("PaymentActivity", "Transaction failed: " + e.getMessage());
        });
    }
}
