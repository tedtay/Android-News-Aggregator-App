package com.example.useraccount
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Ted taylor 976335
 */
class RecyclerView: AppCompatActivity() {
    /**
     * initializes the recycler items
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)
    }
}