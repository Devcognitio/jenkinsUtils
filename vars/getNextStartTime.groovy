import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit

def call(List<Map.Entry<String, String>> timeRangesList, String currentTime){
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