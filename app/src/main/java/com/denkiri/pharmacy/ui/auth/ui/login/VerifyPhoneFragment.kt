package com.denkiri.pharmacy.ui.auth.ui.login

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denkiri.pharmacy.R
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.auth_activity.*
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import java.util.concurrent.TimeUnit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyPhoneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyPhoneFragment : Fragment() {
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    var phone: String? = ""
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

}

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fragment = ChangePasswordFragment()
                        requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commitNow()
                    } else {
                        Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun sendVerificationCode(number: String?) {
        avi.visibility = View.VISIBLE
        val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private val mCallBack: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                editTextCode.setText(code)
                verifyCode(code)
                avi.visibility=View.INVISIBLE
               // activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            avi.visibility=View.INVISIBLE
          //  activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyPhoneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyPhoneFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private fun backToAuth(){
        val fragment = ForgotPasswordFragment()
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    //    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        back.setOnClickListener { backToAuth() }
        mAuth = FirebaseAuth.getInstance()
        val bundle = arguments
        phone = "+"+(bundle?.getString("mobile"))
        sendVerificationCode(phone)
        signin .setOnClickListener {
            val code = editTextCode!!.text.toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                editTextCode!!.error = "Enter code..."
                editTextCode!!.requestFocus()

            }
            verifyCode(code)
        }
        resendOtp.setOnClickListener {
            sendVerificationCode(phone)
        }

}
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_verify_phone, container, false)


        return rootView
    }
}



