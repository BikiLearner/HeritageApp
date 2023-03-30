package com.example.hritage.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.hritage.Constant
import com.example.hritage.R
import com.example.hritage.databinding.ActivityProfileBinding
import com.example.hritage.model.TheProfileModel
import com.example.hritage.model.profileModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException


open class Profile : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProfileBinding
    private lateinit var jsonObject:profileModel
    private lateinit var theUri:Uri
    private lateinit var showContentDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (checkForInternet(this)) {
            binding.appBar.visibility = View.GONE
            binding.nestedSsView.visibility = View.GONE
            binding.progressBarCircularTv.visibility=View.VISIBLE
            toSaveToClipBoard()
            buttonFunctionality()
            getUserDataFromFirebase()


        } else {
            Toast.makeText(this, "Please Turn On Internet Connection", Toast.LENGTH_LONG).show()
            changeTHeVisibility(true)
        }

    }
    private fun changeTHeVisibility(flag:Boolean){
        if(flag) {
            binding.appBar.visibility = View.GONE
            binding.nestedSsView.visibility = View.GONE
            binding.noInternetTv.visibility = View.VISIBLE
        }else{
            binding.appBar.visibility = View.VISIBLE
            binding.nestedSsView.visibility = View.VISIBLE
            binding.noInternetTv.visibility = View.GONE
        }
    }
    private fun buttonFunctionality(){
        binding.logOutBtn.setOnClickListener {
//            dataShave(Constant.PROFILEFLAG, 0)
            val auth=FirebaseAuth.getInstance()
            auth.signOut()
            if(auth.currentUser==null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.backToHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.profileImage.setOnClickListener {
            setImageOnFrameFunction()
        }
        binding.editProfileBtn.setOnClickListener {
            showTheContentOfItemClicked()
        }
    }

    private fun getDataFromJson() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        val json: String? = sharedPreferences.getString(Constant.GETDATAANDSAVE, "")


        if(json!=null){
            if (json.isNotEmpty()){
                val obj: profileModel = gson.fromJson(json, profileModel::class.java)
                setTheDataToTheView(obj)
                jsonObject=obj

            }
        }
    }

    private fun setTheDataToTheView(obj: profileModel) {
        binding.toolBarTvName.text=obj.name
        binding.toolBarTvEmail.text=obj.email
        binding.nameTextView.text=obj.name
        binding.moblieNoTextView.text=obj.phoneNumber
        binding.emailTextView.text=obj.email
        binding.addressTextView.text=obj.address

        binding.dobTextView.text=obj.dateOfBirth
        binding.collegeRollTextView.text=obj.collegeRoll
        binding.collegeIDTextView.text=obj.collegeID
//        binding.profileImage.setImageBitmap(obj.defaultProfile)

        binding.logOutBtn.setOnClickListener {
//            dataShave(Constant.PROFILEFLAG, 0)
            val auth=FirebaseAuth.getInstance()
            auth.signOut()
            if(auth.currentUser==null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.backToHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.profileImage.setOnClickListener {
            setImageOnFrameFunction()
        }
        binding.editProfileBtn.setOnClickListener {
            showTheContentOfItemClicked()
        }

    }
    private fun setTheDataToTheView1(obj: TheProfileModel, email: String) {
        binding.toolBarTvName.text=obj.name
        binding.toolBarTvEmail.text=email
        binding.nameTextView.text=obj.name
        binding.moblieNoTextView.text=obj.phoneNumber
        binding.emailTextView.text=email
        binding.addressTextView.text=obj.address
        binding.profileImage.setImageURI(obj.uri.toUri())
        Toast.makeText(this,""+obj.uri,Toast.LENGTH_LONG).show()

        binding.dobTextView.text=obj.dateOfBirth
        binding.collegeRollTextView.text=obj.collegeRoll
        binding.collegeIDTextView.text=obj.collegeID
//        binding.profileImage.setImageBitmap(obj.defaultProfile)



    }
    private fun toSaveToClipBoard(){
        binding.phoneNumberRl.setOnClickListener {
            toSaveCode(binding.moblieNoTextView.text.toString())
        }
        binding.emailRl.setOnClickListener {
            toSaveCode(binding.emailTextView.text.toString())
        }
        binding.addressRl.setOnClickListener {
            toSaveCode(binding.addressTextView.text.toString())
        }
        binding.collegeRollRl.setOnClickListener {
            toSaveCode(binding.collegeRollTextView.text.toString())
        }
        binding.collegeIdRl.setOnClickListener {
            toSaveCode(binding.collegeIDTextView.text.toString())
        }
    }
   private fun toSaveCode(text:String){
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        Toast.makeText(this,""+text+"is Copied",Toast.LENGTH_LONG).show()
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
    private fun dataShave(Key:String,data:Int){
        val myEdit=sharedPreferences.edit()
        myEdit.putInt(Key, data)
        myEdit.apply()
    }

    open fun setImageOnFrameFunction() {
        val pictureDialog=AlertDialog.Builder(this@Profile)
        pictureDialog.setTitle("Select Action")
        val pictureDialogActions= arrayOf("Select photo from gallery"
            ,"Capture Image using Camera")
        pictureDialog.setItems(pictureDialogActions){
                _, which->
            when(which){
                0 ->{
                    choosePhotoFromGallery()
                    Toast.makeText(this@Profile,
                        "0 is selected",Toast.LENGTH_SHORT).show()
                }
                1->{
                    takePhotoFromCamera()
                    Toast.makeText(this@Profile,
                        "1 is selected",Toast.LENGTH_SHORT).show()
                }
            }
        }
        pictureDialog.show()
    }
    //for camera
    private fun takePhotoFromCamera() {
        if(Build.VERSION.SDK_INT>32){
            if(ContextCompat.checkSelfPermission(this@Profile,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@Profile,"Media permission granted",Toast.LENGTH_SHORT).show()
                val galleryIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(galleryIntent, Constant.CAMERA)
            }else{
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.CAMERA
                    ), Constant.CAMERA
                )

            }
        }else {
            Dexter.withContext(this@Profile).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,

                ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        Toast.makeText(
                            this@Profile,
                            "You can choose photo from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                        val galleryIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(galleryIntent, Constant.CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }

            }).onSameThread().check()
        }
    }

    //image from gallery
    private fun choosePhotoFromGallery() {

        if(Build.VERSION.SDK_INT>32){

            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Media permission granted",Toast.LENGTH_SHORT).show()
                val galleryIntent=Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, Constant.GALLERY)
            }else{
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ),Constant.GALLERY
                )
            }

        }else {
            Dexter.withContext(this).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,

                ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        Toast.makeText(
                            this@Profile,
                            "You can choose photo from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                        val galleryIntent=Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, Constant.GALLERY)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }

            }).onSameThread().check()
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode== Constant.GALLERY){
                if (data!=null){
                    val contentUri=data.data
                    try {
                        theUri=contentUri!!
                        showContentDialog.findViewById<CircleImageView>(R.id.profile_image_setUp)
                            .setImageURI(theUri)
                        val selectedImageBitmap=
                            MediaStore.Images.Media.getBitmap(this.contentResolver,contentUri)
                        binding.profileImage.setImageBitmap(selectedImageBitmap)

                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else if(requestCode== Constant.CAMERA) {
                theUri=data!!.data!!
                showContentDialog.findViewById<CircleImageView>(R.id.profile_image_setUp)
                    .setImageURI(theUri)
                val cameraImg: Bitmap = data.extras!!.get("data") as Bitmap
                binding.profileImage.setImageBitmap(cameraImg)

            }
        }
    }
    private fun showRationalDialogForPermission() {

        AlertDialog.Builder(this).setMessage("We need permission to access the gallery where your images are save " +
                "to insert in the app" +
                "so if you denied the permission please enable it in the setting -> Application -> Choose NewHappyPlace -> Permission -> Photo and Video ->" +
                "Choose your preference")
            .setPositiveButton("Go to settings"){
                    _,_->
                try {
                    val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri= Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                    dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun showTheContentOfItemClicked() {
        showContentDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        showContentDialog.setContentView(R.layout.set_up_profile)
        showContentDialog.show()
        emptyEditText()
    }
//   TODO set dialog data
    private fun emptyEditText() {

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        showContentDialog.findViewById<CircleImageView>(R.id.profile_image_setUp)
            .setOnClickListener {
                setImageOnFrameFunction()
            }
        showContentDialog.findViewById<MaterialButton>(R.id.save_btn)
            .setOnClickListener {
                val name =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_name).text.toString()
                val number =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_mobile_no).text.toString()
                val address =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_address).text.toString()
                val dob =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_dob).text.toString()
                val roll =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_roll).text.toString()
                val id =
                    showContentDialog.findViewById<EditText>(R.id.edit_text_id).text.toString()

                val profileModel =
                    TheProfileModel(name, address, number, roll, id, dob, theUri.toString())

                if (uid != null) {
                    databaseReference.child(uid).setValue(profileModel).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Ur profile is updated", Toast.LENGTH_LONG).show()
                            showContentDialog.dismiss()
                            getUserDataFromFirebase()
                        } else {
                            Toast.makeText(
                                this,
                                " Failed to Ur profile is updated",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }


            }
    }
    private fun getUserDataFromFirebase(){
        val auth=FirebaseAuth.getInstance()
        val uid=auth.currentUser?.uid
        val databaseReference= FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uid!!).get().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
                if(it.result.exists()){
                    val snapshot=it.result
                    Toast.makeText(this, "success${snapshot.child("address").value}",Toast.LENGTH_LONG).show()
                    val model=TheProfileModel(snapshot.child("name").value.toString(),
                        snapshot.child("address").value.toString(),
                        snapshot.child("phoneNumber").value.toString(),
                        snapshot.child("collegeRoll").value.toString(),
                        snapshot.child("collegeID").value.toString(),
                        snapshot.child("dateOfBirth").value.toString(),
                        snapshot.child("uri").value.toString(),
                    )
                    setTheDataToTheView1(model,auth.currentUser!!.email.toString())
                    binding.progressBarCircularTv.visibility=View.GONE
                    changeTHeVisibility(false)
                }
            }else{
                Toast.makeText(this,"Not Success",Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }
}