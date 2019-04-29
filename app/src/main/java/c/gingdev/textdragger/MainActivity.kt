package c.gingdev.textdragger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import c.gingdev.textdragger.fragment.SampleFragment
import kotlinx.android.synthetic.main.activity_main.*

const val first = 0
const val second = 1
const val third = 2

class MainActivity : AppCompatActivity() {

    var firstText: String = "안녕하세요"
    var secondText: String = "하위"
    var thirdText: String = "잘있어"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = pagerAdapter(supportFragmentManager, arrayOf(first, second, third))

        draggableText.attach(pager)
        draggableText.apply {
            addText(firstText)
            addText(secondText)
            addText(thirdText)
        }
    }

    private inner class pagerAdapter internal constructor(fm: FragmentManager, private val items: Array<Int>) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment =
            getFragments(position)

        fun getFragments(position: Int): Fragment =
            when (items[position]) {
                first -> SampleFragment()
                second -> SampleFragment()
                third -> SampleFragment()
                else -> SampleFragment()
            }

        override fun getCount(): Int =
            items.size
    }
}