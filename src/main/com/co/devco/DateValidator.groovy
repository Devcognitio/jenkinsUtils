package com.co.devco

import java.time.LocalTime

class DateValidator{

    boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime){
        LocalTime start = LocalTime.parse( initialTime )
        LocalTime stop = LocalTime.parse( finalTime )
        LocalTime target = LocalTime.parse( currentTime )

        boolean isTargetAfterStartAndBeforeStop = ( target.isAfter( start ) && target.isBefore( stop ) ) || (target == start) || (target == stop)

        return isTargetAfterStartAndBeforeStop
    }

    boolean isTimeBetweenTimeRangesList(List<Map.Entry<String, String>> timeRangesList, String currentTime){
        timeRangesList.any { Map.Entry<String, String> pair ->
            boolean result = isTimeBetweenTwoTime(pair.getKey(), pair.getValue(), currentTime)
            if (result){
                return true
            }
        }
    }

}