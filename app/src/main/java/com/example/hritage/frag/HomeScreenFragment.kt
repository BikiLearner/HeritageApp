package com.example.hritage.frag

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.Constant
import com.example.hritage.R
import com.example.hritage.activities.RoutineListActivity
import com.example.hritage.activities.WebWiewActivity
import com.example.hritage.activities.onlineFacilityForStudentActivity
import com.example.hritage.adapters.collegeImageAdapter
import com.example.hritage.model.profileModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home_screen, container, false)
        functionlaityOfUi(view)
        return view
    }
    private fun functionlaityOfUi(view: View) {
        setTheImageInRv(view)
        onlineFacilityForStudent(view)
        onlineEnquiryForAdmission(view)
        onlineClassRoutines(view)
        onlineSemisterFormFillup(view)
        semesterResult(view)
        prrSemesterResult(view)

    }
    // back


    private fun openInWebBroweser(url: String){
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    private fun prrSemesterResult(view: View) {
        val cvOnlineFacility=view.findViewById<MaterialButton>(R.id.prr_sem_res_btn)
        cvOnlineFacility.setOnClickListener {
            openInWebBroweser("http://136.232.2.202:8084/stud23or.aspx")
        }
    }

    private fun onlineSemisterFormFillup(view: View) {
        val cvSemister=view.findViewById<CardView>(R.id.semister_form_fill_up)
        cvSemister.setOnClickListener {
            openWebActivity("https://www.theheritage.ac.in/instructionexamfee.aspx")
        }
    }

    private fun onlineClassRoutines(view: View) {
        val cvClassRoutines=view.findViewById<CardView>(R.id.class_routines_cv)
        cvClassRoutines.setOnClickListener {
//           loadFragment(DocListFrag(),Constant.ROUTINELIST)
            val intent=Intent(context, RoutineListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onlineEnquiryForAdmission(view: View) {
        val cvOnlineEnquiry=view.findViewById<CardView>(R.id.online_enquiry_cv)
        cvOnlineEnquiry.setOnClickListener {
            openWebActivity("https://www.heritageit.edu/BTechQueries.aspx")
        }
    }

    private fun onlineFacilityForStudent(view: View) {
        val cvOnlineFacility=view.findViewById<CardView>(R.id.online_facility_for_student)
        cvOnlineFacility.setOnClickListener {
//            loadFragment(OnlineFacilityScreenFrag(),1)
            val intent=Intent(context,onlineFacilityForStudentActivity::class.java)
            startActivity(intent)
        }

    }
    private fun semesterResult(view: View){
        val cvOnlineFacility=view.findViewById<MaterialButton>(R.id.odd_sem_res_btn)
        cvOnlineFacility.setOnClickListener {
            getDataFromJson()
            openInWebBroweser("http://136.232.2.202:8084/stud23o.aspx")
        }
    }
    private fun getDataFromJson() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = sharedPreferences.getString(Constant.GETDATAANDSAVE, "")


        if(json!=null){
            if (json.isNotEmpty()){
                val obj: profileModel = gson.fromJson(json, profileModel::class.java)
                toSaveCode(obj.collegeID)

            }
        }
    }
    private fun toSaveCode(text:String){
        val clipboard: ClipboardManager = context?.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager

        Toast.makeText(context,""+text+"is Copied",Toast.LENGTH_LONG).show()
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
    private fun openWebActivity(url:String){
        val intent=Intent(context,WebWiewActivity::class.java)
        intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,url)
        startActivity(intent)
    }

    private fun setTheImageInRv(view: View) {
        val rv=view.findViewById<RecyclerView>(R.id.college_moment_photos_rv)
        val imageList=arrayOf(R.drawable.visual_slide21_1,R.drawable.visual_slide22_1,
            R.drawable.visual_slide23_1,R.drawable.visual_slide26_1,
            R.drawable.visual_slide18_1,R.drawable.visual_slide40_1,R.drawable.visual_slide52_1,
            R.drawable.visual_slide57_1)
        val adapter= collegeImageAdapter(imageList)
        val linearLayout= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        rv.layoutManager=linearLayout
        rv.adapter=adapter
        val snapHelper= LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv)


        Timer().schedule(object : TimerTask() {
            override fun run() {
                linearLayout.smoothScrollToPosition(rv,
                    RecyclerView.State(), linearLayout.findFirstCompletelyVisibleItemPosition()+1)

            }
        }, 0,5000)

    }
    private fun loadFragment(fragment: Fragment, extraData: String) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString(Constant.SHAREWITHFRAG, extraData)
            fragment.arguments = bundle
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}