import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import javax.swing.JOptionPane

actual class NotificationManagerImpl: NotificationManager {
    actual override fun showNotification(title: String, message: String) {
        if (SystemTray.isSupported()) {
            val tray = SystemTray.getSystemTray()
            val image: Image = Toolkit.getDefaultToolkit().createImage("my_activity_logo.png")
            val trayIcon = TrayIcon(image, "Reminder")
            trayIcon.isImageAutoSize = true
            trayIcon.toolTip = "Reminder Notification"
            tray.add(trayIcon)
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.NONE)
        }else{
            JOptionPane.showMessageDialog(
                null,
                message,
                "Desktop Notification",
                JOptionPane.INFORMATION_MESSAGE
            )
        }


    }
}