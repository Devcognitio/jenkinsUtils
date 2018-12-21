package com.co.devco

import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import static org.hamcrest.text.MatchesPattern.*

class DateUtilsTest {

    DateUtils dateUtils = new DateUtils()

    @Test
    void secondsToNextInputDayShouldReturnMoreThat0Seconds() {
        Long resp = dateUtils.secondsToNextInputDay("Sunday", "00:00:00")

        assertThat(resp, greaterThanOrEqualTo(0L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5MinsIfNextDayIsTodayAndTimeIsLessThat10Minutes(){
        final LocalDate today = LocalDate.now()
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(today.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:05:00", today, timeNow)

        assertThat(resp, is(300L))
    }

    @Test
    void secondsToNextInputDayShouldReturn1HoursIfNextDayIsTodayAndTimeIsIn3Hours(){
        final LocalDate today = LocalDate.now()
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(today.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"03:00:00", today, timeNow)

        assertThat(resp, is(3600L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5MinsIfNextDayIsTodayAndTimeIsIn1Hour(){
        final LocalDate today = LocalDate.now()
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(today.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"01:00:00", today, timeNow)

        assertThat(resp, is(300L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5MinsIfNextDayIsTodayAndTimeIsIn1HourOrLess(){
        final LocalDate today = LocalDate.now()
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(today.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:30:00", today, timeNow)

        assertThat(resp, is(300L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5MinsIfNextDayIsTodayExactly(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalDate nextDay = LocalDate.of(2018, 9, 19)
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(nextDay.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(300L))
    }

    @Test
    void secondsToNextInputDayShouldReturn3hoursIfNextDayIsTomorrowExactly(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalDate nextDay = LocalDate.of(2018, 9, 20)
        final LocalTime timeNow = LocalTime.of(00, 00, 00)
        String nextDayString = String.valueOf(nextDay.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(10800L))
    }

    @Test
    void secondsToNextInputDayShouldReturn3hoursIfNextDayIsTodayAndTimeIsGreaterThanInitialTime(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalDate nextDay = LocalDate.of(2018, 9, 19)
        final LocalTime timeNow = LocalTime.of(02, 00, 00)
        String nextDayString = String.valueOf(nextDay.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(10800L))
    }

    @Test
    void secondsToNextInputDayShouldReturn3hoursIfNextDayIsTomorrowMinus4Hours(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalDate nextDay = LocalDate.of(2018, 9, 20)
        final LocalTime timeNow = LocalTime.of(4, 00, 00)
        String nextDayString = String.valueOf(nextDay.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(10800L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5DaysMinus1DayIfNextDayIsIn5DaysExactly(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalDate nextDay = LocalDate.of(2018, 9, 24)
        final LocalTime timeNow = LocalTime.of(0, 00, 00)
        String nextDayString = String.valueOf(nextDay.dayOfWeek)

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(345600L))
    }

    @Test
    void secondsToNextInputDayShouldReturn5MinsIfDayIsAll(){
        final LocalDate today = LocalDate.of(2018, 9, 19)
        final LocalTime timeNow = LocalTime.of(0, 00, 00)
        String nextDayString = "ALL"

        Long resp = dateUtils.secondsToNextInputDay(nextDayString,"00:00:00", today, timeNow)

        assertThat(resp, is(300L))
    }

    @Test
    void secondsToNextInputDayShouldReturnAlmost5MinsIfDayIsAllWithOutTodayInput(){
        String nextDayString = "ALL"

        Long resp = dateUtils.secondsToNextInputDay(nextDayString, "00:00:00")

        assertThat(resp, greaterThanOrEqualTo(300L))
    }

    @Test
    void getNextStartTimeShouldReturnCurrentTimeIfRangeListIsEmpty(){
        String currentTime = "00:00:00"
        List<Map.Entry<String, String>> timeRangesList = new ArrayList<Map.Entry<String, String>>()

        String nextStartTime = dateUtils.getNextStartTime(timeRangesList, currentTime)

        assertThat(nextStartTime, is(currentTime))
    }

    @Test
    void getNextStartTimeShouldReturnFirstTimeIfCurentTimeIsNearToFirstTime(){
        String currentTime = "00:00:00"
        List<Map.Entry<String, String>> timeRanges = new ArrayList<Map.Entry<String, String>>()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("02:00:00", "03:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("04:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))

        String nextStartTime = dateUtils.getNextStartTime(timeRanges, currentTime)

        assertThat(nextStartTime, is("02:00:00"))
    }

    @Test
    void getNextStartTimeShouldReturnSecondTimeIfCurentTimeIsNearToSecondTime(){
        String currentTime = "03:00:00"
        List<Map.Entry<String, String>> timeRanges = new ArrayList<Map.Entry<String, String>>()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("02:00:00", "03:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("04:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))

        String nextStartTime = dateUtils.getNextStartTime(timeRanges, currentTime)

        assertThat(nextStartTime, is("04:00:00"))
    }

    @Test
    void getNextStartTimeShouldReturnThirdTimeIfCurentTimeIsNearToThirdTime(){
        String currentTime = "05:20:00"
        List<Map.Entry<String, String>> timeRanges = new ArrayList<Map.Entry<String, String>>()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("02:00:00", "03:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("04:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))

        String nextStartTime = dateUtils.getNextStartTime(timeRanges, currentTime)

        assertThat(nextStartTime, is("23:00:00"))
    }

    @Test
    void getNextStartTimeShouldReturnCurrenTimeIfCurentTimeGreaterThanLastStartTime(){
        String currentTime = "23:20:00"
        List<Map.Entry<String, String>> timeRanges = new ArrayList<Map.Entry<String, String>>()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("02:00:00", "03:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("04:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))

        String nextStartTime = dateUtils.getNextStartTime(timeRanges, currentTime)

        assertThat(nextStartTime, is("23:20:00"))
    }

    @Test
    void transformStringToListStringShouldReturnACorrectListWhenDaysHaveData(){
        String days = 'monday, saturday,friday'
        List<String> resp = dateUtils.transformStringToListString( days )
        assertThat(resp, hasSize(3))
        assertThat(resp, containsInAnyOrder('monday', 'saturday', 'friday'))
    }

    @Test
    void transformStringToListStringShouldReturnACorrectListWhenDaysHaveTimeData(){
        String days = '12:00:00, 08:00:00, 13:20:01'
        List<String> resp = dateUtils.transformStringToListString( days )
        assertThat(resp, hasSize(3))
        assertThat(resp, containsInAnyOrder('08:00:00', '12:00:00', '13:20:01'))
    }

    @Test
    void transformStringToListStringShouldReturnAnEmptyListWhenDaysHaveNotData(){
        String days = ''
        List<String> resp = dateUtils.transformStringToListString( days )
        assertThat(resp, empty())
    }

    @Test
    void transformStringToListStringShouldReturnAnEmptyListWhenDaysHaveSpaces(){
        String days = '   '
        List<String> resp = dateUtils.transformStringToListString( days )
        assertThat(resp, empty())
    }

    @Test
    void getCurrentTimeStrShouldReturnATimeWithCorrectFormat(){        
        String resp = dateUtils.getCurrentTimeStr()
        assertThat(resp.length(), is(8))
        assertThat(resp, matchesPattern("\\d{2}:\\d{2}:\\d{2}"))
    }

    @Test
    void getTodayOfMonthStrShouldReturnADayInEnglish(){
        def days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday']
        String resp = dateUtils.getTodayOfMonthStr()
        assertThat(resp, isIn(days))
    }

    @Test
    void getTimePlus20MinsShouldReturn12Plus20MinsWhenTimeIs12(){
        String time = "12:00:00"
        String resp = dateUtils.getTimePlus20Mins(time)
        assertThat(resp, is("12:20:00"))
    }

    @Test
    void getTimePlus20MinsShouldReturn00Plus20MinsWhenTimeIs00(){
        String time = "00:00:00"
        String resp = dateUtils.getTimePlus20Mins(time)
        assertThat(resp, is("00:20:00"))
    }

    @Test
    void getTimePlus20MinsShouldReturn00Plus19MinsWhenTimeIs235900(){
        String time = "23:59:00"
        String resp = dateUtils.getTimePlus20Mins(time)
        assertThat(resp, is("00:19:00"))
    }

    @Test
    void transformTimesListTotimeRangesShouldReturnARangePlus20MinsForEveryElementInTimes(){
        List<String> times = ['12:00:00', '20:50:03']
        List<Map.Entry<String, String>> expectedTimes = new ArrayList()
        expectedTimes.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "12:20:00"))
        expectedTimes.add(new AbstractMap.SimpleEntry<String, String>("20:50:03", "21:10:03"))

        List<Map.Entry<String, String>> resp = dateUtils.transformTimesListTotimeRanges(times)
        
        assertThat(resp, equalTo(expectedTimes))
    }

    @Test
    void transformTimesListTotimeRangesShouldReturnAnEmptyListIfTimesIsEmpty(){
        List<String> times = []
        List<Map.Entry<String, String>> resp = dateUtils.transformTimesListTotimeRanges(times)        
        assertThat(resp, empty())
    }
    
}