package com.example.calculator

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // Declaração dos campos de entrada e resultado
    private lateinit var editText: EditText
    private lateinit var resultTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa os campos de entrada
        editText = findViewById(R.id.editText)
        resultTextView = findViewById(R.id.resultTextView)

        // Configura o listener para atualizar o resultado em tempo real
        setupInputChangeListener()

        // Lista de IDs dos botões
        val buttonIds = arrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonDot, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply,
            R.id.buttonDivide, R.id.buttonBr1, R.id.buttonBr2, R.id.backspace,
            R.id.AC, R.id.buttonEquals
        )

        // Configuração dos botões
        for (buttonId in buttonIds) {
            val button = findViewById<Button>(buttonId)

            // Define cores diferentes para botões numéricos
            if (buttonId in arrayOf(R.id.buttonDot, R.id.button0, R.id.button1, R.id.button2, 
                                    R.id.button3, R.id.button4, R.id.button5, R.id.button6, 
                                    R.id.button7, R.id.button8, R.id.button9)) {
                val color = ContextCompat.getColor(this, R.color.navi)
                val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_button)
                drawable?.setColorFilter(color, PorterDuff.Mode.SRC)
                button.background = drawable
            }

            // Define cores para botões de operação (+, -, *, /, =)
            if (buttonId in arrayOf(R.id.buttonEquals, R.id.buttonAdd, R.id.buttonSubtract, 
                                    R.id.buttonMultiply, R.id.buttonDivide)) {
                val color = ContextCompat.getColor(this, R.color.yellow)
                val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_button)
                drawable?.setColorFilter(color, PorterDuff.Mode.SRC)
                button.background = drawable
            }

            // Define cores para os botões de limpeza (AC, C)
            if (buttonId in arrayOf(R.id.backspace, R.id.AC)) {
                val color = ContextCompat.getColor(this, R.color.red)
                val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_button)
                drawable?.setColorFilter(color, PorterDuff.Mode.SRC)
                button.background = drawable
            }

            // Define ação de clique para cada botão
            button.setOnClickListener { onButtonClick(it) }
        }

        // Configura o botão de igual para processar a equação
        findViewById<Button>(R.id.buttonEquals).setOnClickListener { onEqualsButtonClick() }
    }

    // Configura um listener para atualizar o resultado conforme o usuário digita
    private fun setupInputChangeListener() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val expression = s.toString()
                try {
                    val result = evaluateExpression(expression)
                    resultTextView.setText(result.toString())
                } catch (e: Exception) {
                    // Se a expressão for inválida, limpa o resultado
                    resultTextView.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Manipula os cliques dos botões
    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        val currentText = editText.text.toString()

        when (buttonText) {
            "AC" -> editText.setText("") // Limpa tudo
            "C" -> {
                if (currentText.isNotEmpty()) {
                    editText.setText(currentText.dropLast(1)) // Apaga o último caractere
                }
            }
            else -> editText.setText(currentText + buttonText) // Adiciona o número ou operador
        }
    }

    // Processa a equação quando o botão "=" for pressionado
    private fun onEqualsButtonClick() {
        val currentText = editText.text.toString()
        try {
            val result = evaluateExpression(currentText)
            val resultInt = result.toInt() // Converte o resultado para inteiro
            resultTextView.text = Editable.Factory.getInstance().newEditable(resultInt.toString())
        } catch (e: Exception) {
            editText.setText("Error: ${e.message}") // Exibe erro em caso de falha
        }
    }

    // Avalia a expressão matemática digitada
    private fun evaluateExpression(expression: String): Int {
        return ExpressionBuilder(expression).build().evaluate().toInt()
    }
}
