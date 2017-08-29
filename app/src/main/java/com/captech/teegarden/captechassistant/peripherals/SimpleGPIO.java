package com.captech.teegarden.captechassistant.peripherals;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by teegarcs on 8/24/17.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class SimpleGPIO implements AutoCloseable {

    private static final String TAG = SimpleGPIO.class.getSimpleName();

    private Gpio led;

    public enum InitialState {
        ON(Gpio.DIRECTION_OUT_INITIALLY_HIGH), OFF(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        final int direction;

        InitialState(int direction) {
            this.direction = direction;
        }
    }

    public SimpleGPIO(String pinName, InitialState initialState) throws Exception {
        PeripheralManagerService managerService = new PeripheralManagerService();
        try {
            Gpio led = managerService.openGpio(pinName);
            connect(led, initialState);
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    public void toggleGPIO(boolean on) {
        try {
            led.setValue(on);
        } catch (IOException e) {
            Log.e(TAG, "Error setting LED value", e);
        }
    }

    public boolean isOn(){
        try {
            return led.getValue();
        } catch (IOException ignored) {
            return false;
        }
    }

    private void connect(Gpio led, InitialState initialState) throws IOException {
        this.led = led;
        this.led.setDirection(initialState.direction);
    }

    @Override public void close() throws Exception {
        if (led != null) {
            try {
                led.close();
            } finally {
                led = null;
            }
        }
    }
}