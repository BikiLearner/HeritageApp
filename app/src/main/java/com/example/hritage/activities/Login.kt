package com.example.hritage.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hritage.R
import com.example.hritage.databinding.ActivityLoginBinding
import com.example.hritage.frag.loginFrag


class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(loginFrag(),0)

    }


    private fun loadFragment(fragment: Fragment, flag: Int) {
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        if(flag==0)
            fragmentTransaction.add(R.id.container1,fragment)
        else{
            fragmentTransaction.replace(R.id.container1,fragment)
        }
        fragmentTransaction.commit()
    }

}