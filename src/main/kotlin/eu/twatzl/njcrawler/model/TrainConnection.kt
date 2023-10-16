package eu.twatzl.njcrawler.model

data class TrainConnection(
    val trainId: String,
    val fromStation: Station = Station(),
    val toStation: Station = Station(),
)