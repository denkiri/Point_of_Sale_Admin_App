package com.denkiri.pharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReport
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReport
import com.denkiri.pharmacy.utils.OnReportClick
import java.text.NumberFormat
import java.util.*

class InventoryAdapter(private var modelList: ArrayList<InventoryReport>?,private val recyclerListener: OnReportClick): RecyclerView.Adapter<InventoryViewHolder>()  {
    var filterList = ArrayList<InventoryReport>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item,parent,false)
        return InventoryViewHolder(itemView!!)
    }
    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.invoiceNumber.text = model.invoice
        holder.category.text= model.category
        holder.productName.text=model.name
        holder.code.text=model.product
        holder.description.text=model.dname
        holder.quantityStart.text= (model.qty!!+ model.qtyleft!!).toString()
        holder.quantitySold.text=model.qty.toString()
        holder.quantityEnd.text=model.qtyleft.toString()
        holder.priceValue.text=format.format(model.totalAmount)
        holder.costValue.text=format.format(model.totalCost)
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<InventoryReport>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredInventoryList: ArrayList<InventoryReport>) {

        this.modelList = filteredInventoryList
        notifyDataSetChanged()
    }

}