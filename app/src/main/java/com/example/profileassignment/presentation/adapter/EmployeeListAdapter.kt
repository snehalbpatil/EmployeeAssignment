package com.example.profileassignment.presentation.adapter


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.profileassignment.R
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.presentation.view.EmployeeEditDetailsActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore


class EmployeeListAdapter(
    empArrayList: ArrayList<Employee>,
    private var context: Context
) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>() {
    // creating variables for our ArrayList and context
    private var empArrayList: ArrayList<Employee> = empArrayList
    private lateinit var  db: FirebaseFirestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // passing our layout file for displaying our card item
        db = FirebaseFirestore.getInstance()
        this.context = context;
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.emplist_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our text views from our modal class.
        val emp: Employee = empArrayList[position]
        holder.tvName.setText(emp.name)
        holder.tvEmpDesignation.setText(emp.designation)
        holder.tvEmpDOB.setText(emp.dob)
        holder.ivUpdate.setOnClickListener {
            val intent = Intent(context, EmployeeEditDetailsActivity::class.java)
            intent.putExtra("Employee",emp)
            context.startActivity(intent)
        }
        holder.ivDelete.setOnClickListener {
            showDialog(emp,position)

        }
    }
    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<Employee>) {
        // below line is to add our filtered
        // list in our course array list.
        empArrayList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        // returning the size of our array list.
        return empArrayList.size
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our text views.
        // initializing our text views.
         val tvName: TextView = itemView.findViewById<TextView>(R.id.tvName)
        val tvEmpDesignation: TextView = itemView.findViewById<TextView>(R.id.tvEmpDesignation)
        val tvEmpDOB: TextView = itemView.findViewById<TextView>(R.id.tvEmpDOB)
        val ivDelete:ImageView=itemView.findViewById<ImageView>(R.id.ivDelete)
        val ivUpdate:ImageView=itemView.findViewById<ImageView>(R.id.ivUpdate)
    }
    private fun showDialog(emp:Employee,position: Int) {
        val dialog = Dialog(context)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.delete_dialog_bg)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_alert_dialog)
        val yesBtn = dialog.findViewById(R.id.btnYes) as Button
        yesBtn.setOnClickListener {
            db.collection("Employee")
                .document(emp.id)
                .delete()
                .addOnSuccessListener(OnSuccessListener<Void?> { // Remove the deleted item from the list
                    empArrayList.removeAt(position)
                    notifyDataSetChanged()                // Notify the adapter that the data has changed
                    dialog.dismiss()
                }).addOnFailureListener(OnFailureListener {
                    Toast.makeText(context, "Successfully deleted user", Toast.LENGTH_SHORT).show()
                })


        }


        val noBtn = dialog.findViewById(R.id.btnNo) as Button
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
