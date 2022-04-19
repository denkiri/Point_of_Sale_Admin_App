package com.denkiri.pharmacy.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.order.PurchaseOrder
import com.denkiri.pharmacy.utils.OnReportClick
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(private val type: Int, private val context: Context, private var modelList: ArrayList<PurchaseOrder>, private val recyclerListener: OnReportClick): RecyclerView.Adapter<OrderViewHolder>() {
    var filterList = ArrayList<PurchaseOrder>()
    init {
        filterList= modelList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false)
        return OrderViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        if(model.paymentStatus=="Pending"){
            holder.card.setBackgroundColor(Color.parseColor("#FF9800"))
        }
        if(model.paymentStatus=="Complete"){
            holder.card.setBackgroundColor(Color.parseColor("#4CAF50"))
        }
        if(model.paymentStatus=="Pending"){
            holder.payButton.visibility= View.VISIBLE
        }
        if(model.paymentStatus=="Complete"){
            holder.payButton.visibility= View.INVISIBLE
        }
        holder.supplierName.text =model.suplierName
        holder.invoiceNumber.text=model.receiptNumber
        holder.date.text=model.dateDeliver
        holder.dueDate.text=model.dueDate
     //   holder.cost.text=format.format(model.cost).toString()
        holder.status.text=model.paymentStatus

    }
    override fun getItemCount(): Int {
        return modelList!!.size
    }
    fun refresh(modelList: ArrayList<PurchaseOrder>){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredSalesList: ArrayList<PurchaseOrder>) {

        this.modelList = filteredSalesList
        notifyDataSetChanged()
    }
}