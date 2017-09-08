package com.example.androidthings.myproject;

import com.google.android.things.pio.Gpio;

/**
 * Template for IDD Fall 2017 HW2 (text entry device)
 * Created by bjoern on 9/5/17.
 */

public class IDDTextEntryApp extends SimplePicoPro {
    private long[] tStart = new long[10];
    private long[] tEnd = new long[10];
    private int[] counter = new int[10];
    private boolean[] pressed = new boolean[10];
    private char[][] map = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
            {'j', 'k', 'l'},
            {'m', 'n', 'o'},
            {'p', 'q', 'r'},
            {'s', 't', 'u', 'v'},
            {'w', 'x', 'y', 'z'},
    };
    private Gpio[] gpArray = {GPIO_128, GPIO_39, GPIO_37, GPIO_35, GPIO_34, GPIO_33, GPIO_32, GPIO_10, GPIO_174, GPIO_175};

    @Override
    public void setup() {
        //set two GPIOs to input
        for (int i = 0; i < gpArray.length; i ++) {
            pinMode(gpArray[i], Gpio.DIRECTION_IN);
            setEdgeTrigger(gpArray[i],Gpio.EDGE_BOTH);
        }
    }

    @Override
    public void loop() {
        long tCur = System.currentTimeMillis();

        for (int i = 0; i < tStart.length - 2; i ++) {
            if (tStart[i] != 0 && tCur - tStart[i] > 250) {
                if (map[i].length > counter[i]) {
                    printCharacterToScreen(map[i][counter[i]]);
                }
                counter[i] = 0;
                tStart[i] = 0;
            }
        }

        if (pressed[8] && tCur - tStart[8] > 300)
            printCharacterToScreen(' ');
        if (pressed[9] && tCur - tStart[9] > 300)
            deleteCharacterOnScreen();
    }

    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        println("digitalEdgeEvent"+pin+", "+value);
        // when 128 goes from LOW to HIGH
        // this is on button button release for pull-up resistors
        // pressed == LOW
        if(pin==gpArray[0] && value==LOW)       setTime(0);
        else if (pin==gpArray[1] && value==LOW) setTime(1);
        else if (pin==gpArray[2] && value==LOW) setTime(2);
        else if (pin==gpArray[3] && value==LOW) setTime(3);
        else if (pin==gpArray[4] && value==LOW) setTime(4);
        else if (pin==gpArray[5] && value==LOW) setTime(5);
        else if (pin==gpArray[6] && value==LOW) setTime(6);
        else if (pin==gpArray[7] && value==LOW) setTime(7);

        // pressed == HIGH
        else if (pin==gpArray[8] && value==HIGH) {
            printCharacterToScreen(' ');
            pressed[8] = true;
            setTime(8);
        }
        else if (pin==gpArray[8] && value==LOW) {
            pressed[8] = false;
            tStart[8] = 0;
        }
        else if (pin==gpArray[9] && value==HIGH) {
            deleteCharacterOnScreen();
            pressed[9] = true;
            setTime(9);
        }
        else if (pin==gpArray[9] && value==LOW) {
            pressed[9] = false;
            tStart[9] = 0;
        }
    }

    private void setTime (int p){
        if (tStart[p] == 0) {
            tStart[p] = System.currentTimeMillis();
        }
        else {
            long t = System.currentTimeMillis();
            if (t - tStart[p] < 200) {
                counter[p] += 1;
                tStart[p] = t;
            }
        }
    }
}
