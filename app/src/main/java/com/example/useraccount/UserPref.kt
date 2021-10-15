package com.example.useraccount

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_pref.*

private lateinit var auth: FirebaseAuth

/**
 * @author Ted Taylor 9976335
 */
class UserPref : AppCompatActivity() {
    /**
     * on create used to initialize toolbar, grab current Firebase User
     * and load user preference CheckBox Data and listen for clicks
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_pref)
        val mToolbar = (findViewById<Toolbar>(R.id.account_toolbar))
        setSupportActionBar(mToolbar)
        supportActionBar?.title="Interests"

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        /**
         * ID's I am using to uniquely identify each checkbox
         * -------------------------------
         * checkbox_GENERAL = 1
         * checkbox_SPORT = 2
         * checkbox_BUISNESS = 3
         * checkbox_ENTERTAINMENT = 4
         * checkbox_HEALTH = 5
         * checkbox_SCIENCE = 6
         * checkbox_TECHNOLOGY = 7
         * -------------------------------
         */

        //load data from (checkbox and assign it an ID)
        loadCheckboxData(checkbox_GENERAL,1)
        loadCheckboxData(checkbox_SPORTS,2)
        loadCheckboxData(checkbox_BUSINESS,3)
        loadCheckboxData(checkbox_ENTERTAINMENT,4)
        loadCheckboxData(checkbox_HEALTH,5)
        loadCheckboxData(checkbox_SCIENCE,6)
        loadCheckboxData(checkbox_TECHNOLOGY,7)

        //when checkbox is clicked saveData(checkbox and ID)
        checkbox_GENERAL.setOnClickListener{
            saveCheckboxData(checkbox_GENERAL,1)
        }
        checkbox_SPORTS.setOnClickListener{
            saveCheckboxData(checkbox_SPORTS,2)
        }
        checkbox_BUSINESS.setOnClickListener{
            saveCheckboxData(checkbox_BUSINESS,3)
        }
        checkbox_ENTERTAINMENT.setOnClickListener{
            saveCheckboxData(checkbox_ENTERTAINMENT,4)
        }
        checkbox_HEALTH.setOnClickListener{
            saveCheckboxData(checkbox_HEALTH,5)
        }
        checkbox_SCIENCE.setOnClickListener{
            saveCheckboxData(checkbox_SCIENCE,6)
        }
        checkbox_TECHNOLOGY.setOnClickListener{
            saveCheckboxData(checkbox_TECHNOLOGY,7)
        }

    }

    /**
     * @param int used to specify the checkbox ID
     * @param checkBox used to pass checkbox object state
     * Takes in (checkbox and an ID) creates a sharedPreference ID for the data using uid+ID
     *assign current checkbox boolean state to the a user by using their firebaseID
     *
     * This method uses shared preferences to store user data, i saw this as more appropriate as
     * using an sql database for storing user info is rather overkill method and this
     * i devised by the userID and checkbox ID to set the shared preferences is a clever, light weight
     * method of storing boolean values for the users interests
     */
    private fun saveCheckboxData(checkBox: CheckBox,int : Int){
        val isChecked = checkBox.isChecked
        val sharedPreferences = getSharedPreferences((auth.currentUser?.uid)+int, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putBoolean("BOOLEAN_KEY",checkBox.isChecked)
        }.apply()
        val snackbar = Snackbar.make(findViewById(R.id.userPrefLayout), "Interest Saved", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    /**
     * @param int used to specify the checkbox ID
     * @param checkBox used to pass checkbox object state
     */
    private fun loadCheckboxData(checkBox : CheckBox, int : Int){
        val sharedPreferences = getSharedPreferences((auth.currentUser?.uid)+int, Context.MODE_PRIVATE)
        val savedBool = sharedPreferences.getBoolean("BOOLEAN_KEY",false)
        checkBox.isChecked = savedBool
    }

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
            R.id.account -> {
                val intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}