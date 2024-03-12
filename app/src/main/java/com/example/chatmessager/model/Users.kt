package com.example.chatmessager.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.auth.User

data class Users(
    val userid:String ?="",
    val status: String ?="",
    val imageUrl:String ?="",
    val username:String ?="",
    val useremail:String ?=""
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(userid)
        p0.writeString(status)
        p0.writeString(imageUrl)
        p0.writeString(username)
        p0.writeString(useremail)
    }
    companion object CREATOR: Parcelable.Creator<Users>{
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }

    }
}