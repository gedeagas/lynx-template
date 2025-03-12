package com.helloworld.utils

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.helloworld.MainActivity
import android.widget.EditText
import android.content.Context.MODE_PRIVATE
import android.widget.LinearLayout
import android.util.TypedValue

/**
 * Handles developer menu functionality.
 * @param activity The main activity.
 */
class DevMenuHandler(private val activity: Activity) {

    /**
     * Listens for physical key press events.
     * @param keyCode Key code
     * @param event KeyEvent
     * @return Boolean
     */
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_MENU) {
            showLynxDevMenu()
            true
        } else {
            activity.onKeyDown(keyCode, event)
        }
    }

    /**
     * Shows the Lynx Dev Menu.
     */
    private fun showLynxDevMenu() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Lynx Dev Menu")
            .setItems(arrayOf("Reload", "Change Bundle Location", "Enable Fast Refresh", "Settings")) { _, which ->
                when (which) {
                    0 -> {
                        (activity as? MainActivity)?.reloadLynxView()
                    }
                    1 -> {
                        showChangeBundleLocationDialog()
                    }
                    2 -> {
                        val intent = Intent(activity, SwitchActivity::class.java)
                        activity.startActivity(intent)
                    }
                }
            }
        builder.show()
    }

    /**
     * Shows the dialog to change the bundle location.
     */
    private fun showChangeBundleLocationDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Change Bundle URL")

        val input = EditText(activity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val marginDp = 16
        val marginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, marginDp.toFloat(), activity.resources.displayMetrics
        ).toInt()
        lp.setMargins(marginPx, marginPx, marginPx, marginPx)
        input.layoutParams = lp
        builder.setView(input)

        val sharedPref = activity.getPreferences(MODE_PRIVATE)
        val currentUrl = sharedPref.getString("lynxDebugUrl", "")
        input.setText(currentUrl)

        builder.setPositiveButton("OK") { _, _ ->
            val newUrl = input.text.toString()
            val sharedPrefEditor = activity.getPreferences(MODE_PRIVATE).edit()
            sharedPrefEditor.putString("lynxDebugUrl", newUrl)
            sharedPrefEditor.apply()
            (activity as? MainActivity)?.reloadLynxView()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}