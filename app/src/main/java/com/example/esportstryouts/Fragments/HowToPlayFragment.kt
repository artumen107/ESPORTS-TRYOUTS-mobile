package com.example.esportstryouts.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.R

class HowToPlayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.title = "How To Play"
        return inflater.inflate(R.layout.howtoplay_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.howToPlayBackBtn).apply {
            setOnClickListener {

                val action =
                    HowToPlayFragmentDirections.actionHowToPlayFragmentToMainMenuFragment()
                view.findNavController().navigate(action)
            }

        }
    }
}