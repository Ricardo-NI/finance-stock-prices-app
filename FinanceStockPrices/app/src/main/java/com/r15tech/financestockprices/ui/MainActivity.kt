package com.r15tech.financestockprices.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.r15tech.financestockprices.App
import com.r15tech.financestockprices.R
import com.r15tech.financestockprices.core.createDeleteDialog
import com.r15tech.financestockprices.data.model.Company
import com.r15tech.financestockprices.databinding.ActivityMainBinding
import com.r15tech.financestockprices.ui.CompanyQuoteActivity.Companion.IS_FROM_MAIN
import com.r15tech.financestockprices.ui.CompanyQuoteActivity.Companion.OBJ_COMPANY
import com.r15tech.financestockprices.utils.CellCardClickListener
import com.r15tech.financestockprices.utils.CompanyAdapter
import com.r15tech.financestockprices.viewModel.MainViewModel
import com.r15tech.financestockprices.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity(), CellCardClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: CompanyAdapter
    private var companiesList = mutableListOf<Company>()
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()
        getSavedCompanies()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddCompanyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSavedCompanies() {
        mainViewModel.getAll().observe(this, {
            companiesList.clear()
            companiesList.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerView() {

        adapter = CompanyAdapter(companiesList, this, true)
        binding.recyList.layoutManager = LinearLayoutManager(this)
        binding.recyList.adapter = adapter
    }

    private fun showCompanyQuote(company: Company) {//companySymbol: String, currency: String){
        val intent = Intent(this, CompanyQuoteActivity::class.java)
        intent.putExtra(OBJ_COMPANY, company)
        intent.putExtra(IS_FROM_MAIN, true)
        startActivity(intent)
    }

    private fun deleteCompany(company: Company) {

        mainViewModel.deleteData(company)
        adapter.notifyDataSetChanged()
    }

    private fun showDialogConfirmDelete(company: Company) {
        createDeleteDialog {
            setTitle(getString(R.string.delete_record))
            setMessage(getString(R.string.confirm_delete))
            setPositiveButton(android.R.string.ok) { _, _ ->
                deleteCompany(company)
            }
        }.show()
    }

    override fun onCellClickListener(company: Company, clickedStar: Boolean) {

        if (clickedStar) {
            showDialogConfirmDelete(company)
        } else {
            showCompanyQuote(company)
        }
    }
}