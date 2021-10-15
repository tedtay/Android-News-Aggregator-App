import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.Notification
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import com.example.useraccount.R

/**
 * @author Ted Taylor 976335
 * Class to handle notifications for when the users top picks have been refreshed
 */
internal class NotificationHelper(base: Context): ContextWrapper(base){
    private var notifManager: NotificationManager?=null
    private val manager: NotificationManager?get(){
        notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notifManager
    }
    init{
        createChannels()
    }

    /**
     * method to create new channel for notification to be displayed on
     */
    fun createChannels(){
        val notificationChannel =
            NotificationChannel("1", "1", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager!!.createNotificationChannel(notificationChannel)
    }

    /**
     * @param title title of the notification
     * @param body body text of the notification
     * @return Builder
     * method to create the notification using Builder
     */
    fun getNotification1(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, "1")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.subscribe_to_editor_icon)
            .setNumber(3)
            .setAutoCancel(true)
    }

    /**
     * @param id notification id
     * @param notification the notification object
     * method to display the notification created
     */
    fun notify(id: Int, notification: NotificationCompat.Builder) {
        manager!!.notify(id, notification.build())
    }


}