package com.captech.teegarden.captechassistant.data;

import com.google.firebase.database.DataSnapshot;

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
public class BonsaiStatus {

    public long statusDate;//current date & time in ms.
    public boolean watering;//are we currently watering?
    public long watering_duration;//if we are currently watering, how long do we want to water in seconds?

    public BonsaiStatus(){

    }

    public BonsaiStatus(long statusDate){
        this.statusDate = statusDate;
        this.watering = false;
        this.watering_duration = 0;
    }

    public BonsaiStatus(long statusDate, boolean watering, long watering_duration){
        this.statusDate = statusDate;
        this.watering = watering;
        this.watering_duration = watering_duration;
    }

    /**
     * Method to pull the data from the snap shot and return the object.
     * @param dataSnapshot
     * @return
     */
    public static BonsaiStatus getStatusFromSnapShot(DataSnapshot dataSnapshot){
        BonsaiStatus currentStatus = new BonsaiStatus();
        try{
            currentStatus.statusDate = (Long) dataSnapshot.child("status_date").getValue();
            currentStatus.watering = (Boolean) dataSnapshot.child("watering").getValue();
            currentStatus.watering_duration = (Long) dataSnapshot.child("watering_duration").getValue();
        }catch (Exception ignored){

        }

        return currentStatus;

    }
}
