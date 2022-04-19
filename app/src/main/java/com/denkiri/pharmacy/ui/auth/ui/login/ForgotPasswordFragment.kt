package com.denkiri.pharmacy.ui.auth.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.ui.products.EditProduct
import com.denkiri.pharmacy.utils.Validator
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : Fragment() {
    lateinit var intent: Intent
    // TODO: Rename and change types of parameters
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
        val rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        rootView.next.setOnClickListener {
            if (context?.let { it1 -> NetworkUtils.isConnected(it1) }!! && validate()){
               // comm.passDataCom(rootView.mobile.text.toString())
            }


            else{
                Toast.makeText(context,getString(R.string.no_connection), Toast.LENGTH_SHORT).show()

            }
            //  comm.passDataCom(rootView.mobile.text.toString())
            // intent = Intent(context, VerifyPhoneActivity::class.java)
            //  intent.putExtra("mobile", rootView.mobile.text.toString())
            //  startActivity(intent)

        }
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
    private fun validate(): Boolean {
        if (!Validator.isValidPhone(mobile)) {
            return false
        }
        return true
    }
    private fun backToAuth(){
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container,LoginFragment()).addToBackStack("registerFragment").commit()

    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        back.setOnClickListener {(backToAuth())}
        next.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("mobile", mobile.text.toString())
            val fragment = VerifyPhoneFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
        }


    }

}