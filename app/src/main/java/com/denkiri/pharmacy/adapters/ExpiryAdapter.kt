package com.denkiri.pharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReport
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport
import com.denkiri.pharmacy.utils.OnReportClick
import java.text.NumberFormat
import java.util.*

class ExpiryAdapter(private var modelList: ArrayList<ExpiryReport>?,private val recyclerListener: OnReportClick): RecyclerView.Adapter<ExpiryItemViewHolder>()  {
    var filterList = ArrayList<ExpiryReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiryItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.expired_item,parent,false)
        return ExpiryItemViewHolder(itemView!!)
    }
    override fun onBindViewHolder(holder: ExpiryItemViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.productName.text = model.productName
        holder.code.text= model.productCode
        holder.date.text=model.exdate
        holder.amountLose.text=format.format(model.amountLose).toString()
        holder.costValue.text=format.format(model.cost).toString()
        holder.quantity.text=model.qty
        holder.description.text=model.descriptionName
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<ExpiryReport>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredExpiryList: ArrayList<ExpiryReport>) {

        this.modelList = filteredExpiryList
        notifyDataSetChanged()
    }
}