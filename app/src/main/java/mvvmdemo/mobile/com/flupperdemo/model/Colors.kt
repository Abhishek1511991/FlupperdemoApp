package mvvmdemo.mobile.com.flupperdemo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Colors (

	@SerializedName("color")
	@ColumnInfo(name = "product_color_code")
	val color : Int
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readInt()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(color)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Colors> {
		override fun createFromParcel(parcel: Parcel): Colors {
			return Colors(parcel)
		}

		override fun newArray(size: Int): Array<Colors?> {
			return arrayOfNulls(size)
		}
	}
}