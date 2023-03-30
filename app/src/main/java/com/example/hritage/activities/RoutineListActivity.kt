package com.example.hritage.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.Constant
import com.example.hritage.R
import com.example.hritage.adapters.SearchAdapter
import com.example.hritage.model.ListOfDataFromWebModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RoutineListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noSearchResultsFoundText: TextView
    private lateinit var editText: AppCompatEditText
    private var model:ListOfDataFromWebModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_list)

        recyclerView = findViewById(R.id.search_list)
        noSearchResultsFoundText = findViewById(R.id.no_search_results_found_text)
        model=getDataFromJson(Constant.ROUTINELIST)


        attachAdapter(model!!.nameOfContent,model!!.linkOfComponent)
        editText = findViewById(R.id.search_edit_text)
        editText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().toLowerCase(Locale.getDefault())
            filterWithQuery(query)
        }

    }

    private fun onQueryChanged(filterQuery: String): ListOfDataFromWebModel {
        // Empty new array list which contains new strings

        val filteredList = ArrayList<String>()
        val filteredList2 = ArrayList<String>()
        Log.i("hero","in this function"+model?.nameOfContent!!.size)
        // Loop through each item in list
        for (i in 0 until model?.nameOfContent!!.size) {
            if (model!!.nameOfContent[i].lowercase().contains(filterQuery)) {
                filteredList.add(model?.nameOfContent!![i])
                filteredList2.add(model?.linkOfComponent!![i])
            }
            // Before checking string matching convert string to lower case.
        }

        return ListOfDataFromWebModel(filteredList, filteredList2,model!!.drawableId)
    }

    private fun filterWithQuery(query: String) {
        // Perform operation only is query is not empty
        if (query.isNotEmpty()) {
            // Call the function with valid query and take new filtered list.
            val filteredList: ListOfDataFromWebModel = onQueryChanged(query)
            // Call the adapter with new filtered array list
            attachAdapter(filteredList.nameOfContent,filteredList.linkOfComponent)
            // If the matches are empty hide RecyclerView and show error text
        } else if (query.isEmpty()) {
            // If query is empty don't make changes to list
            attachAdapter(model!!.nameOfContent,model!!.linkOfComponent)
        }
    }
    private fun attachAdapter(list: ArrayList<String>,list2: ArrayList<String>) {
        recyclerView.adapter = SearchAdapter(list,list2, this)
        if (list.isEmpty() && list2.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            noSearchResultsFoundText.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noSearchResultsFoundText.visibility = View.INVISIBLE
        }
    }

    private fun getDataFromJson(jsonKey: String):ListOfDataFromWebModel {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        object : TypeToken<ArrayList<String>>() {}.type
        val json: String? = sharedPreferences.getString(jsonKey, null)
        return if (json != null && json.isNotEmpty()) {
            gson.fromJson(json,ListOfDataFromWebModel::class.java)
        }else{
            ListOfDataFromWebModel(ArrayList(),ArrayList(),model!!.drawableId)
        }
    }

}