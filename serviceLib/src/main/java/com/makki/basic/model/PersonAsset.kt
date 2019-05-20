package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class PersonAsset(
    val id: Int,
    val login: String,
    val name: String,
    val email: String,
    val city: String,
    val photo: String,
    val phone1: String,
    val phone2: String,
    val extra: String,
    val accessLevel: Int

) : Parcelable {

    companion object {
        val Null = PersonAsset(0, "", "", "", "", "", "", "", "", 0)
    }

}