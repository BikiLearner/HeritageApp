package com.example.hritage.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.hritage.Constant
import com.example.hritage.R

class onlineFacilityForStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_facility_for_student)

        buttonClick()
    }


    private fun buttonClick() {
        //1
        findViewById<LinearLayout>(R.id.online_fee_payment)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/InstructionHIT.aspx")
        }
        //2
        findViewById<LinearLayout>(R.id.hit_library_web_opac)?.setOnClickListener {
            openInWebActivity("https://hitkopac.lsease.in/")
        }
        //3
        findViewById<LinearLayout>(R.id.e_clearance_from)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/ClearanceFormHIT.aspx")
        }
        //4
        findViewById<LinearLayout>(R.id.e_clearance_status)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/ClearanceFormStatus.aspx")
        }
        //5
        findViewById<LinearLayout>(R.id.graduation_image_2020)?.setOnClickListener {
            onlyForGoogleDrive("https://drive.google.com/drive/folders/18pNl7Uru0c0Yscf-wn09vRHOgzfAptev")
        }
        //6
        findViewById<LinearLayout>(R.id.download_admission_letter)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/hitadmissionletterprint.aspx")
        }
        //7
        findViewById<LinearLayout>(R.id.autonomy_roll_ll)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/knowyourautonomyrollno.aspx")
        }
        //8
        findViewById<LinearLayout>(R.id.online_student_login)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/LoginStudent.aspx")
        }
        //9
        findViewById<LinearLayout>(R.id.graduation_image_2022)?.setOnClickListener {
            onlyForGoogleDrive("https://drive.google.com/drive/folders/1xhPHLDxOUWDvrVRwID2gcbxRIaT6Aq6Y")
        }
        findViewById<LinearLayout>(R.id.online_login_details)?.setOnClickListener {
            openInWebActivity("https://www.theheritage.ac.in/HIT/OfficialEmail.aspx")
        }
    }
    private fun openInWebActivity(url:String){
        val intent= Intent(this, WebWiewActivity::class.java)
        intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,url)
        startActivity(intent)
    }
    private fun onlyForGoogleDrive(url: String){
        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}