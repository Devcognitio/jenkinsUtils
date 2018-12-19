package com.co.devco

import java.time.Duration
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit

import static java.time.temporal.TemporalAdjusters.nextOrSame

class DateUtils {
    Long secondsToNextInputDay(String day, String initialTime, LocalDate today = LocalDate.now(), LocalTime actualTime = LocalTime.now()){
        day = day.toUpperCase()
        int dayToday = today.getDayOfWeek().getValue()
        LocalTime startTime = LocalTime.parse( initialTime )

        HashMap<String, Integer> days = new HashMap<String, Integer>()
        days.put('MONDAY', 1)
        days.put('TUESDAY', 2)
        days.put('WEDNESDAY', 3)
        days.put('THURSDAY', 4)
        days.put('FRIDAY', 5)
        days.put('SATURDAY', 6)
        days.put('SUNDAY', 7)
        days.put('ALL', dayToday)

        Integer dayInt = days.get(day)
        final LocalDate nextInputDay = today.with(nextOrSame(DayOfWeek.of(dayInt)))

        Duration duration = Duration.between(today.atTime(actualTime), nextInputDay.atTime(startTime))

        Long durationL = duration.getSeconds()
        if (durationL < 0L){
            durationL = 10800L
        }else if (durationL <= 3600L){
            durationL = 300L
        }else if (durationL <= 10800L){
            durationL = 3600L
        }else if (durationL <= 86400L){
            durationL = 10800L
        }else{
            durationL = durationL - 86400L
        }

        return durationL
    }

    String getNextStartTime(List<Map.Entry<String, String>> timeRangesList, String currentTime){
        String nextStartTime = currentTime
        LocalTime target = LocalTime.parse( currentTime )
        Duration durationNextStartTime = Duration.of(23, ChronoUnit.HOURS)

        timeRangesList.each { Map.Entry<String, String> pair ->
            LocalTime start = LocalTime.parse( pair.key )
            Duration duration = Duration.between(target, start)
            if (duration.getSeconds() > 0L && duration <= durationNextStartTime){
                durationNextStartTime = duration
                nextStartTime = pair.key
            }
        }
        return nextStartTime
    }
}
