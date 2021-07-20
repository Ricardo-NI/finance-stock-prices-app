package com.r15tech.financestockprices.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.r15tech.financestockprices.data.model.CompanyOverview
import com.r15tech.financestockprices.databinding.ActivityCompanyOverviewBinding
import com.r15tech.financestockprices.ui.CompanyQuoteActivity.Companion.OBJ_COMPANY

class CompanyOverviewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCompanyOverviewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {

                //Parcelable Data
                val companyO: CompanyOverview? = getParcelable(OBJ_COMPANY)

                if (companyO != null) {
                    populateFields(companyO)
                }
            }
        }
    }

    private fun populateFields(companyO: CompanyOverview) {

        binding.txvSymbol.text = companyO.symbol
        binding.txvCompanyName.text = companyO.name
        binding.txvAssetType.text = companyO.assetType
        binding.txvExchange.text = companyO.exchange
        binding.txvCountry.text = companyO.country
        binding.txvSector.text = companyO.sector
        binding.txvIndustry.text = companyO.industry
        binding.txvAddress.text = companyO.address
        binding.txvDescription.text = companyO.description

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}