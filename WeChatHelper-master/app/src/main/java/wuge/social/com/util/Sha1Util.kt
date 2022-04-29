package wuge.social.com.util

import okhttp3.internal.and
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object Sha1Util {
    val securityAppKey: String
        get() = ""

    //使用方法按照getSecurityAppKey方法使用，将要加密的字串写到encryptToSHA中即可！
    fun encryptToSHA(info: String): String {
        var digesta: ByteArray? = null
        try {
            val alga =
                MessageDigest.getInstance("SHA-1")
            alga.update(info.toByteArray())
            digesta = alga.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return byte2hex(digesta)
    }

    fun byte2hex(b: ByteArray?): String {
        var hs = ""
        var stmp = ""
        for (n in b!!.indices) {
            stmp = Integer.toHexString(b[n] and 0XFF)
            hs = if (stmp.length == 1) {
                hs + "0" + stmp
            } else {
                hs + stmp
            }
        }
        return hs
    }

    fun shaEncrypt(strSrc: String): String? {
        var md: MessageDigest? = null
        var strDes: String? = null
        val bt = strSrc.toByteArray()
        try {
            md = MessageDigest.getInstance("SHA-1") // 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt)
            strDes = bytes2Hex(md.digest()) // to HexString
        } catch (e: NoSuchAlgorithmException) {
            return null
        }
        return strDes
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts
     * 数据源
     * @return 16进制字符串
     */
    fun bytes2Hex(bts: ByteArray): String? {
        var des: String? = ""
        var tmp: String? = null
        for (i in bts.indices) {
            tmp = Integer.toHexString(bts[i] and 0xFF)
            if (tmp.length == 1) {
                des += "0"
            }
            des += tmp
        }
        return des
    }
}
