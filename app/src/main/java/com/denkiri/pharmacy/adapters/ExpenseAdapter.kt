package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.expense.Expenses
import com.denkiri.pharmacy.utils.OnExpenseItemClick
import java.text.NumberFormat
import java.util.*

class ExpenseAdapter  (private val type: Int, private  val context: Context, private  var modelList: List<Expenses>?, private val recyclerListener: OnExpenseItemClick):
    RecyclerView.Adapter<ExpenseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ExpenseViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense_item,parent,false)
        return ExpenseViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.expense_title.text =model.name
        holder.expense_amount.text=format.format(model.amount).toString()
        if(model.status==1){
            holder.disable.visibility= View.VISIBLE
            holder.enable.visibility= View.INVISIBLE

        }
        else{
            holder.enable.visibility= View.VISIBLE
            holder.disable.visibility= View.INVISIBLE
        }

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Expenses>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}