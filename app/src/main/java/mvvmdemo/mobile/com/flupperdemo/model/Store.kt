package mvvmdemo.mobile.com.flupperdemo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Store (

	@SerializedName("store_name")
	@ColumnInfo(name = "store_name")
	val store_name : String
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readString()!!) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(store_name)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Store> {
		override fun createFromParcel(parcel: Parcel): Store {
			return Store(parcel)
		}

		override fun newArray(size: Int): Array<Store?> {
			return arrayOfNulls(size)
		}
	}
}