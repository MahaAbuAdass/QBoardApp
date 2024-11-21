package com.example.slaughterhousescreen.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentQ (
    @SerializedName("TicketNo"      ) var TicketNo      : String? = null,
    @SerializedName("CounterId"     ) var CounterId     : Int?    = null,
    @SerializedName("ServiceId"     ) var ServiceId     : Int?    = null,
    @SerializedName("AlphaPrefix"   ) var AlphaPrefix   : String? = null,
    @SerializedName("CounterDoorNo" ) var CounterDoorNo : String? = null,
    @SerializedName("callTime"      ) var callTime      : String? = null,
    @SerializedName("QNameEn"       ) var QNameEn       : String? = null,
    @SerializedName("QNameAr"       ) var QNameAr       : String? = null
):Parcelable