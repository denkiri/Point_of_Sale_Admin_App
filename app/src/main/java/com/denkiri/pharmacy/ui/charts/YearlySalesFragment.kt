package com.denkiri.pharmacy.ui.charts

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.network.NetworkUtils
import kotlinx.android.synthetic.main.fragment_yearly_sales.*
/**
 * A simple [Fragment] subclass.
 * Use the [YearlySalesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YearlySalesFragment : Fragment() {
    private lateinit var pageUrl: String

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
        return inflater.inflate(R.layout.fragment_yearly_sales, container, false)
    }

    companion object {
        const val PAGE_URL = "pageUrl"
        const val MAX_PROGRESS = 100
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YearlySalesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YearlySalesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        // viewModel = ViewModelProvider(this).get(ChartViewModel::class.java)
        pageUrl=NetworkUtils.url()+"charts/pages/yearly_chart.php"
        loadChart()
        refresh.setOnClickListener {
            loadChart()
        }
    }

    fun loadChart(){
        if (NetworkUtils.isConnected(requireContext())) {
            main.visibility=View.VISIBLE
            avi.visibility=View.VISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            initWebView()
            setWebClient()
            loadUrl(pageUrl)
        } else {
            main.visibility=View.VISIBLE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = true


        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                main.visibility=View.VISIBLE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


            }


            private fun loadError() {
                val html =
                        ("<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                                + "<tr>"
                                + "<td><div align=\"center\"><font color=\"black\" size=\"20pt\"></font></div></td>"
                                + "</tr>" + "</table><html><body>")
                println("html $html")
                val base64 = Base64.encodeToString(
                        html.toByteArray(),
                        Base64.DEFAULT
                )
                webView.loadData(base64, "text/html; charset=utf-8", "base64")
                println("loaded html")
            }
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView, url: String) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                activity?.window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }

        }



    }

    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                    view: WebView,
                    newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MonthFragment.MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    avi.visibility=View.VISIBLE
                }

                if (newProgress == MonthFragment.MAX_PROGRESS) {
                    progressBar.visibility = ProgressBar.GONE
                    avi.visibility=View.INVISIBLE
                }
            }
        }
    }

    private fun loadUrl(pageUrl: String) {
        webView.loadUrl(pageUrl)
    }

}