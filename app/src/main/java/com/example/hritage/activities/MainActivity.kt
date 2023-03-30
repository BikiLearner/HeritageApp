package com.example.hritage.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.hritage.Constant
import com.example.hritage.R
import com.example.hritage.dataFromWeb.GetDataFromWeb
import com.example.hritage.databinding.ActivityMainBinding
import com.example.hritage.frag.DocListFrag
import com.example.hritage.frag.HomeScreenFragment
import com.example.hritage.model.ListOfDataFromWebModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var fragNo = 1
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        toolBarFunctio()
        buttomNavBar()
        loadFragment(HomeScreenFragment(), 0, false, "")
//        profileCode()
        checkLoginStatus()
        if (checkForInternet(this)) {
            getDataFromHITKWeb()
        } else {
            Toast.makeText(this, "Please Turn On Internet Connection", Toast.LENGTH_LONG).show()
        }


    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }
    private fun checkLoginStatus(){
        val auth=FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            if (isUsingNightModeResources()) {
                binding?.profileButton?.setImageResource(R.drawable.profile_night)
            } else {
                binding?.profileButton?.setImageResource(R.drawable.profile)
            }
            binding?.profileButton?.setOnClickListener {
                val intent = Intent(this@MainActivity, Profile::class.java)
                startActivity(intent)
            }
        }else{
            binding?.profileButton?.setImageResource(R.drawable.login_icon)
            binding?.profileButton?.setOnClickListener {
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
            }
        }
    }
    private fun profileCode() {
        val profileFlag = sharedPreferences.getInt(Constant.PROFILEFLAG, 0)
        if (profileFlag == 1) {
            if (isUsingNightModeResources()) {
                binding?.profileButton?.setImageResource(R.drawable.profile_night)
            } else {
                binding?.profileButton?.setImageResource(R.drawable.profile)
            }
            binding?.profileButton?.setOnClickListener {
                val intent = Intent(this@MainActivity, Profile::class.java)
                startActivity(intent)
            }
        } else if (profileFlag == 0) {
            binding?.profileButton?.setImageResource(R.drawable.login_icon)
            binding?.profileButton?.setOnClickListener {
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
            }
        }
    }

    private fun toolBarFunctio() {
        setSupportActionBar(binding?.customToolBar)
        val toggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding?.drawerLayout,
            binding?.customToolBar, R.string.OpenDrawer, R.string.CloseDrawer
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        binding?.navDrawer?.setNavigationItemSelectedListener {
            drawerItemWork(it)
        }
//        With databinding .bringToFront() solved the issue. So:
//        _binding.navView.setNavigationItemSelectedListener(this) - not
//        enough. Then add _binding.navView.bringToFront() -
//                and the listener is triggered
        binding?.navDrawer?.bringToFront()
    }

    private fun buttomNavBar() {
        binding?.bottomNav?.setOnItemSelectedListener { item ->
            bottomNavItemFunction(item)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun bottomNavItemFunction(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_button -> {
                loadFragment(HomeScreenFragment(), 1, false, "")
                fragNo = 1
            }
            R.id.notice_button -> {
                loadFragment(DocListFrag(), 1, true, Constant.NOTICEPDFS)
            }
            R.id.events -> {
                Toast.makeText(this@MainActivity, "Events TODO Later", Toast.LENGTH_LONG).show()
                fragNo = 3
            }
            R.id.quickLinks -> {
                loadFragment(DocListFrag(), 1, true, Constant.QUICKLIST)
                fragNo = 4
            }

        }
        return true
    }

    private fun drawerItemWork(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.home -> {
                loadFragment(HomeScreenFragment(), 1, false, "")
                fragNo = 1
            }
            R.id.infrastructure -> {
                Toast.makeText(this@MainActivity, "infrastructure TODO Later", Toast.LENGTH_LONG).show()
            }
            R.id.training_placement -> {
                Toast.makeText(this@MainActivity, "training_placement TODO Later", Toast.LENGTH_LONG).show()
            }
            R.id.library -> {
                loadFragment(DocListFrag(), 1, true, Constant.LIBRARYLIST)
                fragNo=5
            }
            R.id.downloads -> {
                loadFragment(DocListFrag(), 1, true, Constant.DOWNLOADLIST)
                fragNo=6
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return false
    }

    private fun loadFragment(fragment: Fragment, flag: Int, sendData: Boolean, extraData: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (flag == 0 && !sendData)
            fragmentTransaction.add(R.id.container, fragment)
        else if (flag != 0 && !sendData)
            fragmentTransaction.replace(R.id.container, fragment)
        else if (flag != 0 && sendData) {
            val bundle = Bundle()
            bundle.putString(Constant.SHAREWITHFRAG, extraData)
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.container, fragment)
        }
        fragmentTransaction.commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else if (fragNo > 1) {
            loadFragment(HomeScreenFragment(), 1, false, "")
            fragNo = 0
        } else {
            super.onBackPressed()
        }
    }

    private fun getDataFromHITKWeb(){
        CoroutineScope(Dispatchers.IO).launch {
            val model = GetDataFromWeb().noticeDataFromWeb()
            saveDataName(model,Constant.NOTICEPDFS)
            val model1=GetDataFromWeb().getQuickLinkDataFromWeb()
            saveDataName(model1,Constant.QUICKLIST)
            val model2=GetDataFromWeb().getDowloadListDataFromWeb()
            saveDataName(model2,Constant.DOWNLOADLIST)
            val model3=GetDataFromWeb().getLibraryDataFromWeb()
            saveDataName(model3,Constant.LIBRARYLIST)
            val model4= GetDataFromWeb().getClassesRoutineFromWeb()
            saveDataName(model4,Constant.ROUTINELIST)
        }
    }

    private fun saveDataName(
        dataName: ListOfDataFromWebModel,
        contentName: String
    ) {
        val mPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(dataName)
        prefsEditor.putString(contentName, json)
        prefsEditor.apply()
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