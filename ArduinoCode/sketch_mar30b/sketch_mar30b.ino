//This works well for waiting for a command and returning one by string.  be sure to look for 
//the \n character both on input and output
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

String myString="";
void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available()){
    while(1){
      myString = Serial.readStringUntil('\n');
      //Serial.print(myString);
      // we have a complete command, execute it here:
      if (myString == "dowork"){
          Serial.println("hard work");
      }
      else if (myString == "better"){
          Serial.println("better code");
      }
      else if (myString == "AB"){
          Serial.println("AB");
      }
      else{
          Serial.println("error, no matching switch for:" + myString);
      }
      Serial.println("\n");
      return;
    }
  }
}
