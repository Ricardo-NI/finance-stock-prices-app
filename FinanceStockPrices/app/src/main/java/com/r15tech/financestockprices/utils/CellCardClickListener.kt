package com.r15tech.financestockprices.utils

import com.r15tech.financestockprices.data.model.Company

interface CellCardClickListener {
    fun onCellClickListener(company: Company, clickedStar: Boolean)
}