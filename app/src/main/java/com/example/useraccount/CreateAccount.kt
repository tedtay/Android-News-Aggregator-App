package com.example.useraccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.account.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.user_pref.*

/**
 * @author Ted Taylor 976335
 * class to handle creating user account and logging in
 */
class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * initializes firebase and the toolbar and listens for button clicks
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)
        val mToolbar = (findViewById<Toolbar>(R.id.createAccountToolbar))
        setSupportActionBar(mToolbar)
        supportActionBar?.title="Create Account/ Login"
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        createAccountButton.setOnClickListener {
            createAccount()
        }
        loginButton.setOnClickListener {
            login()
        }
    }

    /**
     * login function used to validate text entry and authenticate the
     * details with firebase
     */
    private fun login() {
        if (emailEnter.text.toString().isEmpty()) {
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter an email", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEnter.text.toString()).matches()) {
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter a valid email", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        if (passwordEnter.text.toString().isEmpty()) {
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter a password", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        //authenticated user email and password, if sucsessful current user updated and UI updated
        auth.signInWithEmailAndPassword(emailEnter.text.toString(), passwordEnter.text.toString()).addOnCompleteListener(this) {
                task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateInterface(user)
                } else {
                    val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Authentication Failed", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    updateInterface(null)
                }
        }
    }

    /**
     * validates text entries and creates a new account using firebase
     */
    private fun createAccount() {
        if (emailEnter.text.toString().isEmpty()) {
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter an email", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEnter.text.toString()).matches()) {
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter a valid email", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        if (passwordEnter.text.toString().isEmpty()) {
            //passwordEnter.error = "Enter a password"

            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Enter a password", Snackbar.LENGTH_LONG)
            snackbar.show()
            return
        }
        auth.createUserWithEmailAndPassword(emailEnter.text.toString(),passwordEnter.text.toString()).addOnCompleteListener(this){
            task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created, Please Now Login", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this,CreateAccount::class.java))
                finish()
            }
            else{
                val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Account Process Failed. Make sure password length > 5 ", Snackbar.LENGTH_LONG)
                snackbar.show()

            }
        }
    }

    public override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
    }

    /**
     * @param currentUser to specify the Firebase User
     * if the user logs in successful the process is finished and respective message displayed
     */
    private fun updateInterface(currentUser : FirebaseUser?){
        if(currentUser != null){
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Account::class.java))
            finish()
        }
        else{
            val snackbar = Snackbar.make(findViewById(R.id.createAccountLayout), "Login error", Snackbar.LENGTH_LONG)
            snackbar.show()

        }
    }

    /**
     * onCreate menu options to inflate toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout),menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * handles toolbar icon actions, when specific icon pressed the
     * relevant intent is started
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.account -> {
                val intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
            R.id.topPicks ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.stocks ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}