package com.example.esportstryouts.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.Model.DataBaseRepository
import com.example.esportstryouts.R
import com.example.esportstryouts.ViewModels.LeaderboardAdapter
import com.example.esportstryouts.ViewModels.LeaderboardViewModel

class LeaderboardFragment : Fragment() {

    val repository = DataBaseRepository()
    val viewModel = LeaderboardViewModel()
    lateinit var LeaderboardAdapter : LeaderboardAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.title = "Leaderboard"
        return inflater.inflate(R.layout.leaderboard_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(view.context)
        LeaderboardAdapter = LeaderboardAdapter(view, viewModel, viewModel.allFriends, this.context)


        viewModel.allFriends.observe(viewLifecycleOwner) {
            LeaderboardAdapter.notifyDataSetChanged()

        }
        view.findViewById<RecyclerView>(R.id.leaderboardList).let {
            it.layoutManager = layoutManager
            it.adapter = LeaderboardAdapter

                viewModel.fetchDataBaseFriends()
        }
            view.findViewById<ImageButton>(R.id.leaderboardBackBtn).apply {
                setOnClickListener {

                    val action =
                        LeaderboardFragmentDirections.actionLeaderboardFragmentToMainMenuFragment()
                    view.findNavController().navigate(action)
                }
            }

        view.findViewById<ImageView>(R.id.leaderboardAddFriend).setOnClickListener {

            val inflater = layoutInflater

            val inflateView = inflater.inflate(R.layout.add_friend, null)
            var addFriendId = inflateView.findViewById<EditText>(R.id.addFriendId)
            val addFriendCustomName = inflateView.findViewById<EditText>(R.id.addFriendCustomName)

            val builder = AlertDialog.Builder(requireActivity())

            //builder.setTitle("Edit your profile")
            //builder.setMessage("We have a message")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("add friend") { dialog, which ->
                Toast.makeText(requireActivity(),
                    "done successfully", Toast.LENGTH_SHORT).show()

                Log.i("EMAIL FRIENDA:", addFriendId.text.toString().trim())
                Log.i("CUSTOM_NAME FRIENDA:", addFriendCustomName.text.toString().trim())
                viewModel.addFriend(addFriendId.text.toString().trim(), addFriendCustomName.text.toString())

            }

            builder.setNegativeButton("cancel") { dialog, which ->
                Toast.makeText(requireActivity(),
                    "canceled", Toast.LENGTH_SHORT).show()
            }


            builder.setView(inflateView)
            builder.show()
        }
    }

}