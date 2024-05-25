package com.example.practica2_pdm.ui.questions

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica2_pdm.data.DataProvider
import com.example.practica2_pdm.data.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionsViewModel {

    private val dataProvider = DataProvider()
    val listaQuestion: MutableState<List<Question>> = mutableStateOf(emptyList())

    fun loadData(context: Context){
        dataProvider.loadData(context, "kotlin_questions.json")
        listaQuestion.value = dataProvider.Questions.shuffled()
    }
}