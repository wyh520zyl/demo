package wuge.social.com.model

data class SolarTerms(
    val message: String,
    val result: List<SolarTermsResult>,
    val status: Int

) {
    override fun toString(): String {
        return "SolarTerms(message='$message', result=$result, status=$status)"
    }
}

data class SolarTermsResult(
    val current_status: String,
    val current_time: String,
    val end_time: String,
    val start_time: String,
    val term_name: String,
    val time: List< SolarTermsTime>,
    var total_days: Int

) {
    override fun toString(): String {
        return "SolarTermsResult(current_status='$current_status', current_time='$current_time', end_time='$end_time', start_time='$start_time', term_name='$term_name', time=$time, total_days=$total_days)"
    }
}

data class SolarTermsTime(
    val day: String,
    var status: Int

) {
    override fun toString(): String {
        return "SolarTermsTime(day='$day', status=$status)"
    }
}