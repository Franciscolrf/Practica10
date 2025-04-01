package mx.edu.itson.practica10

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        this.auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val errorTV: TextView = findViewById(R.id.tvError)
        val button: Button = findViewById(R.id.btnLogin)
        val buttonRegister: Button = findViewById(R.id.btnGoRegister)

        errorTV.visibility = View.INVISIBLE

        button.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty()) {
                errorTV.text = "Todos los campos deben de ser llenados"
                errorTV.visibility = View.VISIBLE
            } else {
                errorTV.visibility = View.INVISIBLE
                login(email.text.toString(), password.text.toString())
            }
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError(text = "Usuario y/o contraseña equivocados", visible = true)
                }
            }
    }

    fun goToMain(user: FirebaseUser) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun showError(text: String = "", visible: Boolean) {
        val errorTV: TextView = findViewById(R.id.tvError)
        errorTV.text = text
        errorTV.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}

