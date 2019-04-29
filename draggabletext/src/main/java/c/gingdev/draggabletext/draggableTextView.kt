package c.gingdev.draggabletext

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import c.gingdev.draggabletext.dragger.dragger

class draggableTextView: TextView
    , ViewPager.OnPageChangeListener {
    private var pager: ViewPager? = null
    private val dragger = dragger()

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleSet: Int) : super(context, attributeSet, defStyleSet)

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        dragger.drag(position, positionOffset)
    }

    fun attach(viewPager: ViewPager) {
        viewPager notNull {
            pager isNull {
                viewPager.adapter isNull { throw(NullPointerException("adapter is null")) }
                pager = viewPager
                pager!!.addOnPageChangeListener(this@draggableTextView)
            }
        }
        dragger.setView(this)
    }

    fun addText(text: String) {
        pager notNull {
            dragger.addText(text)
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun setTextAt(position: Int, text: String) {
        pager notNull {
            dragger.addTextAt(position, text)
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun setTextAsArray(array: Array<String>) {
        pager notNull {
            array.forEach {
                dragger.addText(it)
            }
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    fun setTextAsArrayList(array: ArrayList<String>) {
        pager notNull {
            array.forEach {
                dragger.addText(it)
            }
        } isNull { throw (NullPointerException("viewPager not Exist")) }
    }

    private infix fun <T> T?.notNull(f: T.(T)-> Unit) {
        if (this != null) f(this)
    }
    private infix fun <T> T?.isNull(f: T?.()-> Unit) {
        if (this == null) f()
    }
}
