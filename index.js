'use strict';

process.env.DEBUG = 'actions-on-google:*';

const Assistant = require('actions-on-google').ApiAiAssistant;
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const bonsaiStatus = admin.database().ref('bonsai_status');

// API.AI Intent names
const WATER_INTENT = 'water-duration';
const STOP_INTENT = 'stop-watering';

// Contexts
const WATER_CONTEXT = 'water_duration';
const STOP_CONTEXT = 'stop';


// Context Parameters
const DURATION_PARAM = 'duration';
const FULFILLMENT_PARAM = 'fulfillment';

var userRequest;


exports.assistantcodelab = functions.https.onRequest((request, response) => {
   const assistant = new Assistant({request: request, response: response});
    userRequest = request;
    
   let actionMap = new Map();
   actionMap.set(WATER_INTENT, water);
   actionMap.set(STOP_INTENT, stop)
   assistant.handleRequest(actionMap);

    /**
    *Code to start the watering process
    **/
   function water(assistant) {
       
       var d = new Date();
       var n = d.getTime();
       
       const duration = assistant.getContextArgument(WATER_CONTEXT, DURATION_PARAM).value;
    
       const numberTime = duration.amount;
       const unitTime = duration.unit;
       
      
       var timeInSeconds = 120;//default
       
       switch(unitTime){
           case "h":
               timeInSeconds = numberTime *60*60;
               break;
           case "s":
               timeInSeconds = numberTime;
               break;
           default:
               timeInSeconds = numberTime * 60;//default to unit being minutes
               break;
            }
        var currentStatus = {
            'status_date': 0,
            'watering': true,
            'watering_duration': 120
        };
       
        currentStatus.status_date = n;
        currentStatus.watering_duration = timeInSeconds;
       
        bonsaiStatus.push().set(currentStatus);
      
        assistant.tell(request.body.result.fulfillment.speech);
        }
    
    /**
    *Code to stop the watering process
    **/
    function stop(assistant) {
       
       var d = new Date();
       var n = d.getTime();
       
        var currentStatus = {
            'status_date': 0,
            'watering': false,
            'watering_duration': 0
        };
       
        currentStatus.status_date = n;
       
        bonsaiStatus.push().set(currentStatus);
      
        assistant.tell(request.body.result.fulfillment.speech);
        }

});