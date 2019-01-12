package dev.seijou.com.seijoutodo.Views.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.seijou.com.seijoutodo.R

/**
 * Created by frontend on 12/12/17.
 */
class TodoFragment: Fragment() {
    companion object {
        fun newInstance(): TodoFragment {
            return TodoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_todo, container, false)

        return view
    }
}