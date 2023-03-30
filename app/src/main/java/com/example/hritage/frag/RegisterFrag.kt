package com.example.hritage.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hritage.R
import com.example.hritage.activities.Login
import com.example.hritage.activities.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFrag : Fragment() {
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
        val view= inflater.inflate(R.layout.fragment_register, container, false)
        uiFunctionality(view)
        return view
    }

    private fun uiFunctionality(view: View?) {
        //performClickOnDobAndAdd(view)
//        getDataFromEditText(view)
//        getDataFromJson(view)
        registerFirebase(view)


    }

    private fun registerFirebase(view: View?) {
        view?.findViewById<MaterialButton>(R.id.register_btn)?.setOnClickListener {
            val eEd= view.findViewById<EditText>(R.id.editTextEmailReg)?.text.toString()
            val passEd= view.findViewById<EditText>(R.id.editTextPasswordReg)?.text.toString()

            if(eEd.isEmpty() || passEd.isEmpty()){
                Toast.makeText(context,"Empty credential",Toast.LENGTH_LONG).show()
            }else if(passEd.length<6){
                Toast.makeText(context,"Password too short",Toast.LENGTH_LONG).show()
            }else{
                register(eEd,passEd)
            }
        }

    }

    private fun register(eEd: String, passEd: String) {
        val auth=FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(eEd,passEd).addOnCompleteListener {
            if(it.isSuccessful) {
                Toast.makeText(context, "You have been registered SuccessFully", Toast.LENGTH_LONG).show()
                val intent= Intent(context, MainActivity::class.java)
                startActivity(intent)
                Login().finish()

            }else{
                Toast.makeText(context, "Registration failed sorry!! Try Again", Toast.LENGTH_LONG).show()
            }
        }
    }


}