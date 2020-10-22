//ARDUINO 1.0+ ONLY
//ARDUINO 1.0+ ONLY
#include <Ethernet.h>
#include <SPI.h>
#include <Servo.h>
#define LIGHT 7
#define FAN 8
#define DOOR 9


Servo myservo; // create servo object to control a servo
// twelve servo objects can be created on most boards



////////////////////////////////////////////////////////////////////////
//CONFIGURE
////////////////////////////////////////////////////////////////////////
byte server[] = { 192,168,0,150 }; //ip Address of the server you will connect to

//The location to go to on the server
//make sure to keep HTTP/1.0 at the end, this is telling it what type of file it is
String location = " HTTP/1.0";


// if need to change the MAC address (Very Rare)
byte mac[] = { 0xC4, 0x8E, 0x8F, 0xA9, 0x2C, 0x99 };
////////////////////////////////////////////////////////////////////////

EthernetClient client;

char inString[32]; // string for incoming serial data
int stringPos = 0; // string index counter
boolean startRead = false; // is reading?
int pos = 0;   // variable to store the servo position


void setup(){
  Ethernet.begin(server);
  Serial.begin(9600);
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object

  pinMode(LIGHT, OUTPUT);
  pinMode(FAN, OUTPUT);
   pinMode(DOOR, OUTPUT);
  
}

void loop(){
  String pageValue = connectAndRead(); //connect to the server and read the output
  //String pageValue = "100";
  const char* cstr = pageValue.c_str();
  Serial.print("TEST: ");
  Serial.println(pageValue); //print out the findings.
  int num=atoi(cstr);
  switch( num){
    case 000: digitalWrite(LIGHT,LOW);
              digitalWrite(FAN,LOW);
              doorOff();
              break;

  case 001: digitalWrite(LIGHT,LOW);
              digitalWrite(FAN,LOW);
              doorOn();
              break;
  case 010: digitalWrite(LIGHT,LOW);
              digitalWrite(FAN,HIGH);
              doorOff();
              break;

  case 011: digitalWrite(LIGHT,LOW);
              digitalWrite(FAN,HIGH);
              doorOn();
              break;
  case 100: digitalWrite(LIGHT,HIGH);
              digitalWrite(FAN,LOW);
              doorOff();
              break;

  case 101: digitalWrite(LIGHT,HIGH);
              digitalWrite(FAN,LOW);
              doorOn();
              break;
  case 110: digitalWrite(LIGHT,HIGH);
              digitalWrite(FAN,HIGH);
              doorOff();
              break;
   case 111: digitalWrite(LIGHT,HIGH);
              digitalWrite(FAN,HIGH);
              doorOn();
              break;
  }
  

  delay(5000); //wait 5 seconds before connecting again
}

String connectAndRead(){
  //connect to the server

  Serial.println("connecting...");

  //port 80 is typical of a www page
  if (client.connect(server, 80)) {
    Serial.println("connected");
    client.print("GET /readtxt.php?");
    client.println(location);
    client.println();

    //Connected - Read the page
    return readPage(); //go and read the output

  }else{
    return "connection failed";
    
  }

}
void doorOn(){
    
    myservo.write(90);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
 
}
void doorOff(){
 // goes from 180 degrees to 0 degrees
    myservo.write(0);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  
}

String readPage(){
  //read the page, and capture & return everything between '<' and '>'

  stringPos = 0;
  memset( &inString, 0, 32 ); //clear inString memory

  while(true){

    if (client.available()) {
      char c = client.read();

      if (c == '<' ) { //'<' is our begining character
        startRead = true; //Ready to start reading the part 
      }else if(startRead){

        if(c != '>'){ //'>' is our ending character
          inString[stringPos] = c;
          stringPos ++;
        }else{
          //got what we need here! We can disconnect now
          startRead = false;
          //client.println(inString);
          client.stop();
          client.flush();
          Serial.println("disconnecting.");
          return inString;

        }

      }
    }

  }

}
