import java.time.LocalTime

def call (String initialTime, String finalTime, String currentTime){
    LocalTime start = LocalTime.parse( initialTime )
    LocalTime stop = LocalTime.parse( finalTime )
    LocalTime target = LocalTime.parse( currentTime )

    boolean isTargetAfterStartAndBeforeStop = ( target.isAfter( start ) && target.isBefore( stop ) ) || (target == start) || (target == stop)

    return isTargetAfterStartAndBeforeStop
}