package com.denkiri.pharmacy.ui.auth.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.MainActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.utils.Validator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_change_password.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }
    private fun validate(): Boolean {
        if (!Validator.isValidEmail(email)) {
            return false
        }
        if (!Validator.isValidPassword(password)) {
            return false
        }
        if (!Validator.isValidPassword(confirmPassword)) {
            return false
        }
        if (password.text.toString()!= confirmPassword.text.toString()){
            Toast.makeText(context, "Password do not match", Toast.LENGTH_LONG).show()
            return false
        }
        if( TextUtils.isEmpty(password.text) ){
            return false
        }
        if( TextUtils.isEmpty(confirmPassword.text) ){
            return false
        }
        return true
    }
    private fun setStatus(data: Resource<Oauth>) {
        val status: Status = data.status
        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if (status == Status.ERROR) {
            if (data.message != null) {
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }
        }
    }
    private fun getParameters(): Oauth {

        return Oauth(Profile(email.text.toString(), password.text.toString()))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        back.setOnClickListener {(backToAuth())}
        signIn.setOnClickListener { signIn() }
        viewModel.observeChangePassword().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
                if (data.status == Status.SUCCESS && data.data != null) {

                    viewModel.saveProfile(data.data)
                    val fragment = LoginFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commitNow()
                }
            }
        })
    }
    private fun backToAuth(){
        val fragment = ForgotPasswordFragment()
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()

    }
    fun signIn(){
        if (validate()) {
            viewModel.changePassword(getParameters())
        }


    }

}