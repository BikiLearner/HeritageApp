package com.example.hritage.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hritage.R
import com.example.hritage.activities.Login
import com.example.hritage.activities.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class loginFrag:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_login, container, false)

        loginFunctionality(view)
        return view
    }

    private fun loginFunctionality(view: View?) {
        loginDataFromFirebase(view)
//        loginButtonWorking(view)
        registerButtonClicked(view)
    }

    private fun loginDataFromFirebase(view: View?) {
        val lba=view?.findViewById<MaterialButton>(R.id.login_button_activity)
        lba!!.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.editTextEmail)?.text.toString()
            val password = view.findViewById<EditText>(R.id.editTextPassword)?.text.toString()
            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        val auth=FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            Toast.makeText(context,"Login successful",Toast.LENGTH_LONG).show()
//            dataShave(Constant.PROFILEFLAG, 1)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            Login().finish()

        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun registerButtonClicked(view: View?) {
        val gtr=view?.findViewById<TextView>(R.id.goToRegister)
        gtr?.setOnClickListener {
            loadFragment(RegisterFrag(),1)
        }
    }


    private fun loadFragment(fragment: Fragment, flag: Int) {
        val fragmentManager=getFragmentManager()
        val fragmentTransaction=fragmentManager?.beginTransaction()
        if(flag==0)
            fragmentTransaction?.add(R.id.container1,fragment)
        else{
            fragmentTransaction?.replace(R.id.container1,fragment)
        }
        fragmentTransaction?.commit()
    }
}