package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makki.basic.model.PersonAsset

/**
 * @author Maksym.Popovych
 */

@Entity(tableName = "person_table")
class PersonDbo {

    companion object {
        val Null = PeriodDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "login")
    var login: String = ""

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "email")
    var email: String = ""

    @ColumnInfo(name = "city")
    var city: String = ""

    @ColumnInfo(name = "photo")
    var photo: String = ""

    @ColumnInfo(name = "phone1")
    var phone1: String = ""

    @ColumnInfo(name = "phone2")
    var phone2: String = ""

    @ColumnInfo(name = "extra")
    var extra: String = ""

    @ColumnInfo(name = "access_level")
    var accessLevel: Int = 0

    fun build() = PersonAsset(id, login, name, email, city, photo, phone1, phone2, extra, accessLevel)
}