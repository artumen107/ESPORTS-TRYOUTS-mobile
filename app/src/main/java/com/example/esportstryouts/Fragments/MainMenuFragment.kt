package com.example.esportstryouts.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.R

class MainMenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       (activity as MainActivity).supportActionBar?.title = "Main menu"
        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.menuTrainingLL).apply {
            setOnClickListener {

                val action =
                    MainMenuFragmentDirections.actionMainMenuFragmentToTrainingMenuFragment()
                view.findNavController().navigate(action)
            }

            view.findViewById<LinearLayout>(R.id.menuResultsLL).apply {
                setOnClickListener {

                    val action =
                        MainMenuFragmentDirections.actionMainMenuFragmentToResultsFragment()
                    view.findNavController().navigate(action)
                }
            }
            view.findViewById<LinearLayout>(R.id.menuHowToPlayLL).apply {
                setOnClickListener {

                    val action =
                        MainMenuFragmentDirections.actionMainMenuFragmentToHowToPlayFragment()
                    view.findNavController().navigate(action)
                }
            }

            view.findViewById<LinearLayout>(R.id.menuLeaderboardLL).apply {
                setOnClickListener {

                    val action =
                        MainMenuFragmentDirections.actionMainMenuFragmentToLeaderboardFragment()
                    view.findNavController().navigate(action)
                }
            }

            view.findViewById<LinearLayout>(R.id.menuTrainingLL).apply {
                setOnClickListener {

                    val action =
                        MainMenuFragmentDirections.actionMainMenuFragmentToTrainingMenuFragment()
                    view.findNavController().navigate(action)
                }
            }

            view.findViewById<LinearLayout>(R.id.menuTrainingLL).apply {
                setOnClickListener {

                    val action =
                        MainMenuFragmentDirections.actionMainMenuFragmentToTrainingMenuFragment()
                    view.findNavController().navigate(action)
                }
            }

        }
    }
}
