package com.r15tech.financestockprices.data.model

import com.google.gson.annotations.SerializedName

data class GlobalQuote(

    @SerializedName("Global Quote") var globalQuote: CompanyQuote
)
