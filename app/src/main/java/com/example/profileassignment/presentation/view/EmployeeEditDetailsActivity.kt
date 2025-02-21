package com.example.profileassignment.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.profileassignment.R
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.databinding.ActivityProfileBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EmployeeEditDetailsActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private lateinit var employee: Employee
    private var empAddedLiveData = MutableLiveData<String>()
    private var empAddedErrorLiveData = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        db = FirebaseFirestore.getInstance()
        employee= intent.getSerializableExtra("Employee") as Employee
        binding.EvUserName.setText(employee.name)
        binding.EvDesignation.setText(employee.designation)
        binding.EvDateOfBirth.setText(employee.dob)
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                updateEmp(
                    employee,
                    employee.id,
                    binding.EvUserName.text.toString(),
                    binding.EvDesignation.text.toString(),
                    binding.EvDateOfBirth.text.toString()
                )
            }
        }
        binding.ivBackArrow.setOnClickListener {
            finish()
        }
        observer()
    }
    fun observer(){
        empAddedLiveData.observe(this) {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        empAddedErrorLiveData.observe(this){
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }
    private fun updateEmp(
        courses: Employee,
        id:String,
        name: String,
        designation: String,
        dob: String
    ) {

         val updatedEmp: Employee = Employee(id,name, designation, dob)


        db.collection("Employee").document(id).set(updatedEmp).addOnSuccessListener()
         {
             empAddedLiveData.value="Employee Details has been updated.."
        }.addOnFailureListener()
        {
            empAddedErrorLiveData.value="Fail to update the data.."
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    }