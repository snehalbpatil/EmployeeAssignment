package com.example.profileassignment.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.profileassignment.R
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.databinding.ActivityProfileBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore


class EmployeeEditDetailsActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private lateinit var employee: Employee
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_profile)
        // Assign variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        db = FirebaseFirestore.getInstance()
        employee= intent.getSerializableExtra("Employee") as Employee
        binding.EvUserName.setText(employee.name)
        binding.EvDesignation.setText(employee.designation)
        binding.EvDateOfBirth.setText(employee.dob)
        binding.btnSubmit.setOnClickListener {
updateCourses(employee,employee.id,binding.EvUserName.text.toString(),binding.EvDesignation.text.toString(),binding.EvDateOfBirth.text.toString())
        }

    }
    private fun updateCourses(
        courses: Employee,
        id:String,
        name: String,
        designation: String,
        dob: String
    ) {
        val dbCourses = db.collection("Employee")
        dbCourses.document().id

        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        val updatedCourse: Employee = Employee(id,name, designation, dob)


        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        db.collection("Employee").document(id).set(updatedCourse).addOnSuccessListener()
         { // on successful completion of this process
            // we are displaying the toast message.
            Toast.makeText(this, "Employee Details has been updated..", Toast.LENGTH_SHORT)
                .show()
             finish()
        }.addOnFailureListener()
        {
            Toast.makeText(this, "Fail to update the data..", Toast.LENGTH_SHORT)
                .show()
        }
    }
    }