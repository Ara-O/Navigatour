package com.example.navigatour

import android.os.Parcel
import android.os.Parcelable

class RouteSteps() : Parcelable {
     var step: String = "";

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RouteSteps> {
        override fun createFromParcel(parcel: Parcel): RouteSteps {
            return RouteSteps(parcel)
        }

        override fun newArray(size: Int): Array<RouteSteps?> {
            return arrayOfNulls(size)
        }
    }
}