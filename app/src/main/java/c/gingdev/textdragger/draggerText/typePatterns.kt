package c.gingdev.textdragger.draggerText

import java.util.regex.Pattern

object typePatterns {
    val engPattern = Pattern.compile("(^[a-zA-Z]*$)")
    val korPattern = Pattern.compile("(^[ㄱ-ㅎ가-힣]*$)")
}