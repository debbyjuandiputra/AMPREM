package com.amprem.securevideo

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Implementasi TOTP standar (RFC 6238) - kompatibel dengan aplikasi authenticator
 * seperti Google Authenticator / Authy.
 *
 * Kunci rahasia yang dipakai di aplikasi authenticator (untuk digenerate oleh admin)
 * adalah string: AMPREM
 * (String ini valid sebagai secret Base32, jadi tinggal dimasukkan langsung
 * ke aplikasi authenticator manapun sebagai "kunci setup manual").
 *
 * Kunci ini TIDAK ditampilkan di aplikasi ini - hanya dipakai untuk verifikasi.
 */
object TotpUtil {

    private const val SECRET_BASE32 = "ALIGHT MOTION PREMIUM BY YOR PEDIA"
    private const val TIME_STEP_SECONDS = 30L
    private const val CODE_DIGITS = 6

    /** Verifikasi kode 6 digit yang dimasukkan user, dengan toleransi ±1 langkah waktu (drift). */
    fun verifyCode(inputCode: String): Boolean {
        if (inputCode.length != CODE_DIGITS || inputCode.any { !it.isDigit() }) return false
        val currentStep = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS
        for (drift in -1..1) {
            if (generateCode(currentStep + drift) == inputCode) return true
        }
        return false
    }

    private fun generateCode(timeStep: Long): String {
        val key = base32Decode(SECRET_BASE32)
        val data = ByteArray(8)
        var value = timeStep
        for (i in 7 downTo 0) {
            data[i] = (value and 0xFF).toByte()
            value = value shr 8
        }

        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key, "HmacSHA1"))
        val hash = mac.doFinal(data)

        val offset = hash[hash.size - 1].toInt() and 0x0F
        val binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)

        val otp = binary % Math.pow(10.0, CODE_DIGITS.toDouble()).toInt()
        return otp.toString().padStart(CODE_DIGITS, '0')
    }

    private fun base32Decode(input: String): ByteArray {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val cleaned = input.uppercase().replace("=", "")
        val bits = StringBuilder()
        for (c in cleaned) {
            val idx = alphabet.indexOf(c)
            if (idx < 0) continue
            bits.append(idx.toString(2).padStart(5, '0'))
        }
        val bytes = ArrayList<Byte>()
        var i = 0
        while (i + 8 <= bits.length) {
            bytes.add(bits.substring(i, i + 8).toInt(2).toByte())
            i += 8
        }
        return bytes.toByteArray()
    }
}
