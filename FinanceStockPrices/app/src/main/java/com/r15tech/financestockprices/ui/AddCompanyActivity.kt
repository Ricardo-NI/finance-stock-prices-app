package com.r15tech.financestockprices.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.r15tech.financestockprices.App
import com.r15tech.financestockprices.R
import com.r15tech.financestockprices.core.createDialog
import com.r15tech.financestockprices.core.createProgressDialog
import com.r15tech.financestockprices.core.hideSoftKeyboard
import com.r15tech.financestockprices.data.model.Company
import com.r15tech.financestockprices.data.repositories.AddCompanyRepository
import com.r15tech.financestockprices.data.services.APIService
import com.r15tech.financestockprices.databinding.ActivityAddCompanyBinding
import com.r15tech.financestockprices.ui.CompanyQuoteActivity.Companion.IS_FROM_MAIN
import com.r15tech.financestockprices.ui.CompanyQuoteActivity.Companion.OBJ_COMPANY
import com.r15tech.financestockprices.utils.CellCardClickListener
import com.r15tech.financestockprices.utils.CompanyAdapter
import com.r15tech.financestockprices.viewModel.AddCompanyViewModel
import com.r15tech.financestockprices.viewModel.AddCompanyViewModelFactory
import com.r15tech.financestockprices.viewModel.MainViewModel
import com.r15tech.financestockprices.viewModel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddCompanyActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    CellCardClickListener {

    private val binding by lazy { ActivityAddCompanyBinding.inflate(layoutInflater) }
    private lateinit var adapter: CompanyAdapter
    private val progressBar by lazy { createProgressDialog() }
    private var apiService = APIService.getInstance()
    private var companiesList = mutableListOf<Company>()
    private val viewModel: AddCompanyViewModel by viewModels {
        AddCompanyViewModelFactory(AddCompanyRepository(apiService))
    }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.svCompanies.setOnQueryTextListener(this)
        binding.svCompanies.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        initRecyclerView()
        viewModelObserver()
    }

    private fun initRecyclerView() {

        adapter = CompanyAdapter(companiesList, this, false)
        binding.recySearchList.layoutManager = LinearLayoutManager(this)
        binding.recySearchList.adapter = adapter
    }

    private fun viewModelObserver() {

        viewModel.repos.observe(this) {
            when (it) {
                is AddCompanyViewModel.State.Loading -> progressBar.show()
                is AddCompanyViewModel.State.SuccessCompany -> {
                    companiesList.clear()
                    companiesList.addAll(it.list)
                    adapter.notifyDataSetChanged()
                    progressBar.dismiss()
                }
                is AddCompanyViewModel.State.Error -> {
                    progressBar.dismiss()
                    createDialog {
                        val msg = getString(R.string.msg_error_get_data)
                        val error = "$msg ${it.error}"
                        setMessage(error)
                    }.show()
                }
                else -> progressBar.dismiss()
            }
        }
    }

    private fun showCompanyQuote(company: Company) {
        val intent = Intent(this, CompanyQuoteActivity::class.java)
        intent.putExtra(OBJ_COMPANY, company)
        intent.putExtra(IS_FROM_MAIN, false)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrBlank()) {
            binding.root.hideSoftKeyboard()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.searchCompany(query)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onCellClickListener(company: Company, clickedStar: Boolean) {

        if (clickedStar) {
            mainViewModel.insert(company)
            finish()
        } else {
            showCompanyQuote(company)
        }
    }
}