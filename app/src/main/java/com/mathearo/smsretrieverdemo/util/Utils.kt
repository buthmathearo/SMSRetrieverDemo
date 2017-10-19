package com.mathearo.smsretrieverdemo.util

import java.util.regex.Pattern

/**
 * Created by buthmathearo on 10/20/17.
 */
class Utils private constructor(){

    companion object {
        fun extractDigit(text: String):String {
            val pattern = Pattern.compile("(\\d{8})")
            val matcher = pattern.matcher(text)

            if (matcher.find()) {
                return matcher.group(0)
            }

            return ""
        }
    }

}