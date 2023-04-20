package br.com.camargo.vinicius.employees.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.camargo.vinicius.employees.R
import br.com.camargo.vinicius.employees.model.EmployeeEntity
import br.com.camargo.vinicius.employees.viewmodel.mainview.MainViewModel

class EmployeeListAdapter(
        val onItemClicked: (EmployeeEntity) -> Unit,
        val viewModel: MainViewModel,
        private val arrayList: ArrayList<EmployeeEntity>,
        private val context: Context,
    ) :
    RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {

    var selectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = arrayList[position]
        holder.name.text = context.getString(R.string.name_lastname, employee.name, employee.lastname)
        holder.email.text = employee.email
        holder.nis.text = employee.nis

        when (selectedPos) {
            -1 -> {
                holder.itemLayout.setBackgroundResource(0)
            }
            else -> when (selectedPos) {
                position -> {
                    holder.itemLayout.setBackgroundResource(R.color.gray)
                }
                else -> {
                    holder.itemLayout.setBackgroundResource(0)
                }
            }
        }

        holder.itemView.setOnClickListener {
            holder.itemLayout.setBackgroundResource(R.color.gray)
            if (selectedPos != position) {
                notifyItemChanged(selectedPos)
                selectedPos = position
            }

            onItemClicked.invoke(employee)
        }
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.txtName)
        val email = itemView.findViewById<TextView>(R.id.txtEmail)
        val nis = itemView.findViewById<TextView>(R.id.txtNis)
        val itemLayout = itemView.findViewById<ConstraintLayout>(R.id.itemLayout)
    }
}