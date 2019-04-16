package c.gingdev.textdragger.draggerText

import android.util.Log
import android.widget.TextView
import kotlin.math.abs

class dragger {

    var toText: String? = null
    var nowText: String? = null

    var textView: TextView? = null

    var isEnd: Boolean = false

    fun setView(textView: TextView) {
        this.textView = textView
    }

    fun from (nowText: String) {
        this.nowText = nowText.toUpperCase()
    }

    fun to (toText: String) {
        this.toText = toText.toUpperCase()
    }

    fun drag (value: Float) {
        textView notNull {
            if (nowText.isNull() or toText.isNull()) {
                Log.i("dragger", "Some Value is Null... Check it again.")
            } else {
                if (!isEnd)
                    text = TextDvider(value)
            }
        }
    }

    fun startDrag() {
        isEnd = false
    }

    fun endDrag() {
        isEnd = true
    }

    private fun TextDvider(value: Float): String {
        val nowArray = nowText!!.toCharArray()
        val toArray = toText!!.toCharArray()

        if (nowText!!.isEng() and toText!!.isEng()) {
            val size = if (nowArray.size > toArray.size) nowArray.size else toArray.size
            val charList = StringBuilder()
            for (i in 0 until size) {
                Log.i("i is ->", "$i")
                val char = computedChar(nowArray[i], toArray[i], value)
                charList.append(char)
            }
            return charList.toString()
        } else {
            return "Default"
        }
    }

//    ((65 + 90) / 2)
//     value = (a - b) * c
    private fun computedChar(now: Char, to: Char, value: Float): Char {
        val ratio = abs(now.toInt() - to.toInt()) * value

        Log.i("ratio", "${ratio.toInt()}, (${now.toInt()}, ${to.toInt()}), ${(now.toInt() + ratio.toInt()).toChar()}")

        return (now.toInt() + ratio.toInt()).toChar()
    }

    private fun String.isEng(): Boolean = (typePatterns.engPattern).matcher(this).find()

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }

    private fun <T> T?.isNull(): Boolean {
        return this == null
    }
}

