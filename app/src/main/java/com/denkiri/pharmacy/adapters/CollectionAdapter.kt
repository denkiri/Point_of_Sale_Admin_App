package com.denkiri.pharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport
import com.denkiri.pharmacy.utils.OnReportClick
import com.denkiri.pharmacy.utils.OnSupplierClick
import java.text.NumberFormat
import java.util.*

class CollectionAdapter(private var modelList: ArrayList<CollectionReport>?, private val recyclerListener: OnReportClick): RecyclerView.Adapter<CollectionViewHolder>()  {
    var filterList = ArrayList<CollectionReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.collection_item,parent,false)
        return CollectionViewHolder(itemView!!)
    }
    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.invoiceNumber.text = model.invoiceNumber
        holder.date.text= model.dueDate
        holder.collectionDate.text=model.date
        holder.balance.text=format.format(model.balance).toString()
        holder.customerName.text=model.name
        holder.remarks.text=model.remarks
        holder.amountPaid.text=format.format(model.amountReceived).toString()
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<CollectionReport>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredReceivableList: ArrayList<CollectionReport>) {

        this.modelList = filteredReceivableList
        notifyDataSetChanged()
    }
}