package iu.c323.fall2024.afinal.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Order(
    val restaurant: @RawValue Restaurant?,
    val items: List<OrderItem>?,
    val totalPrice: Double?,
    val deliveryAddress: String?,
    val specialInstructions: String?,
    var orderTime: String?,
    val deliveryTime: Double?,
    var timestamp: Timestamp?
):Parcelable{
    constructor() : this(null, null, null, null, null, null, null, null)
}

@Parcelize
data class OrderItem(
    val itemName: String?,
    val price: Double?,
    val quantity: Int?
): Parcelable{
    constructor() : this(null, null, null)
}