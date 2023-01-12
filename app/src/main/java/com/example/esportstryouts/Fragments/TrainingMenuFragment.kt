package com.example.esportstryouts.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.esportstryouts.*

class TrainingMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.title = "Training Menu"
        return inflater.inflate(R.layout.training_menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.trainingMenuReactionLL).apply {
            setOnClickListener {

//                val action =
//                    TrainingMenuFragmentDirections.actionTrainingMenuFragmentToReactionTimeFragment2()
                val intent = Intent(activity, ReactionTimeActivity::class.java)
                startActivity(intent)

                //view.findNavController().navigate(action)

            }

            view.findViewById<LinearLayout>(R.id.trainingMenuSwitchLL).apply {
                setOnClickListener {

                    val intent = Intent(activity, SwitchCostActivity::class.java)
                    startActivity(intent)
                }
            }
            view.findViewById<LinearLayout>(R.id.trainingMenuDelayLL).apply {
                setOnClickListener {

                    val intent = Intent(activity, DelayAbilityActivity::class.java)
                    startActivity(intent)
                }
            }
            view.findViewById<ImageButton>(R.id.trainingMenuBackBtn).apply {
                setOnClickListener {

                    val action =
                        TrainingMenuFragmentDirections.actionTrainingMenuFragmentToMainMenuFragment()
                    view.findNavController().navigate(action)
                }
            }



        }
    }

}