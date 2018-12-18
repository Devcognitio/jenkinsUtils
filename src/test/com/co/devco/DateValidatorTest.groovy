package com.co.devco

import javafx.util.Pair
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import java.time.format.DateTimeParseException

import static org.hamcrest.MatcherAssert.assertThat

class DateValidatorTest {

    DateValidator validator = new DateValidator();
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
}
