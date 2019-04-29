package c.gingdev.draggabletext

import android.content.Context
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import c.gingdev.draggabletext.dragger.dragger

class draggableTextView(context: Context?) : TextView(context)
    , ViewPager.OnPageChangeListener {
    private var pager: ViewPager? = null
    private val dragger by lazy { dragger() }

    override fun onPageScrollStateChanged(state: Int) {

    }
    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pager notNull { dragger.drag(position, positionOffset) }
        isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun attach(viewPager: ViewPager) {
        pager isNull { pager = viewPager }
        viewPager.addOnPageChangeListener(this)
    }

    fun setTextAt(position: Int, text: String) {
        pager notNull {
            if (dragger.addTextAt(position, text) > this.childCount)
                throw (ArrayIndexOutOfBoundsException(
                    "Pager count exceeded " +
                            "[PagerSize: ${this.childCount}, array: $position]"))
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun setTextAsArray(array: Array<String>) {
        pager notNull {
            array.forEachIndexed { position, str ->
                if (dragger.addText(str) > this.childCount)
                    throw (ArrayIndexOutOfBoundsException(
                        "Pager count exceeded " +
                        "[PagerSize: ${this.childCount}, array: $position]"))
            }
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun setTextAsArrayList(array: ArrayList<String>) {
        array.forEach {
            dragger.addText(it)
        }
    }

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }
    private infix fun <T> T?.isNull(f: T.()-> Unit) {
        if (this == null) f()
    }
}
