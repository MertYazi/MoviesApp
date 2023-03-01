package com.mertyazi.mertyazi.shared.presentation

import java.math.BigDecimal
import java.math.RoundingMode

interface ImdbScoreFormatter {
    fun format(value : Double?) : String
}

object TwoDigitsFormatter : ImdbScoreFormatter {

    override fun format(value: Double?): String {
        if (value != null) {
            return BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
                .toString()
        } else {
            return ""
        }
    }

}