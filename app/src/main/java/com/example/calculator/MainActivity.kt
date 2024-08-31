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
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var resultTextView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.workingsTV)
        resultTextView = findViewById(R.id.resultTV)

        setupInputChangeListener()

        val buttonIds = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.dotBtn, R.id.addBtn, R.id.subtractBtn, R.id.multiplyBtn,
            R.id.divideBtn, R.id.parenthesisLeft, R.id.parenthesisRight, R.id.backspace,
            R.id.allClearBtn, R.id.equalsBtn
        )

        for (buttonId in buttonIds) {
            val button = findViewById<Button>(buttonId)
            if (buttonId in arrayOf(R.id.dotBtn, R.id.btn0,R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9)) {
                val color = ContextCompat.getColor(this, R.color.navi)
            }

            if (buttonId in arrayOf(R.id.equalsBtn, R.id.addBtn, R.id.subtractBtn, R.id.multiplyBtn, R.id.divideBtn)) {
                val color = ContextCompat.getColor(this, R.color.yellow)
            }

            if (buttonId in arrayOf(R.id.backspace, R.id.allClearBtn)) {
                val color = ContextCompat.getColor(this, R.color.red)
            }

            button.setOnClickListener { onButtonClick(it) }
        }

        findViewById<Button>(R.id.equalsBtn).setOnClickListener { onEqualsButtonClick() }
    }

    private fun setupInputChangeListener() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Calculate and display result while typing
                val expression = s.toString()
                try {
                    val result = evaluateExpression(expression)
                    resultTextView.setText(result.toString())
                } catch (e: Exception) {
                    // Handle invalid expressions
                    resultTextView.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        val currentText = editText.text.toString()

        when (buttonText) {
            "AC" -> editText.setText("")
            "C" -> {
                if (currentText.isNotEmpty()) {
                    editText.setText(currentText.dropLast(1))
                }
            }
            else -> editText.setText(currentText + buttonText)
        }
    }

    private fun onEqualsButtonClick() {
        val currentText = editText.text.toString()
        try {
            val result = evaluateExpression(currentText)
            val resultInt = result.toInt() // Convert result to integer
            resultTextView.text = Editable.Factory.getInstance().newEditable(resultInt.toString())
        } catch (e: Exception) {
            editText.setText("Error: ${e.message}")
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression).build().evaluate()
    }
}