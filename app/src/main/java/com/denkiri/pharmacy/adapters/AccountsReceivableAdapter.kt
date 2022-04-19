package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivable
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.text.NumberFormat
import java.util.*

class AccountsReceivableAdapter  (private val type: Int, private  val context: Context, private  var modelList: ArrayList<AccountsReceivable>?, private val recyclerListener: OnCategoryItemClick): RecyclerView.Adapter<AccountsReceivableViewHolder>()  {
    var filterList = ArrayList<AccountsReceivable>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsReceivableViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.receivable_item,parent,false)
        return AccountsReceivableViewHolder(itemView!!)
    }
    override fun onBindViewHolder(holder: AccountsReceivableViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.invoiceNumber.text = model.invoiceNumber
        holder.date.text= model.dueDate
        holder.balance.text=format.format(model.balance).toString()
        holder.customerName.text=model.name
        holder.date3.text=model.date

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<AccountsReceivable>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredReceivableList: ArrayList<AccountsReceivable>) {

        this.modelList = filteredReceivableList
        notifyDataSetChanged()
    }
}