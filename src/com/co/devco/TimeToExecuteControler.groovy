package com.co.devco

import com.co.devco.DateUtils
import com.co.devco.DateValidator

class TimeToExecuteControler {
    
    def waitTime(String executionDays, String timeRanges){
        DateUtils dateUtils = new DateUtils()
        DateValidator dateValidator = new DateValidator()

        //TODO: See is is necesary create de calendar here and pass it to others methods, because the diferents seconds between every calendar that we are creating
        while (isDayAndTimeToExecute(executionDays, timeRanges)) {
            //TODO: refactor this part        
            String nextStartTime = dateUtils.getNextStartTime(timeRangesList, currentTime)
            Long secondsToSleep = dateUtils.secondsToNextInputDay(deployDay, nextStartTime)
            sleep secondsToSleep
        }
    }
}
