package com.denkiri.pharmacy.ui.receipt
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.network.NetworkUtils
import kotlinx.android.synthetic.main.fragment_receipt.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [ReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceiptFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var pageUrl: String
    var invoice:String?=null
    var paymentMode:String?=null
    private var myWebView: WebView? = null
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
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    companion object {
        const val PAGE_URL = "pageUrl"
        const val MAX_PROGRESS = 100
        @JvmStatic
        fun newInstance() =
            ReceiptFragment().apply {
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
        invoice=(activity as ReceiptActivity).invoice
        paymentMode=(activity as ReceiptActivity).paymentMode
        if (paymentMode =="mpesa"){
        pageUrl= NetworkUtils.url()+"charts/pages/mpreview.php?id=$invoice"
        }
        if (paymentMode =="cash"){
            pageUrl=NetworkUtils.url()+"charts/pages/receipt.php?id=$invoice"
        }
        if (paymentMode =="credit"){
            pageUrl=NetworkUtils.url()+"charts/pages/invoice.php?invoice=$invoice"
            webView.setInitialScale(100)
        }
        loadChart()
        refresh.setOnClickListener { loadChart() }

    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createWebPrintJob(webView: WebView) {

        val printManager = context
                ?.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.createPrintDocumentAdapter("MyDocument")
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }

        val jobName = getString(R.string.app_name) + " Print Test"

        printManager.print(jobName, printAdapter,
                PrintAttributes.Builder().build())
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
            main.visibility=View.INVISIBLE
            print.visibility=View.INVISIBLE
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
        if (paymentMode =="credit"){
            webView.setInitialScale(80)
        }
        else {
            webView.setInitialScale(155)
        }
        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                main.visibility=View.VISIBLE
                print.visibility=View.INVISIBLE
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
                print.visibility=View.INVISIBLE
            }


            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView, url: String) {
                //  createWebPrintJob(view)
                // print.visibility=View.VISIBLE
                print.setOnClickListener {  createWebPrintJob(view) }
                print.visibility=view.visibility
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                    avi.visibility=View.VISIBLE
                }

                if (newProgress == MAX_PROGRESS) {
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