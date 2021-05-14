# SmartAgricultureSystem

The INO arduino file : 

```
#include<ESP8266WiFi.h>
#include<DHT.h>
#include<FirebaseArduino.h>

#define ssid "hello"
#define password "chikusid9"
#define firebaseHost "studio42-first-app-default-rtdb.firebaseio.com"
#define firebaseAuth "tPPoGYE2nNRYyeNcOkLjAfmoQUKhhEP6WGV1NOuH"

#define DHTPIN 7
#define DHTTYPE DHT22

int dcMotorpositive;
int dcMotornegative;
int soilMoisturePin = A0;

int soilMoistureLevel;
int temperature;
int humidity;
int dcMotorstatus;

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  getConnection();
  Firebase.begin(firebaseHost, firebaseAuth);
  dht.begin();
}

void loop() {

  dcMotorstatus = readMotor();

  temperature = dht22Temperature();

  humidity = dht22Humidity();

  soilMoistureLevel = soilMoistureWriteFunction();
  
  //<!-- Send data to firebase code
  //send data to firebase
  Firebase.setFloat("Soil Moisture Sensor", soilMoistureLevel); // delay(3600000); for actual real world testing
  if(Firebase.failed()){
    Serial.print("setting /number failed:");
    Serial.println(Firebase.error());  
    return;
    }
  Firebase.setFloat("Temperature Sensor", temperature);
  if(Firebase.failed()){
    Serial.print("setting /number failed:");
    Serial.println(Firebase.error());  
    return;
    }
  Firebase.setFloat("Humidity Sensor", humidity);
  if(Firebase.failed()){
    Serial.print("setting /number failed:");
    Serial.println(Firebase.error());  
    return;
    }
    //send data to firebase code ends here --!>
    
  delay(1000); //for simulation purposes
}

//function to connect the board to WiFi
void getConnection(){
  WiFi.begin(ssid, password); 
  Serial.print("connecting"); 
  while (WiFi.status() != WL_CONNECTED) { 
    Serial.print("."); 
    delay(500); 
  } 
  Serial.println(); 
  Serial.print("connected: "); 
  Serial.println(WiFi.localIP());
}

//functions for temperature and humidity (update)
int dht22Temperature(){
  int temp;
  temp = dht.readTemperature();
  return temp;
}

int dht22Humidity(){
  int hum;
  hum = dht.readHumidity();
  return hum;
}

//function for soil moisture sensor (update)
int soilMoistureWriteFunction(){
  int soil;
  soil = analogRead(soilMoisturePin);
  return soil;
}

//function to read DC Motor Status (read)
int readMotor(){
  int dcStatus;
  dcStatus = Firebase.getFloat("DC Motor Status");
  return dcStatus;
}
```
