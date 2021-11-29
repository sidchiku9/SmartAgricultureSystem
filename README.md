# SmartAgricultureSystem

The INO arduino file : 

```c++
#include <DHT.h>
#include <Stepper.h>

const int stepsPerRevolution = 90;
#define DHTPIN 7  
#define DHTTYPE DHT22   
DHT dht(DHTPIN, DHTTYPE);
const int dry = 595;
const int wet = 239;
int chk;
float hum; 
float temp;
Stepper myStepper(stepsPerRevolution, 9, 10, 11, 12);
#define ssid "hello"
#define password "chikusid9"
#define firebaseHost "studio42-first-app-default-rtdb.firebaseio.com"
#define firebaseAuth "tPPoGYE2nNRYyeNcOkLjAfmoQUKhhEP6WGV1NOuH"

void setup() {
    Serial.begin(9600);
    dht.begin();
    //getConnection();
    myStepper.setSpeed(5);
}

void loop() {
  calcSoilMoisture();
  myStepper.step(stepsPerRevolution);
  readTempAndHum();
  //sendtoFirebase();
  delay(2000);
}

void calcSoilMoisture(){
  int sensorVal = analogRead(A0);
  int percentageHumidity = map(sensorVal, wet, dry, 100, 0);
  Serial.print("Soil Moisture");
  Serial.print(percentageHumidity);
  Serial.println("%");
}

void readTempAndHum(){
    hum = dht.readHumidity();
    temp= dht.readTemperature();
    Serial.print("Humidity: ");
    Serial.print(hum);
    Serial.print(" %, Temp: ");
    Serial.print(temp);
    Serial.println(" Celsius");
}

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

void sendtoFirebase(){
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
 }
```
