
# Digtai CoDriver - Ride-Hailing and Payment System

## Contact Information
- **Email**: kirosmisgna7@gmail.com
- **Phone**: +251930625284
- **Address**: Mekelle,Tigray,Ethiopia
## Project Overview
Digtai CoDriver is a comprehensive ride-hailing and payment system designed to provide users with a seamless experience for booking rides, making payments, and managing their accounts. The application integrates with Firebase for authentication and Firestore for data storage, ensuring secure and efficient transactions.

## Functionalities and Features

### 1. **User Authentication**
   - **Login**: Users can log in using their email and password. Firebase Authentication is used to handle user authentication.
   - **Signup**: New users can create an account by providing their personal details, including first name, middle name, last name, phone number, email, and password. The system validates the input and stores user data in Firestore.
   - **Password Visibility Toggle**: Users can toggle the visibility of their password during login and signup for better usability.

### 2. **Ride Booking and Pricing**
   - **Dynamic Price Calculation**: Users can enter their source and destination locations, and the system calculates the estimated price based on the distance between the two points. The system uses a predefined map of locations and calculates the distance using the Haversine formula.
   - **Location Validation**: The system validates the entered locations and suggests the closest match if the input is not exact.

### 3. **Deposit Management**
   - **Deposit Funds**: Users can deposit funds into their account using various payment methods such as TELEBirr, HelloCash, and CBEBirr. The system validates the deposit amount and phone number before processing the deposit.
   - **Deposit Confirmation**: Upon successful deposit, the system displays a confirmation message with the deposited amount and payment method.

### 4. **Payment Transactions**
   - **Send Money**: Users can send money to the admin by entering the recipient's phone number and the amount to be sent. The system validates the transaction and ensures that the sender has sufficient balance.
   - **Transaction Handling**: The system uses Firestore transactions to ensure atomicity and consistency when updating the sender's and receiver's balances.
   - **Transaction History**: All transactions are recorded in Firestore, allowing users to view their transaction history.

### 5. **User Profile Management**
   - **User Details**: Users can view and update their personal details, including name, phone number, and email.
   - **Balance Management**: Users can view their current balance and transaction history.

### 6. **Splash Screen**
   - **Splash Screen**: The application starts with a splash screen that displays the app logo and transitions to the main activity after a short delay.

### 7. **Security and Validation**
   - **Input Validation**: The system validates all user inputs, including email, phone number, and password, to ensure data integrity.
   - **Firebase Authentication**: User authentication is handled securely using Firebase Authentication.
   - **Firestore Transactions**: All financial transactions are handled securely using Firestore transactions to ensure data consistency.

## Technical Stack
- **Frontend**: Android (Java)
- **Backend**: Firebase Authentication, Firestore
- **Libraries**: Firebase Firestore, Firebase Auth, Google Maps (for location-based services)

## How to Run the Project
1. **Clone the Repository**: Clone the project repository from GitHub.
2. **Set Up Firebase**: Create a Firebase project and add the `google-services.json` file to the `app` directory of the project.
3. **Enable Firebase Authentication and Firestore**: Enable Email/Password authentication and Firestore in the Firebase console.
4. **Run the App**: Open the project in Android Studio and run it on an emulator or a physical device.

## Future Enhancements
- **Integration with Google Maps API**: Enhance the location-based services by integrating Google Maps API for real-time navigation and route optimization.
- **Push Notifications**: Implement push notifications to keep users informed about ride status, transaction updates, and promotions.
- **Multi-language Support**: Add support for multiple languages to cater to a broader audience.

## Conclusion
Digtai CoDriver is a robust and user-friendly ride-hailing and payment system that leverages Firebase for secure authentication and Firestore for efficient data management. The application provides a seamless experience for users to book rides, manage their accounts, and perform financial transactions with ease.