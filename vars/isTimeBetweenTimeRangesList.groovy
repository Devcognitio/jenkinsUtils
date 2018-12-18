def call (List<Map.Entry<String, String>> timeRangesList, String currentTime){
    timeRangesList.any { Map.Entry<String, String> pair ->
        boolean result = isTimeBetweenTwoTime(pair.getKey(), pair.getValue(), currentTime)
        if (result){
            return true
        }
    }
}
