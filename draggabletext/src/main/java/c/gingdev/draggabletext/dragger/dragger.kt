package c.gingdev.draggabletext.dragger

import android.widget.TextView
import kotlin.math.abs

class dragger {
    var textArray = ArrayList<String>()
    var textView: TextView? = null

    private val eng = 0
    private val kor = 1

    private var type: Int = eng

    fun setView(textView: TextView) {
        this.textView = textView
    }

    fun addText(text: String) {
        when {
            text.isEng() -> textArray.add(text.toUpperCase())
            text.isKor() -> textArray.add(text).also { type = kor }
            else -> throw IllegalArgumentException("Only English(A~Z) Or Korean(가~힣)")
        }
    }

    fun addTextAt(position: Int, text: String) {
        when {
            text.isEng() -> textArray.add(position, text.toUpperCase())
            text.isKor() -> textArray.add(position, text).also { type = kor }
            else -> throw IllegalArgumentException("Only English(A~Z) Or Korean(가~힣)")
        }
    }

    fun drag(position: Int, value: Float) {
        textView notNull {
            if (textArray.size > 1) {
                text = if (type == eng)
                    TextDvider(position, value)
                else
                    korTextDivider(position, value)
            } else {
                text = "default"
            }
        }
    }

    private fun String.isEng(): Boolean = (typePatterns.engPattern).matcher(this).find()
    private fun String.isKor(): Boolean = (typePatterns.korPattern).matcher(this).find()

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }

//    private fun <T: Comparable<T>> T.Max(maxValue: T): T =
//        if (this < maxValue) this
//        else maxValue
//    private fun <T: Comparable<T>> T.Min(minValue: T): T =
//        if (this > minValue) this
//        else minValue


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
//   English
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun TextDvider(position: Int, value: Float): String {
        if (value < 0.1f) return textArray[position]
        if (value > 0.9f) return textArray[position + 1]

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

    private fun computedChar(left: Char, right: Char, value: Float): Char {
        val now = if (left == ' ') 'A' else left
        val to = if (right == ' ') 'A' else right
        val ratio = (value * (abs(now - to)))

        return if (now.toInt() < to.toInt()) (now.toInt() + ratio.toInt()).toChar()
        else (now.toInt() - ratio.toInt()).toChar()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    Kor
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private val cho = 0
    private val jung = 1
    private val jong = 2

    private val doubleChoWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(1, 4, 8, 10, 13, 15, 17)
    }
    private val doubleJungWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(2 ,3, 5, 6, 7, 12, 15, 16, 17)
    }
    private val doubleJongWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(2, 3, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 20)
    }

    private fun korTextDivider(position: Int, value: Float): String {
        if (value < 0.1f) return textArray[position]
        if (value > 0.9f) return textArray[position + 1]

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

            val computedJong =
                korComputedChar(
                    leftCharList[i] pureKorNumber(jong),
                    rightCharList[i] pureKorNumber(jong),
                    value).nowDouble(doubleJongWords)

            val computedJung =
                korComputedChar(
                    leftCharList[i] pureKorNumber(jung),
                    rightCharList[i] pureKorNumber(jung),
                    value).nowDouble(doubleJungWords)

            val computedCho =
                korComputedChar(
                    leftCharList[i] pureKorNumber(cho),
                    rightCharList[i] pureKorNumber(cho),
                    value).nowDouble(doubleChoWords)

            val korChar = (0xAC00 + ((computedCho * 21) + computedJung) * 28 + computedJong).toChar()


            if ((typePatterns.korPattern).matcher(korChar.toString()).find())
                charList.append(korChar)
        }
        return charList.toString()
    }

    private fun korComputedChar(left: Int, right: Int, value: Float): Int {
        val now = if (left < 0) 0 else left
        val to = if (right < 0) 0 else right
        val ratio = ((value * (abs(now - to))))

        return if (now < to) (now + ratio).toInt()
        else (now - ratio).toInt()
    }

    private fun Char.pureKor(): Int = this.hashCode() - 0xAC00
    private infix fun Char.pureKorNumber(switcher: Int): Int =
        when(switcher) {
            jong -> this.pureKor() % 28
            jung -> ((this.pureKor() - this.pureKorNumber(jong) % 28) / 28) % 21
            cho -> (((this.pureKor() - this.pureKorNumber(jong)) / 28) - this.pureKorNumber(jung)) / 21
            else -> 0
        }

    private fun Int.nowDouble(array: Array<Int>): Int = if (this in array) {
        var i = 0
        while (this - i in array) {
            i++; if (this - i !in array) {
                break
            }
        }
        this - i
    } else this

}
