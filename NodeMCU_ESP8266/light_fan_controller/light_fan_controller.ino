#include "hw_timer.h"
#include <ESP8266WiFi.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>   
#include "DHT.h"
#define DHTTYPE DHT11  

const int dhtPin  = D2; 
DHT dht(dhtPin, DHTTYPE);
float hum = 0.0;
float temp = 0.0;
String sensorStatus;

const byte zcPin = 12;//D6
const byte pwmPin = 13;//D7 
const int light1 = D3;
const int light2 = D4;

int light1IsOn = 0;
int light2IsOn = 0;
bool lightChange = true;

byte fade = 1;
byte state = 1;
byte tarSpeed = 0;// max 255
byte curSpeed = 0;
byte zcState = 0; // 0 = ready; 1 = processing;




const byte DNS_PORT = 53;
IPAddress apIP(172, 217, 28, 1);
DNSServer dnsServer;
ESP8266WebServer webServer(80);

void setup() {  
  pinMode(zcPin, INPUT_PULLUP);
  pinMode(light1, OUTPUT);
  pinMode(light2, OUTPUT);
  pinMode(pwmPin, OUTPUT);
  attachInterrupt(zcPin, zcDetectISR, RISING);    // Attach an Interupt to Pin 2 (interupt 0) for Zero Cross Detection
  hw_timer_init(NMI_SOURCE, 0);
  hw_timer_set_func(dimTimerISR);
  Serial.begin(9600);


  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(apIP, apIP, IPAddress(255, 255, 255, 0));
  WiFi.softAP("Tonmoy's Home","vb67opst123");


  dnsServer.setTTL(300);
  dnsServer.setErrorReplyCode(DNSReplyCode::ServerFailure);
  dnsServer.start(DNS_PORT, "tonmoyhomeapi.abc", apIP);
  webServer.onNotFound(homeStatus);
  restServerRouting();
  
  webServer.begin();
  
}
void fanControl(){
  int fanSpeed =0;
  bool isOn = false;
  String res ="{\"message\":\"success\",";
  fanSpeed = webServer.arg("speed").toInt();
  isOn = webServer.arg("isOn").toInt();
  
  res += "\"Device\":\"Fan\",";
  if(isOn){
    state = 1;
    tarSpeed = fanSpeed;
    res += "\"speed\":";
    res += tarSpeed;
    res += ",";
    res += "\"isOn\":";
    res += state;
  }else{
    state = 0;
    res += "\"speed\":";
    res += tarSpeed;
    res += ",";
    res += "\"isOn\":";
    res += state;
  }
  res += "}";
  webServer.send(200,"text/json",res);
}
void ligh1Control(){
  lightChange = true;
  bool isOn = false;
  isOn = webServer.arg("isOn").toInt();
  light1IsOn = isOn;
  String res ="{\"message\":\"success\",";
  res += "\"Device\":\"Light1\",";
  res += "\"isOn\":";
  res += light1IsOn;
  res += "}";
  webServer.send(200,"text/json",res);
  
}
void ligh2Control(){
  lightChange = true;
  bool isOn = false;
  isOn = webServer.arg("isOn").toInt();
  light2IsOn = isOn;
  String res ="{\"message\":\"success\",";
  res += "\"Device\":\"Light2\",";
  res += "\"isOn\":";
  res += light2IsOn;
  res += "}";
  webServer.send(200,"text/json",res);
  
}
void homeStatus(){
  String res  ="{\"message\":\"success\",";\
  res += "\"isLightOneOn\":";
  res += light1IsOn;
  res += ",";

  res += "\"isLightTwoOn\":";
  res += light2IsOn;
  res += ",";

  res += "\"isFanOn\":";
  res += state;
  res += ",";
  
  res += "\"fanSpeed\":";
  res += tarSpeed;
  res += "}";
  
   webServer.send(200, "text/json", res);
}
void postSensorData(){
  String res  ="{\"message\":";
  res += "\"";
  res += sensorStatus;
  res += "\",";
  res += "\"temperature\":";
  res += temp;
  res += ",";

  res += "\"humidity\":";
  res += hum;
  
  res += "}";
  
   webServer.send(200, "text/json", res);
}
void restServerRouting() {
    webServer.on("/", HTTP_GET,homeStatus);
    webServer.on("/fan", HTTP_POST,fanControl);
    webServer.on("/light1", HTTP_POST,ligh1Control);
    webServer.on("/light2", HTTP_POST,ligh2Control);
    webServer.on("/status", HTTP_POST,homeStatus);
    webServer.on("/readSensorData", HTTP_POST,postSensorData);
}
void loop() {
 
  dnsServer.processNextRequest();
  webServer.handleClient();
  controlLight();
  readDHT11();

}
void readDHT11(){
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  if (!(isnan(t) || isnan(h))) {
    sensorStatus = "success";
     hum = h;
     temp = t;
  }else{
    sensorStatus = "Failed";
  }

}
void controlLight(){
  
  if(lightChange){
  if(light1IsOn){
    digitalWrite(light1,HIGH);
  }else{
    digitalWrite(light1,LOW);
  }
  if(light2IsOn){
     digitalWrite(light2,HIGH);
  }else{
     digitalWrite(light2,LOW);
  }
  lightChange = false;
  }
}


void dimTimerISR() {
    if (fade == 1) {
      if (curSpeed > tarSpeed || (state == 0 && curSpeed > 0)) {
        --curSpeed;
      }
      else if (curSpeed < tarSpeed && state == 1 && curSpeed < 255) {
        ++curSpeed;
      }
    }
    else {
      if (state == 1) {
        curSpeed = tarSpeed;
      }
      else {
        curSpeed = 0;
      }
    }
    
    if (curSpeed == 0) {
      state = 0;
      digitalWrite(pwmPin, 0);
    }
    else if (curSpeed == 255) {
      state = 1;
      digitalWrite(pwmPin, 1);
    }
    else {
      digitalWrite(pwmPin, 1);
    }
    
    zcState = 0;
  
}

ICACHE_RAM_ATTR void zcDetectISR() {
  if (zcState == 0) {
    zcState = 1;
  
    if (curSpeed < 255 && curSpeed > 0) {
      digitalWrite(pwmPin, 0);
      
      int dimDelay = 30 * (255 - curSpeed) + 400;//400
      hw_timer_arm(dimDelay);
    }
  }
}
