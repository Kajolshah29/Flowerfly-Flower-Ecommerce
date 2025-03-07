package com.kajolcodes.plantshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import androidx.recyclerview.widget.RecyclerView
import com.kajolcodes.plantshop.R
import com.kajolcodes.plantshop.PlantAdapter
import com.kajolcodes.plantshop.Plant

class PlantOverview : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var plantList: ArrayList<Plant>
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var viewRoot: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewRoot = inflater.inflate(R.layout.fragment_plant_overview, container, false)
        initUI()
        updateData()
        return viewRoot
    }

    private fun initUI() {
        recyclerView = viewRoot.findViewById(R.id.recyclerView)
        databaseReference = FirebaseDatabase.getInstance().getReference("plant")
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        plantList = arrayListOf()
        plantAdapter = PlantAdapter(requireContext(), plantList)
        recyclerView.adapter = plantAdapter
    }

    private fun updateData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                plantList.clear() // Avoid duplicate entries
                for (dataSnapshot in snapshot.children) {
                    val plant = dataSnapshot.getValue(Plant::class.java)
                    plant?.let { plantList.add(it) }
                }
                plantAdapter.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error (optional: show a Toast message)
            }
        })
    }
}
