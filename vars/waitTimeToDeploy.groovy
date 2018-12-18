import java.text.DateFormatSymbols
import com.co.devco.DateUtils

def call(String deployDay, List<Map.Entry<String, String>> timeRangesList,String resourceName){
    milestone()
    DateUtils DATE_UTILS = new DateUtils()
    lock(resource: resourceName, inversePrecedence: true) {
        Date date = new Date()
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)

        int hour = cal.get(Calendar.HOUR_OF_DAY)
        int minute = cal.get(Calendar.MINUTE)
        int second = cal.get(Calendar.SECOND)

        String hourString = (hour<10?("0"+hour):(hour))
        String minuteString = (minute<10?("0"+minute):(minute))
        String secondString = (second<10?("0"+second):(second))

        String currentTime = "$hourString:$minuteString:$secondString"

        int day = cal.get(Calendar.DAY_OF_WEEK)
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.ENGLISH)
        String dayOfMonthStr = symbols.getWeekdays()[day]

        if (deployDay.toUpperCase() == "ALL"){
            dayOfMonthStr = deployDay
        }

        echo "DEPLOY day: $deployDay"
        echo "Actual time: $currentTime"
        echo "Actual day: $dayOfMonthStr"

        while (dayOfMonthStr != deployDay || !(isTimeBetweenTimeRangesList(timeRangesList, currentTime))) {
            String nextStartTime = DATE_UTILS.getNextStartTime(timeRangesList, currentTime)
            Long secondsToSleep = DATE_UTILS.secondsToNextInputDay(deployDay, nextStartTime)
            sleep secondsToSleep
            date = new Date()
            cal.setTime(date)

            hour = cal.get(Calendar.HOUR_OF_DAY)
            minute = cal.get(Calendar.MINUTE)
            second = cal.get(Calendar.SECOND)

            hourString = (hour<10?("0"+hour):(hour))
            minuteString = (minute<10?("0"+minute):(minute))
            secondString = (second<10?("0"+second):(second))

            currentTime = "$hourString:$minuteString:$secondString"

            day = cal.get(Calendar.DAY_OF_WEEK)
            dayOfMonthStr = symbols.getWeekdays()[day]

            if (deployDay == "ALL"){
                dayOfMonthStr = deployDay
            }

            echo "DEPLOY day: $deployDay"
            echo "Actual time: $currentTime"
            echo "Actual day: $dayOfMonthStr"
            echo "Next Start Time: $nextStartTime"
            echo "Seconds to sleep: $secondsToSleep"
            echo "Waiting for deployment time: Range on day: $deployDay"
        }
        milestone()
    }
}