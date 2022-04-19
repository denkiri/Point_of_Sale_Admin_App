package com.denkiri.pharmacy

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.denkiri.pharmacy.utils.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    private fun showMessage(isConnected: Boolean) {



        if (!isConnected) {

            val messageToUser = "Oops! Your Internet is off" //TODO

           mSnackBar = Snackbar.make(findViewById(R.id.layout), messageToUser, Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
          //  mSnackBar!!.setAction("TURN ON") {
             //   val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
             //   startActivity(intent)

          //  }
            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
           // Toasty.success(this,"Swipe Down to Refresh", Toast.LENGTH_SHORT,false).show()
        }


    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this



    }

    override fun onPause() {
        super.onPause()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }



}