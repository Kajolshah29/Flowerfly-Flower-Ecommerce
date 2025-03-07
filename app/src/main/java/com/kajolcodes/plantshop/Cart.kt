package com.kajolcodes.plantshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kajolcodes.plantshop.R
import com.kajolcodes.plantshop.CartAdapter
import com.kajolcodes.plantshop.PlantCheckOut

class Cart : Fragment() {

    private lateinit var viewLayout: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalText: TextView

    private var plantList = ArrayList<PlantCheckOut>()
    private var plantListTemp = ArrayList<PlantCheckOut>()
    private var userUid: String? = null
    private var alreadyHave = false
    private var totalAmount: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().reference
        userUid = FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLayout = inflater.inflate(R.layout.fragment_cart, container, false)

        userUid?.let { uid ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("users").hasChild(uid)) {
                        initUI()
                        updateData()
                    } else {
                        showSnackbar("You do not have anything in the cart yet.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showSnackbar("Error fetching cart: ${error.message}")
                }
            })
        } ?: showSnackbar("User not logged in.")

        return viewLayout
    }

    private fun initUI() {
        recyclerView = viewLayout.findViewById(R.id.recyclerViewcart)
        totalText = viewLayout.findViewById(R.id.total)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cartAdapter = CartAdapter(requireContext(), plantList)
        recyclerView.adapter = cartAdapter
    }

    private fun updateData() {
        userUid?.let { uid ->
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    totalAmount = 0

                    if (plantList.isNotEmpty()) {
                        alreadyHave = true
                        plantListTemp.clear()
                    } else {
                        alreadyHave = false
                        plantListTemp.clear()
                    }

                    for (plantSnapshot in snapshot.child("users").child(uid).children) {
                        val plantName = plantSnapshot.key ?: continue
                        val plantCheckOut =
                            snapshot.child("plant").child(plantName).getValue(PlantCheckOut::class.java) ?: continue
                        val quantity = plantSnapshot.value as? Long ?: 1

                        plantCheckOut.quantity = quantity
                        plantList.add(plantCheckOut)
                        plantListTemp.add(plantCheckOut)
                        totalAmount += plantCheckOut.price * quantity
                    }

                    if (alreadyHave) {
                        plantList.clear()
                        plantList.addAll(plantListTemp)
                        cartAdapter.notifyDataSetChanged()
                        alreadyHave = false
                    } else {
                        cartAdapter.notifyDataSetChanged()
                        recyclerView.scheduleLayoutAnimation()
                    }

                    totalText.text = "Total: $totalAmount VND"
                }

                override fun onCancelled(error: DatabaseError) {
                    showSnackbar("Error fetching cart: ${error.message}")
                }
            })
        } ?: showSnackbar("User not logged in.")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(viewLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}
