package com.example.hritage.frag

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.Constant
import com.example.hritage.R
import com.example.hritage.adapters.DocListAdapter
import com.example.hritage.model.ListOfDataFromWebModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DocListFrag : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_doc_list, container, false)
        allFunctionality(view)
        return view
    }

    private fun allFunctionality(view: View?) {
       val flag= arguments?.getString(Constant.SHAREWITHFRAG,"")
        if(flag!! == Constant.QUICKLIST) {
            getDataFromJson(view,Constant.QUICKLIST,false)
            Toast.makeText(context,"InsideQlist",Toast.LENGTH_LONG).show()
        }else if (flag == Constant.NOTICEPDFS){
            getDataFromJson(view,Constant.NOTICEPDFS,false)
            Toast.makeText(context,"InsideQlis3r32r2t",Toast.LENGTH_LONG).show()
        }else if (flag==Constant.DOWNLOADLIST){
            getDataFromJson(view,Constant.DOWNLOADLIST,false)
        }else if (flag == Constant.LIBRARYLIST){
            getDataFromJson(view,Constant.LIBRARYLIST,true)
        }
        else{
            getDataFromJson(view,"",false)
        }

    }
    private fun getDataFromJson(view: View?, jsonKey: String,normalLink:Boolean) {
       val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        object : TypeToken<ArrayList<String>>() {}.type
        val json: String? = sharedPreferences.getString(jsonKey, null)

        val rv=view?.findViewById<RecyclerView>(R.id.list_for_pdf_doc)
        val tv=view?.findViewById<TextView>(R.id.no_rv_content_present_tv)
        if(json!=null){
            if (json.isNotEmpty()){
                val obj: ListOfDataFromWebModel? = gson.fromJson(json,ListOfDataFromWebModel::class.java)
                if(obj?.nameOfContent?.isNotEmpty() == true){
                    rv?.visibility=View.VISIBLE
                    tv?.visibility=View.GONE
                }else{
                    rv?.visibility=View.GONE
                    tv?.visibility=View.VISIBLE
                }

                    Log.i("dataOfL2", obj!!.linkOfComponent.size.toString())
                    Log.i("dataOfL2", obj.nameOfContent.size.toString())

                val adapter = context?.let { DocListAdapter(obj,normalLink, it) }
                val linearLayout= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                linearLayout.stackFromEnd=false
                linearLayout.isSmoothScrollbarEnabled=false

                rv?.layoutManager=linearLayout
                rv?.adapter=adapter
            }else{
                rv?.visibility=View.GONE
                tv?.visibility=View.VISIBLE
            }
        }
    }


//  TODO  Delete Latter
//    private fun openInWebActivity(view: View?,url:String) {
//        val mv=view?.findViewById<MaterialButton>(R.id.mb_id_open_In_website)
//        mv?.setOnClickListener {
//           val intent=Intent(context,WebWiewActivity::class.java)
//            intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,url)
//           startActivity(intent)
//        }
//    }
//
//    private fun featchItemAndSetToRecyclarView(view: View?,whichPdf:String) {
//        val rv=view?.findViewById<RecyclerView>(R.id.list_for_pdf_doc)
//        val tv=view?.findViewById<TextView>(R.id.no_rv_content_present_tv)
//       val dataBase = context?.let { DatabaseForFileDataBase.getInstance(it.applicationContext) }
//        dataBase?.databaseForListDao()?.fetchContentByListId(whichPdf)?.observeForever{
//            val listOfData=it as ArrayList<DocumentLidtClassEntity>
//            if(listOfData.isNotEmpty()){
//                rv?.visibility=View.VISIBLE
//                tv?.visibility=View.GONE
//            }else{
//                rv?.visibility=View.GONE
//                tv?.visibility=View.VISIBLE
//            }
//            val adapter = context?.let { it1 -> DocListAdapter(listOfData, it1) }
//            val linearLayout= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
//            linearLayout.stackFromEnd=true
//            linearLayout.isSmoothScrollbarEnabled=false
//
//            rv?.layoutManager=linearLayout
//            rv?.adapter=adapter
//
//        }
//    }
//    private fun sendInAdapter2(view: View?, listData1: ArrayList<String>?) {
//        val rv=view?.findViewById<RecyclerView>(R.id.list_for_pdf_doc)
//        val tv=view?.findViewById<TextView>(R.id.no_rv_content_present_tv)
//    if (listData1 != null) {
//        Log.i("ItemLIstGetting", listData1.size.toString())
//        if(listData1.isNotEmpty()){
//            rv?.visibility=View.VISIBLE
//            tv?.visibility=View.GONE
//
//            val adapter = context?.let { DocListAdapter(listData1, it) }
//            val linearLayout= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
//            linearLayout.stackFromEnd=true
//            linearLayout.isSmoothScrollbarEnabled=false
//            rv?.layoutManager=linearLayout
//            rv?.adapter=adapter
//        }else{
//            rv?.visibility=View.GONE
//            tv?.visibility=View.VISIBLE
//        }
//    }

//    }


}

