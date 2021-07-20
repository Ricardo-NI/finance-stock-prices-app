package com.r15tech.financestockprices.data.model

import com.google.gson.annotations.SerializedName

data class SearchCompany(

    @SerializedName("bestMatches") var bestMatches: List<Company>
)
