package com.example.hritage.dataFromWeb


import android.util.Log
import com.example.hritage.R
import com.example.hritage.model.ListOfDataFromWebModel
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


open class GetDataFromWeb {


    private fun retrofitObj(url: String): Retrofit {
        return Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("$url/")
            .build()
    }

    //For getting notice from HITK
    open suspend fun noticeDataFromWeb(): ListOfDataFromWebModel {
        val retrofit = retrofitObj("https://www.heritageit.edu/Notice.aspx")
        val api = retrofit.create(API1::class.java)
        val list: ArrayList<String> = ArrayList()
        val list1: ArrayList<String> = ArrayList()
        val result = api.getPageResponse("https://www.heritageit.edu/Notice.aspx")
        val model: ListOfDataFromWebModel
        if (result.isSuccessful && result.body() != null) {

            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val list2: ArrayList<String> = ArrayList()
            val list3: ArrayList<String> = ArrayList()

            val element1 = htmlDoc?.getElementById("ctl00_ContentPlaceHolder1_GridView1")
            val idOfNotice = kotlin.collections.ArrayList<String>()
            element1?.forEach {
                idOfNotice.add(it.getElementsByTag("span").attr("id"))
            }
            val sortingTheNameOnly = kotlin.collections.ArrayList<String>()
            val sortingTheDateOnly = kotlin.collections.ArrayList<String>()
            idOfNotice.forEach {
                if (it.contains("lblHeading")) {
                    sortingTheNameOnly.add(it)
                } else if (it.contains("lblPublishDate")) {
                    sortingTheDateOnly.add(it)
                }
            }
            Log.i("dataOfL2", sortingTheDateOnly.distinct().size.toString())
            Log.i("dataOfL2", sortingTheNameOnly.toString())
            sortingTheNameOnly.distinct().forEach {
                element1?.getElementById(it)?.text()?.let { it1 -> list.add(it1) }

            }
            //GOt the Date Here
            sortingTheDateOnly.distinct().forEach {
                element1?.getElementById(it)?.text()?.let { it1 -> list1.add(it1) }
            }

            val element = htmlDoc?.select("a")
            element?.forEach {
                list2.add(it.attr("href").toString())

            }
            list2.forEach {
                if (it.contains("NoticePDF/")) {
                    list3.add(it)
                }
            }

            model = ListOfDataFromWebModel(list, list3, R.drawable.file, list1)//TODO Date

        } else {
            model = ListOfDataFromWebModel(
                ArrayList(),
                ArrayList(),
                R.drawable.arrow_right,
                ArrayList()
            )
        }
        return model
    }

    //For getting Quick Link From HITK
    open suspend fun getQuickLinkDataFromWeb(): ListOfDataFromWebModel {
        val retrofit = retrofitObj("https://www.heritageit.edu/QuickLink.aspx")
        val api = retrofit.create(API1::class.java)
        val list: ArrayList<String> = ArrayList()
        val list1: ArrayList<String> = ArrayList()
        val result = api.getPageResponse("https://www.heritageit.edu/QuickLink.aspx")
        val model: ListOfDataFromWebModel = if (result.isSuccessful && result.body() != null) {
            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val element = htmlDoc?.select("a")
            element?.forEach {
                list.add(it.attr("href").toString())
                list1.add(it.text())

            }
            ListOfDataFromWebModel(list1, list, R.drawable.arrow_right, ArrayList())

        } else {
            ListOfDataFromWebModel(ArrayList(), ArrayList(), R.drawable.arrow_right, ArrayList())
        }
        return model
    }

    //For getting downloads link from HITK
    open suspend fun getDowloadListDataFromWeb(): ListOfDataFromWebModel {
        val retrofit = retrofitObj("https://www.heritageit.edu/Download.aspx")
        val api = retrofit.create(API1::class.java)
        val list: ArrayList<String> = ArrayList()
        val list1: ArrayList<String> = ArrayList()
        val result = api.getPageResponse("https://www.heritageit.edu/Download.aspx")
        val model: ListOfDataFromWebModel = if (result.isSuccessful && result.body() != null) {
            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val element = htmlDoc?.select("a")
            element?.forEach {
                list.add(it.attr("href").toString())
                list1.add(it.text())

            }
            ListOfDataFromWebModel(list1, list, R.drawable.arrow_right, ArrayList())

        } else {
            ListOfDataFromWebModel(ArrayList(), ArrayList(), R.drawable.arrow_right, ArrayList())
        }
        return model
    }

    //For getting Library data From Web
    open suspend fun getLibraryDataFromWeb(): ListOfDataFromWebModel {

        val retrofit = retrofitObj("https://www.heritageit.edu/Library.aspx")
        val api = retrofit.create(API1::class.java)
        val result = api.getPageResponse("https://www.heritageit.edu/Library.aspx")
        val list1 = ArrayList<String>()
        val list2 = ArrayList<String>()
        val model = if (result.isSuccessful && result.body() != null) {
            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val element = htmlDoc?.getElementsByClass("col-6 col-md-4 rgtborder")
            val element1 = element?.select("a")
            element1?.forEach {
                list1.add(it.text())
                list2.add(it.attr("href"))
            }
            ListOfDataFromWebModel(list1, list2, R.drawable.arrow_right, ArrayList())
        } else {
            ListOfDataFromWebModel(ArrayList(), ArrayList(), R.drawable.arrow_right, ArrayList())
        }
        return model
    }

    open suspend fun getClassesRoutineFromWeb(): ListOfDataFromWebModel {
        val retrofit = retrofitObj("https://www.heritageit.edu/routines.aspx")
        val api = retrofit.create(API1::class.java)
        val result = api.getPageResponse("https://www.heritageit.edu/routines.aspx")
        if (result.isSuccessful && result.body() != null) {
            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val element = htmlDoc?.getElementById("ctl00_ContentPlaceHolder1_GridView1")
            val element1 = element?.getElementsByTag("span")
            val element2 = element?.getElementsByTag("a")
            val l1 = ArrayList<String>()
            element1?.forEach {
                if (it.text().length > 14 && !it.text().contains("=")) {
                    l1.add(it.text())
                }
            }
            Log.i("Span", l1.size.toString())
            l1.forEach {
                Log.i("Span", it)

            }
            val l2 = ArrayList<String>()
            element2?.forEach {
                if (it.attr("href").contains(".pdf")) {
                    l2.add(it.attr("href"))
                }
            }
            l2.forEach {
                Log.i("Routine", it)
            }
            Log.i("Span", l2.size.toString())
            return ListOfDataFromWebModel(l1, l2, R.drawable.arrow_right, ArrayList())
        } else {
            return ListOfDataFromWebModel(
                ArrayList(),
                ArrayList(),
                R.drawable.arrow_right,
                ArrayList()
            )
        }
    }

    open suspend fun getResultLinksFromWeb(): ListOfDataFromWebModel {
        val retrofit = retrofitObj("https://www.heritageit.edu/ExamCellDetails.aspx")
        val api = retrofit.create(API1::class.java)
        val result = api.getPageResponse("https://www.heritageit.edu/ExamCellDetails.aspx")
        val name = ArrayList<String>()
        val Links = ArrayList<String>()
        if (result.isSuccessful && result.body() != null) {
            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
            val element = htmlDoc?.getElementById("GridView2")
            val element1 = element?.getElementsByTag("span")
            val element2 = element?.getElementsByTag("a")
            element1?.forEach {
                if (it.id().contains("lblHeading")) {
                    name.add(it.getElementById(it.id())?.text().toString())
                    Log.i("Result", it.getElementById(it.id())?.text().toString())
                }
            }
            element2?.forEach {
                if (it.attr("href").toString().contains("http://136.232.2.202")) {
                    Links.add(it.attr("href"))
                }
            }
            Log.i("Result", name.size.toString())
            Log.i("Result", Links.size.toString())
            Log.i("Result", Links.toString())
            return ListOfDataFromWebModel(name, Links, R.drawable.arrow_right, ArrayList())
        } else {
            return ListOfDataFromWebModel(
                ArrayList(),
                ArrayList(),
                R.drawable.arrow_right,
                ArrayList()
            )
        }
    }

//    open suspend fun getImagesFromWeb(): ArrayList<String> {
//        val retrofit = retrofitObj("https://www.heritageit.edu/")
//        val api = retrofit.create(API1::class.java)
//        val result = api.getPageResponse("https://www.heritageit.edu/")
//        val links = ArrayList<String>()
//        if (result.isSuccessful && result.body() != null) {
//            val htmlDoc = result.body()?.let { Jsoup.parse(it) }
//            val element = htmlDoc?.getElementsByClass("slide-group")
//            Log.i("SlideGroup",""+element)
//
//
//        } else{
//
//        }
//    }
}
interface API1 {
    @GET
    suspend fun getPageResponse(@Url url: String): Response<String>
}