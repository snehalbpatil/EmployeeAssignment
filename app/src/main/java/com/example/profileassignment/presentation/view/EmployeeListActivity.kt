package com.example.profileassignment.presentation.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profileassignment.R
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.databinding.ActivityEmployeelistBinding
import com.example.profileassignment.presentation.adapter.EmployeeListAdapter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


class EmployeeListActivity : AppCompatActivity() {
    private lateinit var  db: FirebaseFirestore
    private lateinit  var employeeList: ArrayList<Employee>
    private lateinit  var sortedEmployeeList: ArrayList<Employee>
    private var employeeListAdapter: EmployeeListAdapter? = null
    private  lateinit  var binding: ActivityEmployeelistBinding
    private var isSorted:Boolean=false
    private var sortedName:Any?=null
    var page: Int = 0
    var limit: Int = 2
    private var empLiveData=MutableLiveData<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_employeelist)
        init()
    // adding on scroll change listener method for our nested scroll view.
     /*   binding.nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                binding.idPBLoading.visibility=View.VISIBLE
                getDataFromDB(page, limit)
            }
        })*/

    }
  fun init(){
      employeeList = ArrayList<Employee>()
      sortedEmployeeList=ArrayList<Employee>()
      binding.ivAddEmp.setOnClickListener {
          // Do some work here
          val intent=Intent(this,ProfileActivity::class.java)
          startActivity(intent)
      }
      binding.ivFilter.setOnClickListener {
          if (isSorted) {
              isSorted=false
              sortedName= employeeList.sortByDescending { it.name }
              employeeListAdapter?.notifyDataSetChanged()
          }else{
              isSorted=true
              employeeList.sortedBy { sortedName.toString() }
              employeeListAdapter?.notifyDataSetChanged()
          }

      }
      binding.sV.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
          android.widget.SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(p0: String?): Boolean {
              return false
          }

          override fun onQueryTextChange(msg: String): Boolean {
              // inside on query text change method we are
              // calling a method to filter our recycler view.
              filter(msg)
              return false
          }
      })
     lifecycleScope.launch(Dispatchers.IO) {
         getDataFromDB(page, limit)
     }
      observer()
  }
    fun observer(){
        empLiveData.observe(this){
            employeeList.add(it)
            binding.rvEmpList.setHasFixedSize(true)
            binding.rvEmpList.setLayoutManager(LinearLayoutManager(this))
            employeeListAdapter = EmployeeListAdapter(employeeList, this)
            binding.rvEmpList.setAdapter(employeeListAdapter)

            employeeListAdapter?.notifyDataSetChanged()
            binding.idPBLoading.visibility= View.GONE
        }
    }
    private fun getDataFromDB(page: Int, limit: Int) {
        if (page > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            binding.idPBLoading.visibility= View.GONE
            return;
        }
        db.collection("Employee")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val emp = document.toObject(Employee::class.java)
                    // Log.d(TAG, "${document.id} => ${document.data}")
                    emp.id=document.id
                    if (::employeeList.isInitialized) {
                       // employeeList.add(emp)
                        empLiveData.value= emp
                            //employeeList.add(emp)
                    }

                }
            }
            .addOnFailureListener { exception ->
                // Log.w(TAG, "Error getting documents: ", exception)
                Toast.makeText(
                    this,
                    "Fail to get the data.",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }


    private fun filter(text: String) {
        val filteredlist: ArrayList<Employee> = ArrayList()
        for (item in employeeList) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            employeeListAdapter?.filterList(filteredlist)
        }
    }
fun callDetailsActivity(emp:Employee){
    val intent=Intent(this,EmployeeEditDetailsActivity::class.java)
    intent.putExtra("empName",emp.name)
    startActivity(intent)
}

    override fun onResume() {
        super.onResume()
        employeeListAdapter?.notifyDataSetChanged()
      //  binding.idPBLoading.visibility=View.VISIBLE
    }
}