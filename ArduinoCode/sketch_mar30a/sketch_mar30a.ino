//define states
const uint8_t STATE_pull_O2_pressurized = 1;
const uint8_t STATE_pull_O2_1ATM = 2;
const uint8_t STATE_pull_air = 3;
const uint8_t STATE_pump = 4;
const uint8_t STATE_paused = 5;
const uint8_t STATE_unpause = 6;

//system is paused by default
uint8_t SYSTEM_STATE = 5;
bool isRunAllowed = false;
bool patientTriggered = false;
bool isPatientTriggering = false;

String cmdstr;
String cmdType;
String cmdVal;

//parameters set by user interface
float FiO2 = 21; //units in percent aka 21-100%
float breathingRate = 20;
float peakInspPressure = 0;
float peepPressure = 5; //units in cmH20
float TidalVolume = 500; //units in ml
float inhaleTime = 1; //abritrary units related to eachother
float exhaleTime = 2; //abritrary units related to eachother




String commandRequest(){
  if(Serial.available() > 0){
    String cmd = Serial.readStringUntil('\n');
    return cmd;
  }
  return " ";
}

void setup() {
  Serial.begin(9600);//MAKE THIS HIGHER

  isRunAllowed = true;
}


void loop() 
{

  String newCommand = commandRequest();
  
  
  if(newCommand!=" "){
    
    cmdstr = newCommand;
    cmdType = String(cmdstr[0])+String(cmdstr[1]);
    cmdVal = String(cmdstr[3])+String(cmdstr[4])+String(cmdstr[5]);

  }

  if(cmdType=="01"){
    Serial.println("01");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="02"){
    Serial.println("02");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="11"){
    //set BreathingRate
    breathingRate = cmdVal.toInt();
    Serial.println("11");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="12"){
    //set FiO2
    FiO2 = cmdVal.toInt();
    Serial.println("12");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="13"){
    //set peakInspPressure
    peakInspPressure = cmdVal.toInt();
    Serial.println("13");
    cmdType = "";
    cmdVal = "";
  }

   else if(cmdType=="14"){
    //set TidalVolume
    TidalVolume = cmdVal.toInt();
    Serial.println("14");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="15"){
    //set Peep target NOT USED RIGHT NOW
    peepPressure = cmdVal.toInt();
    Serial.println("15");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="16"){
    //set IEratio
    inhaleTime = String(cmdVal[0]).toInt();
    exhaleTime = String(cmdVal[1]).toInt();
    Serial.println("16");
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="20"){
    //get breathing rate target
    Serial.println(int(breathingRate));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="21"){
    //get FiO2 target
    Serial.println(int(FiO2));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="22"){
    //get peakInspPressure
    Serial.println(int(peakInspPressure));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="23"){
    //get TidalVolume target
    Serial.println(int(TidalVolume));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="24"){
    //get peep target 
    Serial.println(int(peepPressure));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="25"){
    //get I:Eratio
    Serial.println(String(int(inhaleTime))+String(int(exhaleTime)));
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="30"){
    //set state to unpause
    SYSTEM_STATE = STATE_unpause;
    Serial.println("30");
    cmdType = "";
    cmdVal = "";
  }
   else if(cmdType=="31"){
    //set state to pause
    SYSTEM_STATE = STATE_paused;
    Serial.println("31");
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="32"){
    //get isrunallowed
    Serial.println(isRunAllowed);
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="40"){
    //get state is running?
    Serial.println(SYSTEM_STATE!=STATE_paused);
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="41"){
    //get state is paused?
    Serial.println(SYSTEM_STATE==STATE_paused);
    cmdType = "";
    cmdVal = "";
  }

  else if(cmdType=="50"){
    //get alarm?
    //Serial.print();
    //FUNCTINO NOT CURRENTLY SUPPORTED
  }
  else if(cmdType=="60"){
    //set patient triggered
    patientTriggered = true;
    //Serial.print("60");
    
  }
   else if(cmdType=="61"){
    //get if patient triggering is on
    //Serial.print(patientTriggering);
  }
  else if(cmdType=="62"){
    //get if patient has triggered
    Serial.println(isPatientTriggering);
    cmdType = "";
    cmdVal = "";
  }
  else if(cmdType=="70"){
    //silence alarm
    //Serial.print();
    //COMMAND NOT SUPPORTED RIGHT NOW
  }  
  
 

}

/**/
