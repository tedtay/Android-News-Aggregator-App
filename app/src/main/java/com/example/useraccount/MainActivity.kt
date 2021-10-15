package com.example.useraccount

import NotificationHelper
import android.R.attr.name
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.internal.notify
import java.io.IOException
import kotlin.random.Random


/**
 * @author Ted Taylor 976335
 */
class MainActivity : AppCompatActivity() {

    /**
     * set my global variables to alter news information arrays
     * as well as som additional variables needed including ...
     *  - jsonText for altering json request
     *  - toolbarTitle to change current toolbar title
     *  - userInterests to alter current user interests
     *  - previousTopPicks for refreshing user top picks
     *  - currentTopPick for generating new top picks
     *  - handler & handler to handle top picks refreshing every so often
     */
    companion object {
        private lateinit var auth: FirebaseAuth
        var loadCount =0
        var jsonText = "http://newsapi.org/v2/top-headlines?country=gb&pagesize=20&apiKey=0003a06c5bbc4d0c9cae5a94cc7572f6"
        var toolbarTitle = "Top Stories"
        val userInterests = arrayListOf<String>()
        var previousTopPicks=0
        var currentTopPick = Random.nextInt()
        var handler: Handler = Handler()
        var runnable: Runnable = Runnable {  }

        val headlineArray = arrayListOf<String>()
        val editorArray = arrayListOf<String>()
        val sourceArray = arrayListOf<String>()
        val dateArray = arrayListOf<String>()
        val urlArray = arrayListOf<String>()
        val urlToImageArray = arrayListOf<String>()
        val contentArray = arrayListOf<String>()
        val descriptionArray = arrayListOf<String>()

        val headlineInfo : MutableList<String> = headlineArray
        val editorInfo : MutableList<String> = editorArray
        val sourceInfo : MutableList<String> = sourceArray
        val dateInfo : MutableList<String> = dateArray
        val urlInfo : MutableList<String> = urlArray
        val urlToImageInfo : MutableList<String> = urlToImageArray
        val descriptionInfo : MutableList<String> = descriptionArray
    }

    /**
     * onCreate() initializes the toolbar, then the onResume()
     * operation calls reloadJson()
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = (findViewById<Toolbar>(R.id.main_toolbar))
        setSupportActionBar(mToolbar)
        supportActionBar?.title=getToolbarTitle()
        reloadJson()
        println("load count== "+ loadCount)
        if(loadCount ==0){
            randomTopPicks()
        }
        loadCount++
        search_button.setOnClickListener {
            setJsonText("http://newsapi.org/v2/everything?q=${search.text}&from=2020-11-16&to=2020-11-16&sortBy=popularity&apiKey=0003a06c5bbc4d0c9cae5a94cc7572f6")
            Thread.sleep(1000)
            reloadJson()
            Thread.sleep(1000)
            reloadJson()


        }
        Log.v("6"," ---------------------------onCreate()")
        println(" ---------------------------onCreate()")
    }


    /**
     * function to generate Top Stories,
     * sets json query to pull top stories from UK
     */
    fun generateTopStories(){

        setJsonText("http://newsapi.org/v2/top-headlines?country=gb&apiKey=0003a06c5bbc4d0c9cae5a94cc7572f6")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        clearData(true)
        startActivity(intent)
        setToolbarTitle("Top Stories")
    }

    /**
     * function to generate stocks,
     * sets json query to 'stocks' and reloads home page
     */
    fun generateStocks(){
        setJsonText("http://newsapi.org/v2/everything?q=stocks&from=2020-11-16&to=2020-11-16&sortBy=popularity&apiKey=0003a06c5bbc4d0c9cae5a94cc7572f6")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        println("generating stocks")
        setToolbarTitle("Stocks")
    }

    /**
     * function to generate top picks by grabbing top picks data from
     * the user's shared preferences, then setting the json text category
     * equal to a random one of these values, random value changes every 5 mins
     */
    fun generateTopPicks(){
        try{
            reloadUserInterests()
            setJsonText("")
            println("Categories === "+userInterests + "Random element "+ (currentTopPick))
            setToolbarTitle("Top Picks")
            setJsonText("http://newsapi.org/v2/top-headlines?country=us&pagesize=3&category=${userInterests.elementAt(
                currentTopPick)}&apiKey=0003a06c5bbc4d0c9cae5a94cc7572f6")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            println("generating top pcisk")
            userInterests.clear()
        }
        catch (ex: Exception){
            println("exception in top picks... $ex")
            val snackbar = Snackbar.make(findViewById(R.id.activityMainLayout), "Wait For Top Picks To Refresh", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    fun reloadUserInterests(){
        try{
            userInterests.clear()
            for(categoryID in 1..7){

                auth = FirebaseAuth.getInstance()

                val sharedPreferences = getSharedPreferences((auth.currentUser?.uid)+categoryID, Context.MODE_PRIVATE)
                val currentCategory = sharedPreferences.getBoolean("BOOLEAN_KEY",false)
                println("Category $categoryID: $currentCategory")

                if (currentCategory) {
                    if (categoryID == 1) {
                        userInterests.add("general")
                    }
                    if (categoryID == 2) {
                        userInterests.add("sports")
                    }
                    if (categoryID == 3) {
                        userInterests.add("business")
                    }
                    if (categoryID == 4) {
                        userInterests.add("entertainment")
                    }
                    if (categoryID == 5) {
                        userInterests.add("health")
                    }
                    if (categoryID == 6) {
                        userInterests.add("science")
                    }
                    if (categoryID == 7) {
                        userInterests.add("technology")
                    }
                }
            }
        }catch (ex: StackOverflowError){
            println(ex.stackTrace)
            val snackbar = Snackbar.make(findViewById(R.id.activityMainLayout), "Wait For Top Picks To Refresh", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    /**
     * Method to pick a random new interest which isn't the
     * current interest selected
     */
    fun randomTopPicks(){
        try{
            previousTopPicks= currentTopPick
            reloadUserInterests()
            println("to ppicks size= "+ userInterests.size)
            if(userInterests.size != 0){
                currentTopPick = Random.nextInt(0, userInterests.size)
                if(currentTopPick== previousTopPicks ){
                    randomTopPicks()
                }
                topPicksNotification()
                Toast.makeText(this, "Your Top Picks Have Refreshed", Toast.LENGTH_SHORT).show()
                println("rand val = "+ currentTopPick)
            }
        }
        catch(ex: StackOverflowError){
            println(ex.stackTrace)
            val snackbar = Snackbar.make(findViewById(R.id.activityMainLayout), "Wait For Top Picks To Refresh", Snackbar.LENGTH_LONG)
            snackbar.show()

        }

    }

    /**
     * reloads json data and calls updateNewsFeed using newly populated
     * mutable lists in order to populate recycler view
     */
    fun reloadJson(){
        pullJSON(true,getJsonText())
        updateNewsfeed(headlineInfo,editorInfo,sourceInfo,dateInfo, urlToImageInfo, descriptionInfo,urlInfo)
        println("reloaded")
    }

    /**
     * @Param clearAll to specify if all array data should be cleared
     * @param string to specify the json to pull data from
     * class to pull json data and split the data collected into
     * some code adapted from www.youtube.com/watch?v=53BsyxwSBJk
     */
    fun pullJSON(clearAll: Boolean, string:String) {
        var jsonText = string

        val newRequest = Request.Builder().url(jsonText).build()
        val call = OkHttpClient()
        call.newCall(newRequest).enqueue(object: Callback{//requests for json data

            override fun onResponse(call: Call, answer: Response) {//if response received set to a string and 'feed' variable handles each piece of data
                val response = answer?.body?.string()//raw json data
                //println(response.toString()) //shows raw json data
                val gsonBuilder = GsonBuilder().create()
                val feed =  gsonBuilder.fromJson(response,NewsFeed:: class.java)
                compileData(clearAll,feed)
                Thread.sleep(100)
               // println("article ==  "+feed.getArticle())
                //passes feed we have processed onto handleFeed() to change into arraylists
            }
            override fun onFailure(call: Call, ex: IOException){
                Log.i(ex.toString(), "IO Exception occurred")
            }
        })
    }

    /**
     * @param clearAll to specify if all array data should be cleared
     * @param feed to specify the current data storing object
     * method to clear the existing data and compile new data from the json feed into mutable arrays
     */
    fun compileData(clearAll: Boolean, feed: NewsFeed){

        println("newsfeed")
        clearData(clearAll)
        var y = 0
        try{

            println("inside try...")
            for (i in feed.getArticle()) {
                println("inside for")
                headlineArray.add(feed.getArticle()[y].title)
                dateArray.add(feed.getArticle()[y].publishedAt)
                sourceArray.add(feed.getArticle()[y].source.name)
                editorArray.add(feed.getArticle()[y].author)
                urlArray.add(feed.getArticle()[y].url)
                urlToImageArray.add(feed.getArticle()[y].urlToImage)
                contentArray.add(feed.getArticle()[y].content)
                descriptionArray.add(feed.getArticle()[y].content)
                y = y + 1
            }
            y=0
        }catch(ex: NullPointerException){
            println("$ex ...in compiling data" )
        }
    }

    /**
     * clears the existing json data stored in arrays
     * @param clearAll to specify if all array data should be cleared
     */
    fun clearData(clearAll: Boolean){
        if(clearAll){
            headlineArray.clear()
            editorArray.clear()
            sourceArray.clear()
            dateInfo.clear()
            urlToImageArray.clear()
            descriptionArray.clear()
            contentArray.clear()
            urlArray.clear()
            println("cleared all data ....")
        }

        headlineInfo.clear()
        editorInfo.clear()
        sourceInfo.clear()
        dateInfo.clear()
        urlToImageInfo.clear()
        descriptionInfo.clear()
        urlInfo.clear()

        var t = 0
        println("Info Cleared data ......")
        for(i in headlineInfo){
            println(headlineInfo[t])
            t=t+1
        }
        t=0
    }

    /**
     * used to update the recyclerview items with new data
     * @param MutableList x 7 to specify the lists where the
     * data should be stored
     */
    fun updateNewsfeed(headlineInfo: MutableList<String>, editorList: MutableList<String>,sourceList: MutableList<String>,
                       dateList: MutableList<String>, urlToImageList: MutableList<String>,descriptionList: MutableList<String>,urlList: MutableList<String> ){
        println("updating news feed")
        val mAdapter = RecyclerAdapter(headlineArray.size,headlineInfo,editorList,sourceList,dateList,urlToImageList,descriptionList,urlList,R.id.headline,R.id.editor,R.id.newsSource,R.id.date,R.id.thumbnail,R.id.description)
        val headlineView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        headlineView.layoutManager = linearLayoutManager
        headlineView.adapter = mAdapter
    }

    fun topPicksNotification(){

        val notificationHelper = NotificationHelper(this)
        var notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel("1","1",IMPORTANCE_HIGH)
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.subscribe_to_editor_icon)
            .setContentTitle("News App Updated")
            .setContentText("Your Top Picks Have Been Updated !")
            .setAutoCancel(true)
        notificationHelper!!.notify(1,builder)
    }


    /**
     * used to inflate toolbar
     * @param menu to inflate toolbar menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout),menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * refreshes json data when page is reloaded.
     * also used to refresh Top picks every 5mins, some code adapted
     * from https://stackoverflow.com/questions/11434056/how-to-run-a-method-every-x-seconds
     */
    override fun onResume() {
        super.onResume()
        println("resuming")
        Thread.sleep(1000)
        reloadJson()
        Log.v("5"," ---------------------------onResume()")
        println(" ---------------------------onResume()")
        handler.postDelayed(Runnable { randomTopPicks()
            handler.postDelayed( runnable,300000)
        }.also { runnable = it }, 300000)

    }

    override fun onStart(){
        Log.v("1"," ---------------------------onStart()")
        println(" ---------------------------onStart()")
        super.onStart()
    }
    override fun onDestroy() {
        Log.v("2"," ---------------------------onDestroy()")
        println(" ---------------------------onDestroy()")
        super.onDestroy()
    }
    override fun onStop(){
        Log.v("3"," ---------------------------onStop()")
        super.onStop()
    }
    override fun onPause(){
        Log.v("4"," ---------------------------onPause()")
        println(" ---------------------------onPause()")
        super.onPause()
    }


    /**
     * class to handle toolbar clicks
     * @param item to specify an item being pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.account -> {
                val intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
            R.id.home -> {
                generateTopStories()
            }
            R.id.topPicks ->{
                generateTopPicks()
            }
            R.id.stocks ->{
                generateStocks()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * getters and setters for json data text
     * and toolbar title
     */
    fun setJsonText(string: String){
        jsonText = string
    }
    fun getJsonText():String{
        return jsonText
    }
    fun setToolbarTitle(title: String){
        toolbarTitle = title

    }
    fun getToolbarTitle():String{
        return  toolbarTitle
    }
}



/**
 * Three classes to handle list of json 'articles', split the data into individual articles
 * and split the articles into sets of data.
 * @param articles to alter the article objects
 */
class NewsFeed(val articles: List<Article>){

    fun getLength(articles: List<Article>): Int {
        return articles.size
    }
    fun getArticle():List<Article>{
        return articles
    }
}
class Article(val source: Source, val author: String, val title: String, val description: String, val url: String, val urlToImage : String, val publishedAt : String, val content : String){
}
class Source(val id : String, val name : String){
}




