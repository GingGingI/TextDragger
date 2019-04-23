package c.gingdev.textdragger.draggerText

import android.util.Log
import android.widget.TextView
import kotlin.math.abs


class dragger {
    var textArray = ArrayList<String>()
    var textView: TextView? = null

    private val eng = 0
    private val kor = 1

    private val doubleChoWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(1, 4, 8, 10, 13, 15, 17)
    }
    private val doubleJungWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(2 ,3, 5, 6, 7, 12, 15, 16, 17)
    }
    private val doubleJongWords by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(2, 3, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 20)
    }

    private var type: Int = eng

    fun setView(textView: TextView) {
        this.textView = textView
    }

    fun addText(text: String) {
        if (text.isEng()) {
            textArray.add(text.toUpperCase())
        } else if (text.isKor()) {
            textArray.add(text)
            type = kor
        }

    }

    fun addTextAt(position: Int, text: String) {
        if (text.isEng())
            textArray.add(position, text.toUpperCase())
        else
            throw IllegalArgumentException("Only English(A~Z)")
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

    //   English
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

        return if (now.toInt() < to.toInt()) (now.toInt() + ratio.toInt()).toChar()
        else (now.toInt() - ratio.toInt()).toChar()
    }

    //    Kor
    private fun korTextDivider(position: Int, value: Float): String {
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

            val jong = leftCharList[i].pureKor() % 28
            val jung = ((leftCharList[i].pureKor() - jong) / 28) % 21
            val cho = (((leftCharList[i].pureKor() - jong) / 28) - jung) / 21

            val tjong = rightCharList[i].pureKor() % 28
            val tjung = ((rightCharList[i].pureKor() - tjong) / 28) % 21
            val tcho = (((rightCharList[i].pureKor() - tjong) / 28) - tjung) / 21

            val computedJong = korComputedChar(jong, tjong, value).nowDoubleJong()
            val computedJung = korComputedChar(jung, tjung, value).nowDoubleJung()
            val computedCho = korComputedChar(cho, tcho, value).nowDoubleCho()

//            Log.i("CJJ", "cho : $cho, jung : $jung, jong : $jong")
//            Log.i("CJJ To", "cho : $tcho, jung : $tjung, jong : $tjong")
//
//            Log.i("CJJ Add", String.format("%%u%02X",0xAC00 + ((cho * 21) + jung ) * 28 + jong))
            charList.append((0xAC00 + ((computedCho * 21) + computedJung) * 28 + computedJong).toChar())
        }
//        Log.i("text", "left: ${leftCharList.toList()}, right: ${rightCharList.toList()}")
        return charList.toString()
    }

    private fun korComputedChar(left: Int, right: Int, value: Float): Int {
        val now = if (left < 0) 0 else left
        val to = if (right < 0) 0 else right
//        val ratio = (value) * (abs(now - to) * 1)
        val ratio = (value) * (abs(now - to) * 1)

        return if (now < to) (now + ratio).toInt()
        else (now - ratio).toInt()
    }

//    y=1–(1–t)2f

    private fun Int.nowDoubleCho(): Int = if (this in doubleChoWords) this - 1 else this

    private fun Int.nowDoubleJung(): Int = if (this in doubleJungWords) {
        var i = 0
        while (this - i in doubleJungWords) {
            i++; if (this - i !in doubleJungWords) {
                break
            }
        }
        this - i
    } else this

    private fun Int.nowDoubleJong(): Int = if (this in doubleJongWords) {
        var i = 0
        while (this - i in doubleJongWords) {
            i++; if (this - i !in doubleJongWords) {
                break
            }
        }
        this - i
    } else this

    private fun Char.pureKor(): Int = this.hashCode() - 0xAC00

    private fun String.isEng(): Boolean = (typePatterns.engPattern).matcher(this).find()
    private fun String.isKor(): Boolean = (typePatterns.korPattern).matcher(this).find()

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }
}

