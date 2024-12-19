package iu.c323.fall2024.afinal.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Restaurant(
    var id: String?,
    var name: String?,
    var location: @RawValue GeoPoint?
): Parcelable {
    constructor() : this(id = null, name = null, location = null)
}


