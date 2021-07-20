package com.r15tech.financestockprices.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyOverview(

    @SerializedName("Symbol") var symbol: String,
    @SerializedName("AssetType") var assetType: String,
    @SerializedName("Name") var name: String,
    @SerializedName("Description") var description: String,
    @SerializedName("Exchange") var exchange: String,
    @SerializedName("Country") var country: String,
    @SerializedName("Sector") var sector: String,
    @SerializedName("Industry") var industry: String,
    @SerializedName("Address") var address: String

) : Parcelable
