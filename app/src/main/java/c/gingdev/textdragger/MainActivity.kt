package c.gingdev.textdragger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import c.gingdev.textdragger.draggerText.dragger
import c.gingdev.textdragger.fragment.SampleFragment
import kotlinx.android.synthetic.main.activity_main.*

const val first = 0
const val second = 1

class MainActivity : AppCompatActivity(),
    ViewPager.OnPageChangeListener{

    override fun onPageScrollStateChanged(state: Int) {

    }

//    핵심
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        dragger.startDrag()
        dragger.drag(positionOffset)
    }

    override fun onPageSelected(position: Int) {
        dragger.endDrag()
        if (position == first) {
            draggableText.text = fromText
        } else {
            draggableText.text = toText
        }
    }

    var fromText: String = "asdf"
    var toText: String = "zxcv"

    var dragger: dragger = dragger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        draggableText.text = fromText

        pager.adapter = pagerAdapter(supportFragmentManager, arrayOf(first, second))
        pager.addOnPageChangeListener(this)

        dragger.setView(draggableText)
        dragger.from(fromText)
        dragger.to(toText)
    }

    private inner class pagerAdapter internal constructor(fm: FragmentManager, private val items: Array<Int>) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return getFragments(position)
        }

        fun getFragments(position: Int): Fragment {
            when (items[position]) {
                first -> return SampleFragment()
                second -> return SampleFragment()
                else -> return SampleFragment()
            }
        }

        override fun getCount(): Int {
            return items.size
        }
    }
}