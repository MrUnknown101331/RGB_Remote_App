package com.mrunknown101331.rgbremote

import java.math.BigInteger

fun decToHex(n: Int): String {
    var out: String = Integer.toHexString(n)
    if (out.length == 1)
        out = "0$out"
    return out
}

fun hexToDec(h: String): Int {
    var ans = 0
    try {
        ans = BigInteger(h, 16).toInt()
    } catch (_: Exception) {
        for (ch in h) {
            when (ch) {
                in '0'..'9' -> ans += ch.code - 48
                in 'A'..'F' -> ans += ch.code - 65 + 10
                in 'a'..'f' -> ans += ch.code - 97 + 10
            }
        }
    }
    return ans
}

fun hexToFloat(h: String): Float {
    return BigInteger(h, 16).toFloat()
}

fun hexToLong(h: String): Long {
    return BigInteger(h, 16).toLong()
}

fun invertColor(h: String): String {
    if (h.length == 6) {
        return decToHex(255 - hexToDec(h.substring(0, 2))) +
                decToHex(255 - hexToDec(h.substring(2, 4))) +
                decToHex(255 - hexToDec(h.substring(4, 6)))
    }
    return "000000"
}
