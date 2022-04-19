package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.expense.Income
import com.denkiri.pharmacy.models.reports.salesReport.SalesReport
import com.denkiri.pharmacy.utils.RecordedExpenseItemClick
import java.text.NumberFormat
import java.util.*
class IncomeAdapter (private val type: Int, private  val context: Context, private  var modelList: List<Income>?, private val recyclerListener: RecordedExpenseItemClick):
    RecyclerView.Adapter<IncomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):IncomeViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.income_item,parent,false)
        return IncomeViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder:IncomeViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.incomeTitle.text =model.name
        holder.incomeAmount.text =format.format(model.amount).toString()
        holder.date.text=model.date
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Income>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredIncomeList: ArrayList<Income>) {
        this.modelList = filteredIncomeList
        notifyDataSetChanged()
    }
}