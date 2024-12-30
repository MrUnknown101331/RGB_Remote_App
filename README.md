RGB Remote App
This project consists of an Android application and ESP32 firmware designed to remotely control RGB LED strips or other compatible devices via Firebase Realtime Database. The Android app provides a user interface to select different lighting modes and set custom colors, while the ESP32 firmware receives these instructions from Firebase and controls the LEDs accordingly.

Features
Remote Control: Control your RGB LEDs from anywhere with an internet connection.
Multiple Lighting Modes:
Off: Turns the LEDs off.
Custom Color: Allows users to select a specific RGB color using sliders or a hex color code.
Smooth Transition: Creates a smooth, continuous transition between different colors.
RGB Cycle: Cycles through red, green, and blue colors.
Appliance Control: Includes functionality to control four additional appliances via digital outputs on the ESP32.
Real-time Updates: Changes made in the Android app are reflected on the LEDs in real-time thanks to Firebase Realtime Database.
Getting Started
Prerequisites
Android Development:
Android Studio installed.
A Firebase project set up with Realtime Database.
ESP32 Development:
Arduino IDE installed with ESP32 board support.
Necessary libraries installed (WiFi, Firebase ESP Client).
Hardware:
ESP32 development board.
RGB LED strip.
Resistors for current limiting (if necessary for your LED strip).
Connecting wires.
(Optional) Relays or transistors for controlling additional appliances.
Installation
1. Android App:

Clone this repository.
Open the project in Android Studio.
Configure Firebase:
Replace the placeholder Firebase configuration values in the code with your own Firebase project credentials.
Build and run the app on an Android device or emulator.
2. ESP32 Firmware:

Clone this repository.
Open the .ino file (Arduino sketch) in the Arduino IDE.
Configure WiFi and Firebase:
Replace the placeholder WiFi credentials (SSID and password) with your network information.
Replace the placeholder Firebase API key and database URL with your Firebase project credentials.
Connect the ESP32 to your computer.
Select the correct board and port in the Arduino IDE.
Upload the code to the ESP32.
3. Hardware Setup:

Connect the RGB LED strip to the ESP32 using appropriate pins and resistors.
(Optional) Connect relays or transistors to the ESP32's digital output pins to control additional appliances.
Connect the ESP32 to a power source.
Usage
Connect your Android device to the internet.
Open the RGB Remote app.
Select the desired lighting mode (Off, Custom Color, Smooth Transition, RGB Cycle).
If "Custom Color" is selected, use the sliders or hex input to choose a color.
The LEDs should update in real-time to reflect the changes made in the app.
The state of the additional appliances connected to the ESP32 will also be controlled by the app.
Code Structure
Android App (Kotlin/Compose):
MainActivity.kt: Main activity that sets up the UI.
FinalLayout.kt: Contains the main layout with radio buttons and the conditional NewCard composable.
NewCard.kt: Contains the color sliders, hex input, and send button for custom color mode.
ESP32 Firmware (Arduino/C++):
.ino file: Contains the main logic for connecting to WiFi, Firebase, and controlling the LEDs and additional appliances.
Dependencies
Android:
Jetpack Compose
Firebase Android SDK
ESP32:
WiFi library
Firebase ESP Client library
Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.