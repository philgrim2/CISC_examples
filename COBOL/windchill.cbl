       IDENTIFICATION DIVISION.
       PROGRAM-ID. WINDCHILL.
              
       DATA DIVISION.
       WORKING-STORAGE SECTION.
       01 WS-TEMPERATURE      PIC S9(3)V99 VALUE 0.
       01 WS-WIND-SPEED       PIC 9(3)V99 VALUE 0.
       01 WS-WIND-CHILL       PIC S9(3)V99 VALUE 0.
       01 WS-TEMP-INPUT       PIC X(10).
       01 WS-WIND-INPUT       PIC X(10).
       01 WS-VALID-TEMP-INPUT PIC 9 VALUE 0.
       01 WS-VALID-WIND-INPUT PIC 9 VALUE 0.
       01 WS-CALC-TEMP        PIC S9(3)V99.
       01 WS-CALC-WIND        PIC 9(3)V99.
       01 WS-WIND-POWER       PIC 9(3)V99999.
       01 WS-TERM1            PIC S9(3)V99.
       01 WS-TERM2            PIC S9(3)V99.
       01 WS-TERM3            PIC S9(3)V99.
       01 WS-TERM4            PIC S9(3)V99.
       
       PROCEDURE DIVISION.
       
           PERFORM GET-TEMPERATURE.
           PERFORM GET-WIND-SPEED.
           PERFORM CALCULATE-WIND-CHILL.
           PERFORM DISPLAY-RESULT.
           STOP RUN.
       
       GET-TEMPERATURE.
           PERFORM UNTIL WS-VALID-TEMP-INPUT = 1
               DISPLAY "Enter temperature in Fahrenheit (-50 to 50): "
                   WITH NO ADVANCING
               ACCEPT WS-TEMP-INPUT
               MOVE 0 TO WS-VALID-TEMP-INPUT
               
               MOVE FUNCTION NUMVAL(WS-TEMP-INPUT) 
                   TO WS-TEMPERATURE
               
               IF FUNCTION TEST-NUMVAL(WS-TEMP-INPUT) = 0
                   IF WS-TEMPERATURE >= -50 AND 
                      WS-TEMPERATURE <= 50
                       MOVE 1 TO WS-VALID-TEMP-INPUT
                   ELSE
                       DISPLAY "Error: Temperature must be between "
                           "-50 and 50 degrees."
                   END-IF
               ELSE
                   DISPLAY "Error: Please enter a valid number."
               END-IF
           END-PERFORM.
       
       GET-WIND-SPEED.
           PERFORM UNTIL WS-VALID-WIND-INPUT = 1
               DISPLAY "Enter wind speed in miles per hour "
                   "(must be positive): "
                   WITH NO ADVANCING
               ACCEPT WS-WIND-INPUT
               MOVE 0 TO WS-VALID-WIND-INPUT
               
               MOVE FUNCTION NUMVAL(WS-WIND-INPUT) 
                   TO WS-WIND-SPEED
               
               IF FUNCTION TEST-NUMVAL(WS-WIND-INPUT) = 0
                   IF WS-WIND-SPEED > 0
                       MOVE 1 TO WS-VALID-WIND-INPUT
                   ELSE
                       DISPLAY "Error: Wind speed must be a "
                           "positive number."
                   END-IF
               ELSE
                   DISPLAY "Error: Please enter a valid number."
               END-IF
           END-PERFORM.
       
       CALCULATE-WIND-CHILL.
           MOVE WS-TEMPERATURE TO WS-CALC-TEMP.
           MOVE WS-WIND-SPEED TO WS-CALC-WIND.
           
      *    Wind Chill Formula:
      *    WC = 35.74 + 0.6215T - 35.75(V^0.16) + 0.4275T(V^0.16)
           
           COMPUTE WS-WIND-POWER = 
               WS-CALC-WIND ** 0.16.
           
           COMPUTE WS-TERM1 = 35.74.
           COMPUTE WS-TERM2 = 0.6215 * WS-CALC-TEMP.
           COMPUTE WS-TERM3 = 35.75 * WS-WIND-POWER.
           COMPUTE WS-TERM4 = 0.4275 * WS-CALC-TEMP * 
               WS-WIND-POWER.
           
           COMPUTE WS-WIND-CHILL = WS-TERM1 + WS-TERM2 - 
               WS-TERM3 + WS-TERM4.
       
       DISPLAY-RESULT.
           DISPLAY " ".
           DISPLAY "====== Wind Chill Calculation ======".
           DISPLAY "Temperature: " WS-TEMPERATURE 
               " degrees Fahrenheit".
           DISPLAY "Wind Speed: " WS-WIND-SPEED 
               " miles per hour".
           DISPLAY "Wind Chill: " WS-WIND-CHILL 
               " degrees Fahrenheit".
           DISPLAY "=====================================".
