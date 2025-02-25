    package com.example.calculator

    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat

    class MainActivity : AppCompatActivity() {
        private lateinit var vivod: TextView
        private var currentExpression: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            vivod = findViewById(R.id.vivod)

            val numberButtons = listOf(
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
            )

            numberButtons.forEach { id ->
                findViewById<Button>(id).setOnClickListener {
                    appendToExpression((it as Button).text.toString())
                }
            }

            findViewById<Button>(R.id.buttonPoint).setOnClickListener { appendToExpression(".") }
            findViewById<Button>(R.id.buttonPl).setOnClickListener { appendToExpression("+") }
            findViewById<Button>(R.id.buttonMn).setOnClickListener { appendToExpression("-") }
            findViewById<Button>(R.id.buttonMp).setOnClickListener { appendToExpression("*") }
            findViewById<Button>(R.id.buttonDv).setOnClickListener { appendToExpression("/") }
            findViewById<Button>(R.id.buttonEq).setOnClickListener { calculateResult() }
            findViewById<Button>(R.id.buttonC).setOnClickListener { clearAll() }
        }

        private fun appendToExpression(value: String) {
            currentExpression += value
            vivod.text = currentExpression
        }

        private fun formatResult(result: Double): String {
            return if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        }

        private fun calculateResult() {
            try {
                val result = evalSimpleExpression(currentExpression)
                vivod.text = formatResult(result)  // Используем форматирование
                currentExpression = formatResult(result)
            } catch (e: Exception) {
                vivod.text = "Ошибка"
                currentExpression = ""
            }
        }


        private fun evalSimpleExpression(expression: String): Double {
            val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex()).map { it.trim() }
            if (tokens.isEmpty()) throw IllegalArgumentException("Пустое выражение")

            var result = tokens[0].toDoubleOrNull() ?: throw IllegalArgumentException("Неверный формат")

            var i = 1
            while (i < tokens.size) {
                val operator = tokens[i]
                val nextNumber = tokens.getOrNull(i + 1)?.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Ожидалось число после оператора $operator")

                result = when (operator) {
                    "+" -> result + nextNumber
                    "-" -> result - nextNumber
                    "*" -> result * nextNumber
                    "/" -> if (nextNumber != 0.0) result / nextNumber else throw ArithmeticException("Деление на ноль")
                    else -> throw IllegalArgumentException("Неизвестный оператор $operator")
                }
                i += 2
            }
            return result
        }

        private fun clearAll() {
            currentExpression = ""
            vivod.text = ""
        }
    }
