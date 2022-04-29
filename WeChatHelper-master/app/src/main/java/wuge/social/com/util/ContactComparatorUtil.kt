package wuge.social.com.util

import java.util.*

class ContactComparatorUtil : Comparator<String>{
    override fun compare(o1: String?, o2: String?): Int {
        println("String o1：$o1")
        println("String o2：$o2")
        if (o1 == null && o2 == null) {
            return 0
        }
        if (o1 == null) {
            return -1
        }
        if (o2 == null) {
            return 1
        }
        val c1 = (o1[0].toString() + "").toUpperCase().hashCode()
        val c2 = (o2[0].toString() + "").toUpperCase().hashCode()
        val c1Flag = c1 < "A".hashCode() || c1 > "Z".hashCode() // 不是字母
        val c2Flag = c2 < "A".hashCode() || c2 > "Z".hashCode() // 不是字母
        if (c1Flag && !c2Flag) {
            return 1
        } else if (!c1Flag && c2Flag) {
            return -1
        }
        return c1 - c2
    }
}