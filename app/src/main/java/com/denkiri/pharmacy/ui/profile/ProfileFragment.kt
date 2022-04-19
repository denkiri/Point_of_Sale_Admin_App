package com.denkiri.pharmacy.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.denkiri.pharmacy.MainActivity
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.ui.accounts.AccountsFragment
import com.denkiri.pharmacy.utils.Validator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.profile_fragment.*
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    var userId:String?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setUpUi()
        back.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountsFragment())
                .commitNow()  }
        edit.setOnClickListener { updateProfile() }
        viewModel.observeUserAction().observe(viewLifecycleOwner, { data ->
            run {
                setStatus(data)
                if (data.status == Status.SUCCESS && data.data != null) {

                    viewModel.saveProfile(data.data)
                    requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, AccountsFragment())
                            .commitNow()
                }
            }
        })

    }

fun setUpUi() {

    viewModel.getOuthProfile().observe(viewLifecycleOwner, {
        try {
            name.setText(it.name)
            email.setText(it.email)
            username.setText(it.username)
            mobile.setText(it.mobile)
            userId= it.id.toString()

        } catch (e: Exception) {
        }
    })
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
    private fun validate(): Boolean {
        if (!Validator.isValidName(name)) {
            return false
        }
        if (!Validator.isValidName(username)) {
            return false
        }

        if (!Validator.isValidEmail(email)) {
            return false
        }
        if (!Validator.isValidPhone(mobile)) {
            return false
        }

        return true
    }
    fun  updateProfile(){
        if (validate()) {
            viewModel .updateProfile(username.text.toString(),name.text.toString(),email.text.toString(),mobile.text.toString(),userId.toString())
        }

    }


}