package c.gingdev.draggabletext.dragger

import java.util.regex.Pattern

object typePatterns {
    val engPattern = Pattern.compile("(^[a-zA-Z]*$)")
    val korPattern = Pattern.compile("(^[ㄱ-ㅎ가-힣]*$)")
}