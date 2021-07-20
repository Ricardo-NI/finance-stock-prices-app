package com.r15tech.financestockprices.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Company(

    @PrimaryKey
    @SerializedName("1. symbol") var symbol: String,
    @SerializedName("2. name") var name: String,
    @SerializedName("4. region") var region: String,
    @SerializedName("7. timezone") var timezone: String,
    @SerializedName("8. currency") var currency: String

) : Parcelable
