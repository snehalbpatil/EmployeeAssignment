package com.example.profileassignment.presentation.view


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.databinding.ActivityProfileBinding
import com.example.profileassignment.utils.ChooseDate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class ProfileActivity : AppCompatActivity(), ChooseDate.DateListener {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private var year = 0
    private var month = 0
    private var day = 0
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 22
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    private var empAddedLiveData = MutableLiveData<String>()
    private var empAddedErrorLiveData = MutableLiveData<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            com.example.profileassignment.R.layout.activity_profile
        );
        db = FirebaseFirestore.getInstance()

        binding.EvDateOfBirth.setOnClickListener {
            showDateDialog()

        }

        binding.btnSubmit.setOnClickListener {
            addEmployeeData()
        }
        observer()
    }

    fun observer() {
        empAddedLiveData.observe(this) {
            Toast.makeText(
                this@ProfileActivity,
                it,
                Toast.LENGTH_SHORT
            ).show()
          finish()
        }
        empAddedErrorLiveData.observe(this){
            Toast.makeText(
                this@ProfileActivity,
                it,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun addEmployeeData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dbCourses = db.collection("Employee")
            dbCourses.document().id
            val courses: Employee = Employee(
                dbCourses.document().id,
                binding.EvUserName.text.toString(),
                binding.EvDesignation.text.toString(),
                binding.EvDateOfBirth.text.toString()
            )


            // below method is use to add data to Firebase Firestore.
            dbCourses.add(courses).addOnSuccessListener {
                empAddedLiveData.value = "Employee has been added to Firebase Firestore"
            }
                .addOnFailureListener { e ->
                    empAddedErrorLiveData.value="Fail to add Employee"
                }
        }
    }

    private fun showDateDialog() {
        val pickDialog = ChooseDate()
        pickDialog.show(supportFragmentManager, "")

    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        this.day = day;
        this.month = month;
        this.year = year;

        var selectedDate = day.toString() + "/" + month.toString() + "/" + year.toString()
        binding.EvDateOfBirth.setText(selectedDate.toString())
    }
}
/*created for firebase storage private fun SelectImage() {
    // Defining Implicit Intent to mobile gallery

    val intent = Intent()
    intent.setType("image/*")
    intent.setAction(Intent.ACTION_GET_CONTENT)
    startActivityForResult(
        Intent.createChooser(
            intent,
            "Select Image from here..."
        ),
        PICK_IMAGE_REQUEST
    )
}
*/
// Override onActivityResult method
/*override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
) {
    super.onActivityResult(
        requestCode,
        resultCode,
        data
    )


    // checking request code and result code
    // if request code is PICK_IMAGE_REQUEST and
    // resultCode is RESULT_OK
    // then set image in the image view
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
        // Get the Uri of data

        filePath = data.data
        try {
            // Setting image on image view using Bitmap

            val bitmap = MediaStore
                .Images
                .Media
                .getBitmap(
                    contentResolver,
                    filePath
                )
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            // Log the exception
            e.printStackTrace()
        }
    }
}

}*/