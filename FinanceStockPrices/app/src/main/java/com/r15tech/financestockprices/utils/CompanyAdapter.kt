package com.r15tech.financestockprices.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.r15tech.financestockprices.R
import com.r15tech.financestockprices.data.model.Company
import com.r15tech.financestockprices.databinding.CompanyItemBinding

class CompanyAdapter(
    private val company: List<Company>,
    private val listener: CellCardClickListener,
    private val isSavedList: Boolean
) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(company: Company, listener: CellCardClickListener) {

            var cSymbol = company.symbol
            if (cSymbol.contains(".")) {
                cSymbol = cSymbol.split(".")[0]
            }

            binding.txvSymbol.text = cSymbol
            binding.txvName.text = company.name

            binding.cardviewCompany.setOnClickListener {

                listener.onCellClickListener(company, false)
            }

            if (isSavedList) {
                binding.btnStar.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_star
                    )
                )
            }

            binding.btnStar.setOnClickListener {
                listener.onCellClickListener(company, true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CompanyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(company[position], listener)
    }

    override fun getItemCount(): Int = company.size
}