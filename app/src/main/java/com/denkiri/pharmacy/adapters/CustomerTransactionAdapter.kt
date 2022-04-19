package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.ui.reports.CustomerTransaction
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.util.ArrayList

class CustomerTransactionAdapter (private val type: Int, private  val context: Context, private  var modelList: List<Customer>?, private val recyclerListener: OnCategoryItemClick):
        RecyclerView.Adapter<CustomerTransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerTransactionViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.customer_transaction_item,parent,false)
        return CustomerTransactionViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CustomerTransactionViewHolder, position: Int) {
        val model= modelList!![position]
        holder.customerName.text =model.customerName
        holder.addressValue.text=model.address
        holder.contactValue.text=model.contact
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Customer>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredCustomerList: ArrayList<Customer>) {
        this.modelList = filteredCustomerList
        notifyDataSetChanged()
    }}