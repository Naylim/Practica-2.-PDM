package com.example.practica2_pdm.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class DataProvider {
    var Questions: List<Question> = emptyList()
        private set

    fun loadData(context: Context, fileName: String){
        val questionsJsonString = context.assets.open(fileName).bufferedReader().use{
            it.readText()
        }
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(Array<Question>::class.java)
        //adapta la info
        Questions = adapter.fromJson(questionsJsonString)?.toList() ?: emptyList()
    }
}