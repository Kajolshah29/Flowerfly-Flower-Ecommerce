package com.kajolcodes.plantshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kajolcodes.plantshop.R
import com.kajolcodes.plantshop.PlantCheckOut

class CartAdapter(private val context: Context, private val plantList: ArrayList<PlantCheckOut>) :
    RecyclerView.Adapter<CartAdapter.CartHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_plant_checkout, parent, false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val plant = plantList[position]
        holder.name.text = plant.name
        holder.quantity.text = plant.quantity.toString()
        holder.price.text = "${plant.price} Rs"
        Glide.with(context).load(plant.ImageUrl).placeholder(R.drawable.peacelily).into(holder.image)

        holder.addButton.setOnClickListener { view ->
            val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userRef = databaseReference.child("users").child(userUid).child(plant.name!!.toString())
                    val newAmount = (snapshot.child("users").child(userUid).child(plant.name!!.toString()).getValue(Long::class.java) ?: 0) + 1
                    userRef.setValue(newAmount)
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(view, "Error when adding item to cart", Snackbar.LENGTH_SHORT).show()
                }
            })
        }

        holder.subButton.setOnClickListener { view ->
            val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userRef = databaseReference.child("users").child(userUid).child(plant.name!!.toString())
                    val currentAmount = snapshot.child("users").child(userUid).child(plant.name.toString()).getValue(Long::class.java) ?: 0
                    if (currentAmount > 1) {
                        userRef.setValue(currentAmount - 1)
                    } else {
                        userRef.removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(view, "Error when removing item from cart", Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int = plantList.size

    inner class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.plantnamecheckout)
        val image: ImageView = itemView.findViewById(R.id.imagecheckout)
        val price: TextView = itemView.findViewById(R.id.pricecheckout)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val addButton: ImageView = itemView.findViewById(R.id.addbutton)
        val subButton: ImageView = itemView.findViewById(R.id.subbutton)
    }
}
