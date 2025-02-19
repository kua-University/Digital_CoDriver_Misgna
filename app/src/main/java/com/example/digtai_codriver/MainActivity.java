package com.example.digtai_codriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText sourceEditText, destinationEditText;
    private TextView priceTextView;
    private Button calculateButton, depositButton, loginButton, paymentButton, payNowButton;
    private double depositedAmount; // User's deposited amount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        priceTextView = findViewById(R.id.priceTextView);
        calculateButton = findViewById(R.id.calculateButton);
        depositButton = findViewById(R.id.depositButton);
        loginButton = findViewById(R.id.loginButton);
        paymentButton = findViewById(R.id.payNowButton);
        payNowButton = findViewById(R.id.payNowButton); // Initialize the new Pay Now button

        // Load deposited amount
        loadDepositedAmount();

        // Calculate price on button click
        calculateButton.setOnClickListener(v -> {
            String source = sourceEditText.getText().toString().trim();
            String destination = destinationEditText.getText().toString().trim();
            calculateDynamicPrice(source, destination);
        });

        // Open deposit screen
        depositButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DepositActivity.class);
            startActivity(intent);
        });

        // Open login screen
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Open payment screen
        paymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });

        // "Pay Now" button action - opens payment screen
        payNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }

    private void calculateDynamicPrice(String source, String destination) {
        String sourceKey = source.toLowerCase().trim();
        String destinationKey = destination.toLowerCase().trim();

        // Map of valid locations
        Map<String, LatLng> mekelleLocations = new HashMap<>();
        mekelleLocations.put("mekelle university", new LatLng(13.4964, 39.4752));
        mekelleLocations.put("quiha", new LatLng(13.4825, 39.5321));
        mekelleLocations.put("aynalem", new LatLng(13.4610, 39.5291));
        mekelleLocations.put("adihaki", new LatLng(13.5000, 39.4700));
        mekelleLocations.put("hawelti", new LatLng(13.4905, 39.4785));
        mekelleLocations.put("kedamay weyane", new LatLng(13.4969, 39.4771));
        mekelleLocations.put("romanat", new LatLng(13.5041, 39.4873));
        mekelleLocations.put("ayder hospital", new LatLng(13.4963, 39.4692));
        mekelleLocations.put("abry hotel", new LatLng(13.4912, 39.4756));
        mekelleLocations.put("kebele 16", new LatLng(13.5087, 39.4820));
        mekelleLocations.put("hadnet", new LatLng(13.4992, 39.4811));
        mekelleLocations.put("martyrs' memorial", new LatLng(13.4828, 39.4862));
        mekelleLocations.put("semen hotel", new LatLng(13.4948, 39.4796));
        mekelleLocations.put("adiha", new LatLng(13.5073, 39.4935));
        mekelleLocations.put("golden hotel", new LatLng(13.4927, 39.4765));
        mekelleLocations.put("lach", new LatLng(13.4917, 39.4565));
        mekelleLocations.put("planet hotel", new LatLng(13.4927, 39.4765));
        mekelleLocations.put("mesebo ciminto", new LatLng(13.5667, 39.4565));
        // Find closest match for user input
        sourceKey = getClosestMatch(sourceKey, mekelleLocations);
        destinationKey = getClosestMatch(destinationKey, mekelleLocations);

        if (sourceKey == null || destinationKey == null) {
            Toast.makeText(this, "Invalid location! Try another.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get LatLng values
        LatLng sourceLatLng = mekelleLocations.get(sourceKey);
        LatLng destinationLatLng = mekelleLocations.get(destinationKey);

        // Calculate distance and price
        double distance = calculateDistance(
                sourceLatLng.latitude, sourceLatLng.longitude,
                destinationLatLng.latitude, destinationLatLng.longitude
        );
        double price = distance * 5.0; // Example price calculation

        // Update UI with the calculated price
        priceTextView.setText("Source: " + sourceKey + "\n" +
                "Destination: " + destinationKey + "\n" +
                "Estimated Price: " + String.format("%.2f", price) + " Birr");
    }

    private String getClosestMatch(String userInput, Map<String, LatLng> locations) {
        int minDistance = Integer.MAX_VALUE;
        String closestMatch = null;

        for (String location : locations.keySet()) {
            int distance = levenshteinDistance(userInput, location);
            if (distance < minDistance && distance <= 2) {
                minDistance = distance;
                closestMatch = location;
            }
        }
        return closestMatch;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    private void loadDepositedAmount() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        depositedAmount = prefs.getFloat("depositedAmount", 0.0f);
        Log.d("MainActivity", "Deposited Amount Loaded: " + depositedAmount);
    }
}
