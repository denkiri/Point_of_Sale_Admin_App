package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.creditreport.CreditReport
import com.denkiri.pharmacy.models.reports.salesReport.SalesReport
import com.denkiri.pharmacy.utils.OnReportClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
class CreditAdapter (private val type: Int, private  val context: Context, private  var modelList: ArrayList<CreditReport>?, private val recyclerListener: OnReportClick): RecyclerView.Adapter<CreditViewHolder>() {
    var filterList = ArrayList<CreditReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.credit_item,parent,false)
        return CreditViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.paymentMode.text =model.type
        holder.invoiceNumber.text=model.invoiceNumber
        holder.date.text=model.date
        holder.customerName.text=model.name
        holder.sales.text=format.format(model.amount).toString()
        holder.balance.text=format.format(model.balance).toString()

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<CreditReport>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredCreditList: ArrayList<CreditReport>) {

        this.modelList = filteredCreditList
        notifyDataSetChanged()
    }
}