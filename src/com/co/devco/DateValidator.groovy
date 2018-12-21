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
    
    boolean compareDays(String today, String executionDay){
        executionDay = executionDay.toLowerCase()
        if (executionDay == "all"){
            today = executionDay
        }
        boolean resp = today.equalsIgnoreCase(executionDay)
        return resp
    }

    boolean compareDayAndDays(String day, List<String> days){
        boolean resp = false
        resp = days.any{ element -> this.compareDays(day, element) } 
        return resp
    }

    //TODO: Test this method using Mocks and refactor
    boolean isDayAndTimeToExecute(String executionDays, String executionTimes){
        DateUtils dateUtils = new DateUtils()
        DateValidator dateValidator = new DateValidator()

        print("llamando current time")
        String currentTime = dateUtils.getCurrentTimeStr()
        print("llamando current time: " + currentTime)
        String dayOfMonthStr = dateUtils.getTodayOfMonthStr()
        print("llamando dayOfMonthStr: " + dayOfMonthStr)
        List<String> executionDaysList = dateUtils.transformStringToListString(executionDays)
        List<String> timeRangesList = dateUtils.transformStringToListString(executionTimes)
        List<Map.Entry<String, String>> timesRangesMapList = dateUtils.transformTimesListTotimeRanges(timeRangesList)

        boolean resp = !this.compareDayAndDays(dayOfMonthStr, executionDaysList) || !(this.isTimeBetweenTimeRangesList(timesRangesMapList, currentTime))

        return resp
    }

}