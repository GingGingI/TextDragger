package c.gingdev.textdragger.draggerText

import android.util.Log
import android.widget.TextView
import kotlin.math.abs

class dragger {
    var textArray = ArrayList<String>()

    var textView: TextView? = null

    fun setView(textView: TextView) {
        this.textView = textView
    }

    fun addText(text: String) {
        if (text.isEng())
            textArray.add(text.toUpperCase())
    }
    fun addTextAt(position: Int, text: String) {
        if (text.isEng())
            textArray.add(position, text.toUpperCase())
        else
            throw IllegalArgumentException("Only English(A~Z)")
    }

    fun drag (position: Int, value: Float) {
        textView notNull {
            if (textArray.size > 1) {
                text = TextDvider(position, value)
            } else {
                text = "default"
            }
        }
    }

//    Log.e("left", "left: ${leftCharList.toList()}, right: ${rightCharList.toList()}")

    private fun TextDvider(position: Int, value: Float): String {
        if (value < 0.1f) return textArray[position]

        val nextPosition = if (position + 1 > textArray.size) textArray.size else position + 1
        val charSize = if (textArray[position].length > textArray[nextPosition].length)
            textArray[position].length
        else textArray[if (position + 1 > textArray.size) textArray.size else position + 1].length

        val leftCharList = CharArray(charSize)
        val rightCharList = CharArray(charSize)

        val charList = StringBuilder()
        for (i in 0 until charSize) {
            leftCharList[i] = if (textArray[position].length > i) textArray[position].toCharArray()[i] else ' '
            rightCharList[i] = if (textArray[nextPosition].length > i) textArray[nextPosition].toCharArray()[i] else ' '

            charList.append(computedChar(leftCharList[i], rightCharList[i], value))
        }

        return charList.toString()
    }

//    ((65 + 90) / 2)
//     value = (a - b) * c

//    Log.i("ratio_about_$now",
//    "ratio:${ratio.toInt()}, " +
//    "now&to:($now, $to), " +
//    "computed:${if(now.toInt() < to.toInt()) (now.toInt() + ratio.toInt()).toChar() else (to.toInt() - ratio.toInt()).toChar()}, " +
//    "value: $value")

    private fun computedChar(left: Char, right: Char, value: Float): Char {
        val now = if (left == ' ') 'A' else left
        val to = if (right == ' ') 'A' else right

        val ratio = value * (abs(now - to) * 1)

        return if (now.toInt() < to.toInt())
            (now.toInt() + ratio.toInt()).toChar()
        else
            (now.toInt() - ratio.toInt()).toChar()
    }

    private fun String.isEng(): Boolean = !(typePatterns.engPattern).matcher(this).find()

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }
}

