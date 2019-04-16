package c.gingdev.textdragger.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import c.gingdev.textdragger.R

class SampleFragment : Fragment() {

    private var layout: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        layout ?: inflater.inflate(R.layout.fragment_sample, container, false)
            .also { layout = it }

}