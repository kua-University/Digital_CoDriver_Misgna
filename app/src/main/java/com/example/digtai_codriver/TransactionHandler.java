package com.example.digtai_codriver;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import java.util.Objects;

public class TransactionHandler {
    private static final String USERS_COLLECTION = "Users"; // Firestore collection name

    public static void processTransaction(TransactionModel transactionModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // References to sender and receiver documents
        DocumentReference senderRef = db.collection(USERS_COLLECTION).document(transactionModel.getSenderPhone());
        DocumentReference receiverRef = db.collection(USERS_COLLECTION).document(transactionModel.getReceiverPhone());

        db.runTransaction((Transaction.Function<Void>) firebaseTransaction -> {
                    // Check if sender exists
                    DocumentSnapshot senderSnapshot = firebaseTransaction.get(senderRef);
                    if (!senderSnapshot.exists()) {
                        throw new RuntimeException("Sender account does not exist.");
                    }

                    // Check if receiver exists
                    DocumentSnapshot receiverSnapshot = firebaseTransaction.get(receiverRef);
                    if (!receiverSnapshot.exists()) {
                        throw new RuntimeException("Receiver account does not exist.");
                    }

                    // Fetch sender balance (default to 0.0 if null)
                    Double senderBalance = senderSnapshot.getDouble("balance");
                    if (senderBalance == null) {
                        senderBalance = 10000.0;
                    }

                    // Fetch receiver balance (default to 0.0 if null)
                    Double receiverBalance = receiverSnapshot.getDouble("balance");
                    if (receiverBalance == null) {
                        receiverBalance = 0.0;
                    }

                    // Validate sender balance
                    if (senderBalance < transactionModel.getAmount()) {
                        throw new RuntimeException("Insufficient balance.");
                    }

                    // Deduct from sender
                    firebaseTransaction.update(senderRef, "balance", senderBalance - transactionModel.getAmount());

                    // Add to receiver
                    firebaseTransaction.update(receiverRef, "balance", receiverBalance + transactionModel.getAmount());

                    return null;
                }).addOnSuccessListener(aVoid -> Log.d("Transaction", "Transaction successful!"))
                .addOnFailureListener(e -> Log.e("Transaction", "Transaction failed: " + e.getMessage()));
    }
}