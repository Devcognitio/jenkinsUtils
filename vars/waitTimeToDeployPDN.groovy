def call(props){
    List<Map.Entry<String, String>> timeRanges = new ArrayList()
    for (i = 0; i <props.deployCountPdn.toInteger(); i++) {
        def item=i
        def startHour= props."deployStartHourPdn${item}"
        def endHour= props."deployEndHourPdn${item}"
        echo "Hora start cargada de las propiedades $item : $startHour"
        echo "Hora end cargada de las propiedades $item : $endHour"
        timeRanges.add(new AbstractMap.SimpleEntry<String, String>(startHour, endHour))
    }
	def resourceName="lockPDN-${props.appName}"
    waitTimeToDeploy(props.deployDayUat, timeRanges, resourceName)
}