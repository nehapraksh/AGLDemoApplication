package com.nehaprakash.aglapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.nehaprakash.aglapplication.model.People
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    private var maleList: MutableList<String> = mutableListOf<String>()
    private var femaleList: MutableList<String> = mutableListOf<String>()

    // UI Components
    private var maleHeaderTv: TextView? = null
    private var femaleHeaderTv: TextView? = null
    private var maleListTv: TextView? = null
    private var femaleListTv: TextView? = null
    private var headerTv: TextView? = null

    // URL to be called
    private val BASE_URL = "http://agl-developer-test.azurewebsites.net"
    private val CAT = "Cat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myCompositeDisposable = CompositeDisposable()
        initUI()
        loadData()
    }

    //UI components initialization
    private fun initUI() {
        maleHeaderTv = findViewById(R.id.male_list)
        femaleHeaderTv = findViewById(R.id.female_list)
        maleListTv = findViewById(R.id.mcat_list)
        femaleListTv = findViewById(R.id.fcat_list)
        headerTv = findViewById(R.id.header)


    }

    //Network call to load the data from server using Retrofit and RxJAVA
    private fun loadData() {
        try {
            val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(GetResponse::class.java)

            myCompositeDisposable?.add(requestInterface.getResponse()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result ->
                        run {
                            if(result.isNotEmpty())
                                handleResponse(result)
                        }
                    },
                    { error ->
                        run {
                            Toast.makeText(this, String.format("%s %s",this.resources.getString(R.string.error_msg), error.message), Toast.LENGTH_LONG).show()

                        }
                    }
                )
            )
        } catch (e: Exception) {
            Log.e("MainActivity", this.resources.getString(R.string.error_msg) + e.printStackTrace())
            Toast.makeText(this, String.format("%s %s",this.resources.getString(R.string.error_msg), e.message), Toast.LENGTH_LONG).show()
        }

    }


    // Function to extract data from the response
    private fun handleResponse(peopleList: List<People>) {
        headerTv?.text = this.resources.getString(R.string.header)
        // loop to categorise the cat list by gender
        for (people in peopleList) {
            if (null != people.pets) {
                if (people.gender == this.resources.getString(R.string.male)) {
                    for (pet in people.pets)
                        if (pet.type == CAT) {
                            maleList.add(pet.name + "\n")
                        }
                } else if (people.gender == this.resources.getString(R.string.female)) {
                    for (pet in people.pets)
                        if (pet.type == CAT)
                            femaleList.add(pet.name + "\n")
                }
            }
        }
        displayData()
    }

    // function to display data on the screen
    private fun displayData() {
        maleHeaderTv?.text = resources.getString(R.string.male)
        femaleHeaderTv?.text = resources.getString(R.string.female)

        //Method to sort the list
        maleList.sort()
        femaleList.sort()

        // loops to display data according to the gender
        for (male in maleList) {
            maleListTv?.text = String.format("%s \n %s", maleListTv?.text as String, male)
        }
        for (female in femaleList) {
            femaleListTv?.text = String.format("%s \n %s", femaleListTv?.text as String, female)
        }
    }


    // function to clear dispose
    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()
    }
}


