package com.example.digtai_codriver;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DepositActivity extends AppCompatActivity {

    private EditText amountEditText, phoneEditText;
    private Spinner paymentMethodSpinner;
    private Button depositButton;
    private String selectedPaymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        // Apply fade-in animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

        // Initialize views
        amountEditText = findViewById(R.id.amountEditText);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        phoneEditText = findViewById(R.id.phoneEditText);
        depositButton = findViewById(R.id.depositButton);

        // Set up spinner options (dropdown)
        String[] paymentMethods = {"TELEBirr","HelloCash", "CBEBirr"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        paymentMethodSpinner.setAdapter(adapter);

        // Handle spinner selection
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = paymentMethods[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPaymentMethod = "";
            }
        });

        // Handle deposit button click
        depositButton.setOnClickListener(view -> processDeposit());
    }

    private void processDeposit() {
        String amount = amountEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();

        if (amount.isEmpty()) {
            amountEditText.setError("Enter an amount");
            return;
        }

        if (selectedPaymentMethod.equals("Select Payment Method") || selectedPaymentMethod.isEmpty()) {
            Toast.makeText(this, "Please select a deposit option", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            phoneEditText.setError("Enter a valid phone number");
            return;
        }

        Toast.makeText(this, "Depositing $" + amount + " via " + selectedPaymentMethod + " to phone " + phoneNumber, Toast.LENGTH_LONG).show();

        // Here you can add further actions like API calls to process the deposit
    }
}
