#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include <math.h>
#include <string.h>

#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

#define WIFI_SSID "POCO M3 Pro"
#define WIFI_PASSWORD "NewPasscode"

#define API_KEY "AIzaSyDRUoMrt50Aykdw6TLhe1Baptuhojh7LS8"
#define DATABASE_URL "https://esp32-rgb-bd93d-default-rtdb.asia-southeast1.firebasedatabase.app/"

#define LED_BUILTIN 2
#define redPin 13   // 13 corresponds to GPIO13
#define greenPin 12 // 12 corresponds to GPIO12
#define bluePin 14  // 14 corresponds to GPIO14
#define sw1Pin 5
#define sw2Pin 18
#define sw3Pin 19
#define sw4Pin 21

FirebaseData fbdo_isOff;
FirebaseData fbdo_isCustom;
FirebaseData fbdo_color;
FirebaseData fbdo_rgbLight;
FirebaseData fbdo_smoothLight;
FirebaseData fbdo_sw1;
FirebaseData fbdo_sw2;
FirebaseData fbdo_sw3;
FirebaseData fbdo_sw4;
FirebaseAuth auth;
FirebaseConfig config;

const int freq = 5000;
const int redChannel = 0;
const int greenChannel = 1;
const int blueChannel = 2;
const int resolution = 8;

bool signupOK = false;
String rgb = "FA00A00";
bool off = false, iscolor = true, issmooth = false, isrgb = false;

void setColor(String);

void setup()
{
    Serial.begin(115200);
    pinMode(LED_BUILTIN, OUTPUT);
    pinMode(sw1Pin, OUTPUT);
    pinMode(sw2Pin, OUTPUT);
    pinMode(sw3Pin, OUTPUT);
    pinMode(sw4Pin, OUTPUT);

    ledcSetup(redChannel, freq, resolution);
    ledcSetup(greenChannel, freq, resolution);
    ledcSetup(blueChannel, freq, resolution);

    ledcAttachPin(redPin, redChannel);
    ledcAttachPin(greenPin, greenChannel);
    ledcAttachPin(bluePin, blueChannel);

    setColor("FA00A0");

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(300);
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println();

    config.api_key = API_KEY;
    config.database_url = DATABASE_URL;
    if (Firebase.signUp(&config, &auth, "", ""))
    {
        Serial.println("ok");
        signupOK = true;
    }
    else
    {
        Serial.printf("%s\n", config.signer.signupError.message.c_str());
    }

    config.token_status_callback = tokenStatusCallback;
    Firebase.begin(&config, &auth);
    Firebase.reconnectWiFi(true);

    if (!Firebase.RTDB.beginStream(&fbdo_isOff, "/isOff"))
        Serial.printf("Strem 1 begin error : %s\n\n", fbdo_isOff.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_isCustom, "/isCustom"))
        Serial.printf("Strem 2 begin error : %s\n\n", fbdo_isCustom.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_color, "/Color"))
        Serial.printf("Strem 3 begin error : %s\n\n", fbdo_color.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_rgbLight, "/rgbLight"))
        Serial.printf("Strem 4 begin error : %s\n\n", fbdo_rgbLight.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_smoothLight, "/smoothLight"))
        Serial.printf("Strem 5 begin error : %s\n\n", fbdo_smoothLight.errorReason().c_str());

    if (!Firebase.RTDB.beginStream(&fbdo_sw1, "/Sw1"))
        Serial.printf("Strem 5 begin error : %s\n\n", fbdo_sw1.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_sw2, "/Sw2"))
        Serial.printf("Strem 5 begin error : %s\n\n", fbdo_sw2.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_sw3, "/Sw3"))
        Serial.printf("Strem 5 begin error : %s\n\n", fbdo_sw3.errorReason().c_str());
    if (!Firebase.RTDB.beginStream(&fbdo_sw4, "/Sw4"))
        Serial.printf("Strem 5 begin error : %s\n\n", fbdo_sw4.errorReason().c_str());
}

void loop()
{
    digitalWrite(LED_BUILTIN, WiFi.status() == WL_CONNECTED);
    if (Firebase.ready() && signupOK)
    {
        if (!Firebase.RTDB.readStream(&fbdo_isOff))
            Serial.printf("Strem 1 read error : %s\n\n", fbdo_isOff.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_isCustom))
            Serial.printf("Strem 2 read error : %s\n\n", fbdo_isCustom.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_color))
            Serial.printf("Strem 3 read error : %s\n\n", fbdo_color.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_rgbLight))
            Serial.printf("Strem 4 read error : %s\n\n", fbdo_rgbLight.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_smoothLight))
            Serial.printf("Strem 5 read error : %s\n\n", fbdo_smoothLight.errorReason().c_str());

        if (!Firebase.RTDB.readStream(&fbdo_sw1))
            Serial.printf("Strem 6 read error : %s\n\n", fbdo_sw1.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_sw2))
            Serial.printf("Strem 7 read error : %s\n\n", fbdo_sw2.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_sw3))
            Serial.printf("Strem 8 read error : %s\n\n", fbdo_sw3.errorReason().c_str());
        if (!Firebase.RTDB.readStream(&fbdo_sw4))
            Serial.printf("Strem 9 read error : %s\n\n", fbdo_sw4.errorReason().c_str());

        if (fbdo_isOff.streamAvailable())
        {
            off = fbdo_isOff.boolData();
            if (off)
                setColor("000000");
        }
        if (fbdo_isCustom.streamAvailable())
        {
            iscolor = fbdo_isCustom.boolData();
            if (iscolor)
                setColor(rgb);
        }
        if (fbdo_color.streamAvailable())
        {
            rgb = fbdo_color.stringData();
            if (iscolor)
                setColor(rgb);
        }
        if (fbdo_rgbLight.streamAvailable())
        {
            isrgb = fbdo_rgbLight.boolData();
        }
        if (fbdo_smoothLight.streamAvailable())
        {
            issmooth = fbdo_smoothLight.boolData();
        }

        if (fbdo_sw1.streamAvailable())
        {
            digitalWrite(sw1Pin,!fbdo_sw1.boolData());
        }
        if (fbdo_sw2.streamAvailable())
        {
            digitalWrite(sw2Pin,!fbdo_sw2.boolData());
        }
        if (fbdo_sw3.streamAvailable())
        {
            digitalWrite(sw3Pin,!fbdo_sw3.boolData());
        }
        if (fbdo_sw4.streamAvailable())
        {
            digitalWrite(sw4Pin,!fbdo_sw4.boolData());
        }


        if (isrgb)
            changeRgb();
        if (issmooth)
            changeSmooth();
    }
}

void setColor(String rgb)
{
    int red, green, blue;
    red = hexToDec(rgb.substring(0, 2));
    green = hexToDec(rgb.substring(2, 4));
    blue = hexToDec(rgb.substring(4));
    if (red * 2 < 256)
        red *= 2;
    else
    {
        green /= 2;
        blue /= 2;
    }
    changeColor(red, green, blue);
}

void changeColor(int red, int green, int blue)
{
    ledcWrite(redChannel, red);
    ledcWrite(greenChannel, green);
    ledcWrite(blueChannel, blue);
}

void changeRgb()
{
    static int step = 1;
    if (step == 1)
    {
        changeColor(255, 0, 0);
        step = 2;
    }
    else if (step == 2)
    {
        changeColor(0, 255, 0);
        step = 3;
    }
    else
    {
        changeColor(0, 0, 255);
        step = 1;
    }
    delay(1000);
}

void changeSmooth()
{
    static int r = 0, b = 255, g = 0, mode = 1;
    changeColor(r, g, b);
    switch (mode)
    {
    case 1:
        if (r < 255)
            r++;
        else
            mode = 2;
        break;
    case 2:
        if (b > 0)
            b--;
        else
            mode = 3;
        break;
    case 3:
        if (g < 255)
            g++;
        else
            mode = 4;
        break;
    case 4:
        if (r > 0)
            r--;
        else
            mode = 5;
        break;
    case 5:
        if (b < 255)
            b++;
        else
            mode = 6;
        break;
    case 6:
        if (g > 0)
            g--;
        else
            mode = 1;
        break;
    default:
        mode = 1;
    }
    delay(9);
}

int hexToDec(String st)
{
    int cnt = 0;
    int digit;
    int decimalnumber = 0;
    for (int i = 1; i >= 0; i--)
    {
        switch (st[i])
        {
        case 'A':
        case 'a':
            digit = 10;
            break;
        case 'B':
        case 'b':
            digit = 11;
            break;
        case 'C':
        case 'c':
            digit = 12;
            break;
        case 'D':
        case 'd':
            digit = 13;
            break;
        case 'E':
        case 'e':
            digit = 14;
            break;
        case 'F':
        case 'f':
            digit = 15;
            break;
        default:
            digit = st[i] - 48;
        }
        decimalnumber = decimalnumber + (digit)*pow((double)16, (double)cnt);
        cnt++;
    }
    return decimalnumber;
}