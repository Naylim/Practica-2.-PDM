package com.example.practica2_pdm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.practica2_pdm.ui.questions.QuestionsViewModel
import kotlinx.coroutines.delay
private val questionsViewModel = QuestionsViewModel()
private const val MAX_QUESTIONS = 6 // Número máximo de preguntas

@Preview(showBackground = true)
@Composable
fun preguntas() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        questionsViewModel.loadData(context)
    }

    val questionsState by remember { questionsViewModel.listaQuestion } // lista de preguntas
    var currentQuestionIndex by remember { mutableStateOf(0) } //índice de la pregunta actual
    var isAnswered by remember { mutableStateOf(false) } //botones presionados
    var selectedAnswerIndex by remember { mutableStateOf(-1) }

    // Contadores de aciertos y errores
    var correctAnswers by remember { mutableStateOf(0) }
    var wrongAnswers by remember { mutableStateOf(0) }

    // Verifica que la lista de preguntas no esté vacía antes de mostrar la pregunta actual
    val currentQuestion = questionsState.getOrNull(currentQuestionIndex)

    // Restablece el estado cuando se cambia de pregunta
    LaunchedEffect(currentQuestionIndex) {
        isAnswered = false
        selectedAnswerIndex = -1
    }

    // Cambia a la siguiente pregunta automáticamente después de 2 segundos si una pregunta ha sido respondida
    LaunchedEffect(isAnswered) {
        if (isAnswered) {
            delay(2000) // Espera 2 segundos
            if (currentQuestionIndex + 1 < MAX_QUESTIONS && currentQuestionIndex + 1 < questionsState.size) {
                currentQuestionIndex++
            } else {
                currentQuestionIndex = MAX_QUESTIONS // Marca el final
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentQuestionIndex >= MAX_QUESTIONS) { //si es mayor o igual a 6(preguntas de ronda)
            Text("Fin de la ronda!", fontSize = 50.sp, lineHeight = 30.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Aciertos: $correctAnswers", fontSize = 30.sp)
            Text("Errores: $wrongAnswers", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Reinicia el juego
                    currentQuestionIndex = 0
                    correctAnswers = 0
                    wrongAnswers = 0
                    questionsViewModel.loadData(context) //vuelve a cargar para que se revuelvan de nuevo
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Reiniciar juego", fontSize = 18.sp, color = Color.White)
            }
        } else {

            Text("Pregunta ${currentQuestionIndex + 1}", fontSize = 40.sp, lineHeight = 30.sp)
            currentQuestion?.let {
                Text(
                    it.text, //muestra la pregunta actual
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(35.dp)
                )

                it.options.forEachIndexed { index, option ->
                    val buttonColor = when {
                        !isAnswered -> Color.Gray // Color inicial del botón
                        index == it.correctAnswerIndex -> Color.Green // Respuesta correcta
                        index == selectedAnswerIndex -> Color.Red // Respuesta incorrecta
                        else -> Color.Gray // Otros botones
                    }
                    Button(
                        onClick = {
                            if (!isAnswered) {
                                isAnswered = true
                                selectedAnswerIndex = index
                                if (index == it.correctAnswerIndex) {
                                    //sumatoria de aciertos o errores
                                    correctAnswers++
                                } else {
                                    wrongAnswers++
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(option, fontSize = 30.sp, lineHeight = 40.sp)
                    }
                }
            } ?: Text("No hay preguntas disponibles", modifier = Modifier.weight(2f))
        }
    }
}
