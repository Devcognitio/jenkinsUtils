package com.co.devco

import javafx.util.Pair
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.time.format.DateTimeParseException
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*
import static org.hamcrest.text.MatchesPattern.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

class DateValidatorTest {

    DateValidator validator = new DateValidator()

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    void isTimeBetweenTwoTimeShouldBeTrueWhenTimeIsInRange(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "01:15:00"

        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)

        assertThat("Time is not between range", response)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeTrueWhenTimeIsInRangeLimitDown(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "02:00:00"

        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)

        assertThat("Time is not between range", response)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeTrueWhenTimeIsInRangeLimitUp(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "01:00:00"

        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)

        assertThat("Time is not between range", response)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeTrueWhenTimeIsOutRange(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "22:00:00"

        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)

        assertThat("Time is between range", !response)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeErrorIfTimeIsBadFormedWhitLetter(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "22:A0:00"

        thrown.expect(DateTimeParseException.class);
        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)
    }

    @Test
    void isTimeBetweenTwoTimeShouldOKIfTimeIsWithOutSeconds(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "22:00"

        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)

        assertThat("Time is between range", !response)
    }

    @Test
    void isTimeBetweenTwoTimeShouldErrorIfTimeIsWithOutSecondsAndMinutes(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "22"

        thrown.expect(DateTimeParseException.class);
        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeErrorIfTimeIsBiggerThan24h(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "26:00:00"

        thrown.expect(DateTimeParseException.class);
        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)
    }

    @Test
    void isTimeBetweenTwoTimeShouldBeErrorIfTimeIsNegative(){
        String start = "01:00:00"
        String stop = "02:00:00"
        String current = "-01:00:00"

        thrown.expect(DateTimeParseException.class);
        boolean response = validator.isTimeBetweenTwoTime(start, stop, current)
    }

    @Test
    void isTimeBetweenTimeRangesListShouldReturnFalseIfTimeIsNotInAnyRange(){
        List<Map.Entry<String, String>> timeRanges = new ArrayList()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("02:00:00", "03:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("04:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))
        String current = "01:00:00"

        boolean response = validator.isTimeBetweenTimeRangesList(timeRanges, current)

        assertThat("Time is between ranges", !response)
    }

    @Test
    void isTimeBetweenTimeRangesListShouldReturnTrueIfTimeIsInOneRange(){
        List<Map.Entry<String, String>> timeRanges = new ArrayList()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "13:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("00:00:00", "05:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "00:00:00"))
        String current = "01:00:00"

        boolean response = validator.isTimeBetweenTimeRangesList(timeRanges, current)

        assertThat("Time is not between ranges", response)
    }

    @Test
    void isTimeBetweenTimeRangesListShouldReturnTrueIfTimeIsInRangesAreEquals(){
        List<Map.Entry<String, String>> timeRanges = new ArrayList()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "13:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "13:00:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "13:00:00"))
        String current = "12:30:00"

        boolean response = validator.isTimeBetweenTimeRangesList(timeRanges, current)

        assertThat("Time is not between ranges", response)
    }

    @Test
    void compareDaysShouldReturnTrueWhenTodayAndExecutionDayAreEquals(){
        String today = 'monday'
        String executionDay = 'monday'
        boolean resp = validator.compareDays(today, executionDay)
        assertThat(resp, is(true))
    }

    @Test
    void compareDaysShouldReturnFalseWhenTodayAndExecutionDayAreNotEquals(){
        String today = 'monday'
        String executionDay = 'tuesday'
        boolean resp = validator.compareDays(today, executionDay)
        assertThat(resp, is(false))
    }

    @Test
    void compareDaysShouldReturnTrueWhenExecutionIsALL(){
        String today = 'monday'
        String executionDay = 'ALL'
        boolean resp = validator.compareDays(today, executionDay)
        assertThat(resp, is(true))
    }

    @Test
    void compareDaysShouldReturnTrueWhenTodayAndExecutionDayAreEqualsAndNotMatterCase(){
        String today = 'monday'
        String executionDay = 'MoNdaY'
        boolean resp = validator.compareDays(today, executionDay)
        assertThat(resp, is(true))
    }

    @Test
    void compareDaysShouldReturnTrueWhenTodayAndExecutionDayAreEqualsAndNotMatterCaseBoth(){
        String today = 'mONDAY'
        String executionDay = 'MoNdaY'
        boolean resp = validator.compareDays(today, executionDay)
        assertThat(resp, is(true))
    }

    @Test
    void compareDayAndDaysShouldReturnTrueWhenTodayIsInListOfDays(){
        String today = 'mONDAY'
        def days = ['MoNdaY', 'Friday']
        boolean resp = validator.compareDayAndDays(today, days)
        assertThat(resp, is(true))
    }

    @Test
    void compareDayAndDaysShouldReturnFalseWhenTodayIsNotInListOfDays(){
        String today = 'SATURDAY'
        def days = ['MoNdaY', 'Friday']
        boolean resp = validator.compareDayAndDays(today, days)
        assertThat(resp, is(false))
    }

    @Test
    void compareDayAndDaysShouldReturnTrueWhenTodayIsNotInListOfDaysButAllIsInList(){
        String today = 'SATURDAY'
        def days = ['MoNdaY', 'Friday', 'aLl']
        boolean resp = validator.compareDayAndDays(today, days)
        assertThat(resp, is(true))
    }

    @Test
    void compareDayAndDaysShouldReturnFalseWhenListOfDaysIsEmpty(){
        String today = 'SATURDAY'
        def days = []
        boolean resp = validator.compareDayAndDays(today, days)
        assertThat(resp, is(false))
    }

    @Test
    void isDayAndTimeToExecuteShouldReturnTrueWhenTodayAndCurrentTimeAreInRanges(){
        String executionDays = 'SATURDAY, MoNdaY, Friday'
        String executionTimes = '12:00:00, 11:00:00, 23:00:00'
        List<Map.Entry<String, String>> timeRanges = new ArrayList()
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("12:00:00", "12:20:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("11:00:00", "11:20:00"))
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>("23:00:00", "23:20:00"))

        def mockDateUtils = new MockFor( DateUtils )
        mockDateUtils.demand.with {
            //DateUtils() { mockDateUtils.proxyInstance() }
            getCurrentTimeStr{ return '10:00:00' }
            getTodayOfMonthStr{ return 'monday' }
            transformStringToListString{str -> 
                if (str==executionDays){
                    return ['saturday','monday', 'friday']
                }else{
                    return ['12:00:00', '11:00:00', '23:00:00']
                }
            }
            transformTimesListTotimeRanges{str -> return timeRanges}
        }

        mockDateUtils.use {
            boolean resp = validator.isDayAndTimeToExecute(executionDays, executionTimes)
            assertThat(resp, is(true))
        }
        //mockDateUtils.expect.verify() 
    }
}
