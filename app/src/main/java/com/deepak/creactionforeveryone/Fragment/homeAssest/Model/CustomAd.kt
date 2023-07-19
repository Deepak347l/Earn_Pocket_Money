package com.deepak.creactionforeveryone.Fragment.homeAssest.Model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi


class CustomAd: Parcelable {
    var id: String? = null
    var total_reffer: String? = null

    constructor(){}
    @RequiresApi(Build.VERSION_CODES.Q)
    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        total_reffer= `in`.readString()
    }
    constructor(
        id: String?,
        total_reffer: String?,
    ) {
        this.id = id
        this.total_reffer = total_reffer
    }
    override fun describeContents(): Int {
        return 0
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(total_reffer)
    }
    companion object {
        val CREATOR: Parcelable.Creator<CustomAd?> = object : Parcelable.Creator<CustomAd?> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun createFromParcel(`in`: Parcel): CustomAd {
                return CustomAd(`in`)
            }

            override fun newArray(size: Int): Array<CustomAd?> {
                return arrayOfNulls(size)
            }
        }
    }
}