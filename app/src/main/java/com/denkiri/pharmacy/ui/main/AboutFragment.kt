package com.denkiri.pharmacy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.ui.supplier.SupplierFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.main_fragment.*


class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        getAppVersion()
        privacyPolicy.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, PrivacyFragment())
                .commitNow() }
        terms.setOnClickListener {  requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, TermsFragment())
                .commitNow() }
    }
    fun getAppVersion(){
        val manager = requireContext().packageManager
        val info = manager.getPackageInfo(
            requireContext().packageName, 0
        )
        appVersion.text= info.versionName
    }

}