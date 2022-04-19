package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.cashreport.CashReport
import com.denkiri.pharmacy.utils.OnReportClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CashAdapter (private val type: Int, private  val context: Context, private  var modelList: ArrayList<CashReport>?, private val recyclerListener: OnReportClick): RecyclerView.Adapter<CashViewHolder>() {
    var filterList = ArrayList<CashReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.cash_item,parent,false)
        return CashViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CashViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.paymentMode.text =model.type
        holder.invoiceNumber.text=model.invoiceNumber
        holder.date.text=model.date
        holder.customerName.text=model.name
        holder.sales.text=format.format(model.amount).toString()

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<CashReport>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredCashList: ArrayList<CashReport>) {

        this.modelList = filteredCashList
        notifyDataSetChanged()
    }
}