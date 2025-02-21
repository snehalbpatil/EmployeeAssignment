package com.example.profileassignment.presentation.view

//import com.example.profileassignment.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.profileassignment.data.model.Employee
import com.example.profileassignment.databinding.ActivityProfileBinding
import com.example.profileassignment.utils.ChooseDate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Locale




class ProfileActivity : AppCompatActivity() , ChooseDate.DateListener {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityProfileBinding
    private var year = 0
    private var month = 0
    private var day = 0
    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
      var storage: FirebaseStorage? = null
     var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_profile)
        // Assign variable
        binding = DataBindingUtil.setContentView(this, com.example.profileassignment.R.layout.activity_profile);
        db = FirebaseFirestore.getInstance()

        binding.EvDateOfBirth.setOnClickListener {
           // openDOBDialog()
            showDateDialog()

        }

        binding.btnSubmit.setOnClickListener {
            val dbCourses = db.collection("Employee")
            dbCourses.document().id
            // adding our data to our courses object class.
            val courses: Employee = Employee(
                dbCourses.document().id,
                binding.EvUserName.text.toString(),
                binding.EvDesignation.text.toString(),
                binding.EvDateOfBirth.text.toString()
            )


            // below method is use to add data to Firebase Firestore.
            dbCourses.add(courses).addOnSuccessListener { // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(
                    this@ProfileActivity,
                    "Your Course has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }
                .addOnFailureListener { e -> // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    Toast.makeText(
                        this@ProfileActivity,
                        "Fail to add course \n$e",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

        }
    }
   /* fun openDOBDialog(){
        val calendar: Calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,

            object : OnDateSetListener {
                fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                    val newDate: Calendar = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)

                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val date: String = simpleDateFormat.format(newDate.getTime())
                }

                override fun onDateSet(
                    view: android.widget.DatePicker?,
                    year: Int,
                    month: Int,
                    dayOfMonth: Int
                ) {

                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }*/
    private fun showDateDialog() {
        val pickDialog = ChooseDate()
        pickDialog.show(supportFragmentManager, "")

    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        this.day = day;
        this.month = month;
        this.year = year;

        var selectedDate=day.toString() + "/" + month.toString() + "/" + year.toString()
        binding.EvDateOfBirth.setText(selectedDate.toString())
        Log.d("TAG","day::"+day)
        Log.d("TAG","day::"+month)
        Log.d("TAG","day::"+year)
        Log.d("TAG","selectedDate::"+selectedDate)
    }
}
    /*private fun SelectImage() {
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