package mvvmdemo.mobile.com.flupperdemo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import mvvmdemo.mobile.com.flupperdemo.utility.TypeConverter
import java.util.ArrayList

@Entity(tableName = "product")
data class Product (


	@SerializedName("id")
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "product_id")
	var id : Int,

	@SerializedName("name")
	@ColumnInfo(name = "product_name")
	var name : String,

	@SerializedName("desc")
	@ColumnInfo(name = "product_details")
	var desc : String,

	@SerializedName("reguler_price")
	@ColumnInfo(name = "base_price")
	var reguler_price : String,

	@SerializedName("sale_price")
	@ColumnInfo(name = "discounted_price")
	var sale_price : String,

	@SerializedName("product_photo")
	@ColumnInfo(name = "product_photo")
	var product_photo : String,

	@TypeConverters(TypeConverter::class)
	@NonNull
	@SerializedName("colors")
	@ColumnInfo(name = "colors")
	var colors : ArrayList<Colors>,

	@TypeConverters(TypeConverter::class)
	@SerializedName("stores")
	@ColumnInfo(name = "stores")
	var stores : ArrayList<Store>


) : Parcelable {


	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.createTypedArrayList(Colors)!!,
		parcel.createTypedArrayList(Store)!!
	) {
	}

	override fun hashCode(): Int {
		var result = id
		result = 31 * result + name.hashCode()
		result = 31 * result + desc.hashCode()
		result = 31 * result + reguler_price.hashCode()
		result = 31 * result + sale_price.hashCode()
		result = 31 * result + product_photo.hashCode()
		result = 31 * result + colors.hashCode()
		result = 31 * result + stores.hashCode()
		return result
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as Product

		if (id != other.id) return false
		if (name != other.name) return false
		if (desc != other.desc) return false
		if (reguler_price != other.reguler_price) return false
		if (sale_price != other.sale_price) return false
		if (product_photo != other.product_photo) return false
		if (colors != other.colors) return false
		if (stores != other.stores) return false

		return true
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeString(desc)
		parcel.writeString(reguler_price)
		parcel.writeString(sale_price)
		parcel.writeString(product_photo)
		parcel.writeTypedList(colors)
		parcel.writeTypedList(stores)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Product> {
		override fun createFromParcel(parcel: Parcel): Product {
			return Product(parcel)
		}

		override fun newArray(size: Int): Array<Product?> {
			return arrayOfNulls(size)
		}
	}

}
