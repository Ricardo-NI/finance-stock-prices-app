package com.r15tech.financestockprices.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.r15tech.financestockprices.App
import com.r15tech.financestockprices.R
import com.r15tech.financestockprices.core.createDeleteDialog
import com.r15tech.financestockprices.core.createDialog
import com.r15tech.financestockprices.core.createProgressDialog
import com.r15tech.financestockprices.data.model.Company
import com.r15tech.financestockprices.data.model.CompanyOverview
import com.r15tech.financestockprices.data.model.CompanyQuote
import com.r15tech.financestockprices.data.repositories.AddCompanyRepository
import com.r15tech.financestockprices.data.services.APIService
import com.r15tech.financestockprices.databinding.ActivityCompanyQuoteBinding
import com.r15tech.financestockprices.viewModel.AddCompanyViewModel
import com.r15tech.financestockprices.viewModel.AddCompanyViewModelFactory
import com.r15tech.financestockprices.viewModel.MainViewModel
import com.r15tech.financestockprices.viewModel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CompanyQuoteActivity : AppCompatActivity() {

    companion object {
        const val IS_FROM_MAIN = "isfrommain"
        const val OBJ_COMPANY = "objcompany"
        private const val DATE_MASK = "dd-MM-yyyy"
        private const val EMPTY_RETURN = "----"
        const val BRL_CURRENCY = "BRL"
    }

    private val progressBar by lazy { createProgressDialog() }
    private var apiService = APIService.getInstance()
    private val binding by lazy { ActivityCompanyQuoteBinding.inflate(layoutInflater) }
    private var companySymbol = EMPTY_RETURN
    private var companyCurrency = BRL_CURRENCY
    private var isFromMain = false
    private lateinit var companyQ: CompanyQuote
    private var company: Company? = null
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

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {

                //Parcelable Data
                company = getParcelable(OBJ_COMPANY)
                isFromMain = getBoolean(IS_FROM_MAIN, false)

                if (company != null) {
                    companyCurrency = company!!.currency
                    companySymbol = company!!.symbol
                    getCompanyQuote(company!!.symbol)
                }
            }
        }
        insertListeners()
    }

    private fun insertListeners() {
        if (isFromMain) {
            binding.btnStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star))
            binding.btnStar.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow))
        }

        binding.btnStar.setOnClickListener {
            if (isFromMain) {
                company?.let { it1 -> showDialogConfirmDelete(it1) }
            } else {
                company?.let { it1 ->
                    insertCompany(it1)
                }
            }
        }

        // a visão global da empresa não esta disponível na API para empresas brasileiras.
        if (!isFromMain && companyCurrency != BRL_CURRENCY) {

            binding.btnCompanyOverview.visibility = View.VISIBLE
            binding.btnCompanyOverview.setOnClickListener {

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getCompanyOverview(companySymbol)
                    // finish()
                }
            }
        }

    }

    private fun insertCompany(company: Company) {
        mainViewModel.insert(company)
        finish()
    }

    private fun deleteCompany(company: Company) {
        mainViewModel.deleteData(company)
        finish()
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

    private fun getCompanyQuote(symbol: String) {

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getCompanyQuote(symbol)
        }

        viewModel.repos.observe(this) {
            when (it) {
                is AddCompanyViewModel.State.Loading -> progressBar.show()
                is AddCompanyViewModel.State.SuccessCompanyQuote -> {
                    progressBar.dismiss()
                    populateFields(it.obj, companyCurrency)
                }

                is AddCompanyViewModel.State.SuccessCompanyOverview -> {
                    progressBar.dismiss()
                    showCompanyOverview(it.obj)

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

    private fun showCompanyOverview(companyO: CompanyOverview) {
        val intent = Intent(this, CompanyOverviewActivity::class.java)
        intent.putExtra(OBJ_COMPANY, companyO)
        startActivity(intent)

    }

    private fun populateFields(companyQuote: CompanyQuote, currency: String) {

        companyQ = companyQuote

        binding.txvDate.text = dateFormat(companyQuote.latestTradingDay)
        binding.txvSymbol.text = companySymbolFormat(companyQuote.symbol)
        binding.txvOpen.text = currencyFormat(companyQuote.open, currency)
        binding.txvHigh.text = currencyFormat(companyQuote.high, currency)
        binding.txvLow.text = currencyFormat(companyQuote.low, currency)
        binding.txvPrice.text = currencyFormat(companyQuote.price, currency)
        binding.txvPercent.text = percentFormat(companyQuote.changePercent)
        binding.txvChange.text = currencyFormat(companyQuote.change, currency)
        binding.txvPrevClose.text = currencyFormat(companyQuote.previousClose, currency)
        binding.txvCurrency.text = currency

        if (companyQuote.change.isNotEmpty()) {
            if (companyQuote.change.toDouble() < 0) {
                binding.txvChange.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.txvPercent.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }

    private fun currencyFormat(value: String, currency: String): String {

        return try {

            val n = NumberFormat.getCurrencyInstance()
            n.currency = Currency.getInstance(currency)
            val roundValue = BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN)
            n.format(roundValue)

        } catch (e: Exception) {
            EMPTY_RETURN
        }
    }

    private fun percentFormat(value: String): String {

        return try {

            var newValue = value
            newValue = newValue.dropLast(1)
            val roundValue = BigDecimal(newValue).setScale(2, RoundingMode.HALF_EVEN)
            val retValue = "($roundValue%)"
            retValue

        } catch (e: Exception) {
            EMPTY_RETURN
        }
    }

    private fun companySymbolFormat(symbol: String): String {
        return symbol.split(".")[0]
    }

    private fun dateFormat(date: String): String {

        return try {
            var newDate = date
            val locale = Locale.getDefault()
            if (locale.toString().contains("pt")) {
                val currentDate: Date? = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
                val parser = SimpleDateFormat(DATE_MASK, locale)
                currentDate?.let { it -> newDate = parser.format(it).toString() }
            }
            newDate

        } catch (e: Exception) {
            EMPTY_RETURN
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

