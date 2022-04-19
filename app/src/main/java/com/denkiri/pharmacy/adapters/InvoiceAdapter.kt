package com.denkiri.pharmacy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.invoice.Invoice
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.text.NumberFormat
import java.util.*

class InvoiceAdapter  (private val type: Int, private  val context: Context, private  var modelList: ArrayList<Invoice>?, private val recyclerListener: OnCategoryItemClick): RecyclerView.Adapter<InvoiceViewHolder>() {
    var filterList = ArrayList<Invoice>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.invoice_item,parent,false)
        return InvoiceViewHolder(type,itemView!!,recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.invoiceNumber.text = model.invoiceNumber
        holder.date.text= model.dueDate
        holder.due.text=format.format(model.totalAmount).toString()
        holder.balance.text=format.format(model.balance).toString()
        holder.name.text=model.name
        holder.address.text=model.address
        holder.contact.text=model.contact
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<Invoice>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredInvoiceList: ArrayList<Invoice>) {

        this.modelList = filteredInvoiceList
        notifyDataSetChanged()
    }

}