#include <Servo.h>
#include <MeetAndroid.h>
#include <SoftwareSerial.h>
//74HC595 shift register 
#define MOTOR_LATCH 12
#define MOTOR_CLK 4
#define MOTOR_ENABLE 7
#define MOTOR_DATA 8
// Only _A is used
#define MOTOR1_A 2
#define MOTOR1_B 3
#define MOTOR2_A 1
#define MOTOR2_B 4
#define MOTOR3_A 5
#define MOTOR3_B 7
#define MOTOR4_A 0
#define MOTOR4_B 6
#define MOTOR1_PWM 11
#define MOTOR2_PWM 3
#define MOTOR3_PWM 6
#define MOTOR4_PWM 5
#define SERVO1_PWM 10
#define SERVO2_PWM 9
#define FORWARD 1
#define BACKWARD 2
#define BRAKE 3
#define RELEASE 4

static int STICK_NONE = 0;
static int STICK_UP = 1;
static int STICK_UPRIGHT = 2;
static int STICK_RIGHT = 3;
static int STICK_DOWNRIGHT = 4;
static int STICK_DOWN = 5;
static int STICK_DOWNLEFT = 6;
static int STICK_LEFT = 7;
static int STICK_UPLEFT = 8; 

Servo servo_1;
Servo servo_2;

//SoftwareSerial  mySerial(2,3); // 設定對應bt的 rx 與 tx 接腳
MeetAndroid meetAndroid;
int Distance;
int Direction;
int  Angle;

void setup()
{
  Serial.begin(9600);
  //Serial.println("start:");
  //mySerial.begin(9600);
  servo_1.attach(SERVO1_PWM);
  servo_2.attach(SERVO2_PWM);
  meetAndroid.registerFunction(SetDirection, 'o');
  meetAndroid.registerFunction(SetDistance, 'A');
}

void loop()
{

  //motor(1, FORWARD, 255);
  //motor(2, FORWARD, 255);
  //motor(3, FORWARD, 255);
  //motor(4, FORWARD, 255);
  //delay(2000);
  // Be friendly to the motor: stop it before reverse.
  //motor(1, RELEASE, 0);
  //motor(2, RELEASE, 0);
  //motor(3, RELEASE, 0);
  //motor(4, RELEASE, 0);
  //delay(100);
  //motor(1, BACKWARD, 128);
  //motor(2, BACKWARD, 128);
  //motor(3, BACKWARD, 128);
  //motor(4, BACKWARD, 128);
  //delay(2000);
  //motor(1, RELEASE, 0);
  //motor(2, RELEASE, 0);
  //motor(3, RELEASE, 0);
  //motor(4, RELEASE, 0);
  meetAndroid.receive(); // you need to keep this in your loop() to receive events
  delay(100);
}

void SetDistance(byte flag, byte numOfValues)
{
  Distance = meetAndroid.getInt(); 
}

void SetDirection(byte flag, byte numOfValues)
{
  meetAndroid.send("ok");
  Direction = meetAndroid.getInt(); 
  //Serial.print("Direction:");
  //Serial.println(Direction);
  // Serial.print("Distance:");
  //Serial.println(Distance);
  
       //motor(3, FORWARD, 255);
       //motor(4, FORWARD, 255);
       //delay(2000);
       //motor(3, RELEASE, 0);
       //motor(4, RELEASE, 0);
    if(Direction == STICK_NONE)
    {
       motor(3, RELEASE, 0);
       motor(4, RELEASE, 0);
    }
    else if(Direction == STICK_UP)
    {
       motor(3, FORWARD, Distance);
       motor(4, FORWARD, Distance);
    }   
    else if(Direction == STICK_UPRIGHT)
    {
       motor(3, FORWARD, Distance/2);
       motor(4, FORWARD, Distance);
    }   
    else if(Direction == STICK_RIGHT)
    {
       motor(3, BACKWARD, Distance);
       motor(4, FORWARD, Distance);
    }   
    else if(Direction == STICK_DOWNRIGHT)
    {
       motor(3, BACKWARD, Distance/2);
       motor(4, BACKWARD, Distance);
    }   
    else if(Direction == STICK_DOWN)
    {
       motor(3, BACKWARD, Distance);
       motor(4, BACKWARD, Distance);
    }   
    else if(Direction == STICK_DOWNLEFT)
    {
       motor(3, BACKWARD, Distance);
       motor(4, BACKWARD, Distance/2);
    }   
    else if(Direction == STICK_LEFT){
       motor(3, FORWARD, Distance);
       motor(4, BACKWARD, Distance);
    }   
    else if(Direction == STICK_UPLEFT)
    {
       motor(3, FORWARD, Distance);
       motor(4, FORWARD, Distance/2);
    }
   
}



void motor(int nMotor, int command, int speed)
{
  int motorA, motorB;
  if (nMotor >= 1 && nMotor <= 4)
  {
    switch (nMotor)
    {
      case 1:
        motorA = MOTOR1_A;
        motorB = MOTOR1_B;
        break;
      case 2:
        motorA = MOTOR2_A;
        motorB = MOTOR2_B;
        break;
      case 3:
        motorA = MOTOR3_A;
        motorB = MOTOR3_B;
        break;
      case 4:
        motorA = MOTOR4_A;
        motorB = MOTOR4_B;
        break;
      default:
        break;
    }
    
    switch (command)
    {
      case FORWARD:
        motor_output (motorA, HIGH, speed);
        motor_output (motorB, LOW, -1); // -1: no PWM set
        break;
      case BACKWARD:
        motor_output (motorA, LOW, speed);
        motor_output (motorB, HIGH, -1); // -1: no PWM set
        break;
      case BRAKE:
        motor_output (motorA, LOW, 255); // 255: fully on.
        motor_output (motorB, LOW, -1); // -1: no PWM set
        break;
      case RELEASE:
        motor_output (motorA, LOW, 0); // 0: output floating.
        motor_output (motorB, LOW, -1); // -1: no PWM set
        break;
      default:
        break;
    }
  }
}

void motor_output (int output, int high_low, int speed)
{
  int motorPWM;
  switch (output)
  {
    case MOTOR1_A:
    case MOTOR1_B:
      motorPWM = MOTOR1_PWM;
      break;
    case MOTOR2_A:
    case MOTOR2_B:
      motorPWM = MOTOR2_PWM;
      break;
    case MOTOR3_A:
    case MOTOR3_B:
      motorPWM = MOTOR3_PWM;
      break;
    case MOTOR4_A:
    case MOTOR4_B:
      motorPWM = MOTOR4_PWM;
      break;
    default:
      speed = -3333;
      break;
  }
  if (speed != -3333)
  {
    shiftWrite(output, high_low);
    // set PWM only if it is valid
    if (speed >= 0 && speed <= 255)
    {
      analogWrite(motorPWM, speed);
    }
  }
}

void shiftWrite(int output, int high_low)
{
  static int latch_copy;
  static int shift_register_initialized = false;
  // Do the initialization on the fly,
  // at the first time it is used.
  if (!shift_register_initialized)
  {
    // Set pins for shift register to output
    pinMode(MOTOR_LATCH, OUTPUT);
    pinMode(MOTOR_ENABLE, OUTPUT);
    pinMode(MOTOR_DATA, OUTPUT);
    pinMode(MOTOR_CLK, OUTPUT);
    // Set pins for shift register to default value (low);
    digitalWrite(MOTOR_DATA, LOW);
    digitalWrite(MOTOR_LATCH, LOW);
    digitalWrite(MOTOR_CLK, LOW);
    // Enable the shift register, set Enable pin Low.
    digitalWrite(MOTOR_ENABLE, LOW);
    // start with all outputs (of the shift register) low
    latch_copy = 0;
    shift_register_initialized = true;
  }
  // The defines HIGH and LOW are 1 and 0.
  // So this is valid.
  bitWrite(latch_copy, output, high_low);
  digitalWrite(MOTOR_LATCH, LOW);
  shiftOut(MOTOR_DATA, MOTOR_CLK, MSBFIRST, latch_copy);
  delayMicroseconds(5); // For safety, not really needed.
  digitalWrite(MOTOR_LATCH, HIGH);
  delayMicroseconds(5); // For safety, not really needed.
  //  digitalWrite(MOTOR_LATCH, LOW);
}

