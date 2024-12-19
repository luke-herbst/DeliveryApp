package iu.c323.fall2024.afinal.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val name: String? = "",
    val price: Double? = 0.0
) : Parcelable {
    constructor() : this(null, null)
}

@Parcelize
data class MenuItemWithQuantity(
    val menuItem: MenuItem,
    var quantity: Int = 0
): Parcelable


