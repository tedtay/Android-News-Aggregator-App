package com.example.useraccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.account.*
import kotlinx.android.synthetic.main.create_account.*

/**
 * @author Ted Taylor 976335
 */
class Account : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    /**
     * initializes firebase and the toolbar
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account)
        val mToolbar = (findViewById<Toolbar>(R.id.accountToolbar))
        setSupportActionBar(mToolbar)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser?.email.toString()
        supportActionBar?.title="Your Account"
        //supportActionBar?.title="$currentUser"
    }

    /**
     * inflates toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout),menu)
        return super.onCreateOptionsMenu(menu)
    }

    public override fun onStart(){
        super.onStart()
        //change the current user logged in textview
        setAccountText("Current User: "+auth.currentUser?.email.toString())

    }

    /**
     * Changes current user text when page is resumed so that when a new user logs in the
     * text is also changed
     */
    public override fun onResume() {
        super.onResume()

        //change the current user logged in textview
        setAccountText("Current User: "+auth.currentUser?.email.toString())
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

    /**
     * @param view to start account page
     * functions to open activities when respective buttons clicked
     */
    fun accountPageCreateAccountButtonClick(view: View){
        val intent = Intent(this, CreateAccount::class.java)
        startActivity(intent)

    }
    /**
     * @param view to start edit interests page
     * functions to open activities when respective buttons clicked
     */
    fun accountPageUserPrefButtonClick(view: View){
        val intent = Intent(this, UserPref::class.java)
        startActivity(intent)

    }


    /**
     * @param user for current user name
     * Changes the current the account text
     */
    private fun setAccountText(user :String){
        val textView = findViewById<TextView>(R.id.text_view_id)
        textView.setText(user).toString()
        Log.i("Chaning Text", auth.currentUser.toString())
    }
}