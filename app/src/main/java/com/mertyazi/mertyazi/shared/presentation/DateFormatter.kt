package com.mertyazi.mertyazi.shared.presentation

interface DateFormatter {
    fun format(value : String?) : String
    fun minify(value : String?) : String
}

object DayMonthYearFormatter : DateFormatter {

    override fun format(value: String?): String {
        if (value != null) {
            val dateArray: List<String> = value.split("-")
            return dateArray[2] + "." + dateArray[1] + "." + dateArray[0]
        } else {
            return ""
        }
    }

    override fun minify(value: String?): String {
        if (value != null) {
            val dateArray: List<String> = value.split("-")
            return " (" + dateArray[0] + ")"
        } else {
            return ""
        }

    }

}