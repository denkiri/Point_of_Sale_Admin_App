package com.denkiri.pharmacy.ui.main
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.adapters.PaymentReceiptAdapter
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.custom.Status
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.pharmacy.models.receipt.Receipt
import com.denkiri.pharmacy.models.receipt.ReceiptData
import com.denkiri.pharmacy.models.subscription.Subscription
import com.denkiri.pharmacy.models.subscription.SubscriptionData
import com.denkiri.pharmacy.utils.OnCategoryClick
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_reports.*
import kotlinx.android.synthetic.main.subscription_fragment.*
import kotlinx.android.synthetic.main.subscription_fragment.avi
import kotlinx.android.synthetic.main.subscription_fragment.empty_button
import kotlinx.android.synthetic.main.subscription_fragment.empty_layout
import kotlinx.android.synthetic.main.subscription_fragment.itemsswipetorefresh
import kotlinx.android.synthetic.main.subscription_fragment.main
import kotlinx.android.synthetic.main.subscription_fragment.recyclerView
import java.text.NumberFormat
import java.util.*
class SubscriptionFragment : Fragment() {
    lateinit var paymentAdapter:PaymentReceiptAdapter
    private lateinit var receipt: List<Receipt>
    var businessCode:String?=null
    var basicSubscription:Double?=null
    var subscriptionValue:Double?=null
    var phone:String?=null
    var fixedAmount:Int?=null
    var basicAmount:Int?=null
    companion object {
        fun newInstance() = SubscriptionFragment()
    }

    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.subscription_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubscriptionViewModel::class.java)
        initView()
        setUpUi()
        observers()
        /*
        basic_subscription.setOnClickListener { basicSubscription() }
        fixed_subscription.setOnClickListener { fixedSubscription() }

         */
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.white))
        itemsswipetorefresh.setColorSchemeColors(Color.GREEN)
        itemsswipetorefresh.setOnRefreshListener {
            init()
            viewModel.getReceipt(businessCode.toString())
            //   observers()
            itemsswipetorefresh.isRefreshing = false
        }

    }
    private fun initView(){
        receipt = ArrayList()
        paymentAdapter = context?.let {
            PaymentReceiptAdapter(0, it, receipt, object : OnCategoryClick {
                override fun selected(pos: Int) {
                    viewModel.completeSubscription(receipt[pos].amount.toString(),receipt[pos].phoneNumber.toString(),receipt[pos].invoiceNumber.toString())
                }

                override fun onClickListener(position1: Int) {

                }


            })
        }!!
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter =  paymentAdapter
        paymentAdapter.notifyDataSetChanged()
    }
    private fun setUpReceipts( receipt: ArrayList<Receipt>) {
        this.receipt = receipt
        paymentAdapter.refresh(this.receipt)
        Handler().postDelayed(Runnable {
        }, 1)
    }
    fun basicSubscription(){
        viewModel.subscribe(basicAmount.toString(),phone.toString(),businessCode.toString())

    }
    fun fixedSubscription(){
        viewModel.subscribe(fixedAmount.toString(),phone.toString(),businessCode.toString())

    }
    private fun setStatus(data: Resource<SubscriptionData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
               //   empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener {
                init()
            }
        }
    }


    fun setUpUi() {

        viewModel.getOuthProfile().observe(viewLifecycleOwner, {
            try {
                phone=it.mobile
                businessCode= it.businessCode.toString()
                init()
                viewModel.getReceipt(it.businessCode.toString())

            } catch (e: Exception) {
            }
        })
        viewModel.getSubscription().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                fixedSubscriptionAmount.text =format.format( it.fixedAmount).toString()
                subscriptionValue= basicSubscription!!* it.percentage!! /100
             //   subscriptionAmount.text=format.format( subscriptionValue).toString()
                fixedAmount=it.fixedAmount
                basicAmount= subscriptionValue!!.toInt()


            } catch (e: Exception) {
            }
        })
        viewModel.getLastMonthProfit().observe(viewLifecycleOwner, {
            try {
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.setCurrency(Currency.getInstance("KSh"))
                basicSubscription=it.lastMonthProfit

           

            } catch (e: Exception) {
            }
        })
    }
    fun init() {
           viewModel.subscription(getParameters())

    }
    private fun getParameters(): SubscriptionData {

        return SubscriptionData(Subscription(businessCode.toString()))
    }

 fun observers(){
     viewModel.observeSubscription().observe(viewLifecycleOwner, { data ->
         run {
             setStatus(data)
             if (data.status == Status.SUCCESS && data.data != null) {
                 viewModel.saveSubscription(data.data)
             }
         }
     })
     viewModel.observeSubscriptionAction().observe(viewLifecycleOwner, { data ->
         run {
             setStatusB(data)
             if (data.status == Status.SUCCESS && data.data != null) {
                 Toasty.success(requireContext(),data.data.message.toString(), Toast.LENGTH_LONG, true).show()



             }
         }
     })
     viewModel.observeReceipt().observe(viewLifecycleOwner, { data ->
         run {
             setStatusC(data)
             if (data.status == Status.SUCCESS) {
                 if (data.data?.receipts != null && !data.data.receipts!!.isEmpty()) {
                     setUpReceipts(data.data.receipts as ArrayList<Receipt>)
                 } else {
                     setUpReceipts(ArrayList())
                 }
             }
         }
     })
 }
    private fun setStatusB(data: Resource<PaymentResponseData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                //   empty_text.text = data.message
                view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            //    empty_layout.visibility = View.VISIBLE
            main.visibility = View.VISIBLE
            //  empty_button.setOnClickListener {
            // init()
        }
    }
    private fun setStatusC(data: Resource<ReceiptData>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status

        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                //   empty_text.text = data.message
                //  view?.let { Snackbar.make(it, data.message.toString(), Snackbar.LENGTH_LONG).show() }
            }

            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener {
                init()
            }
        }
    }

}