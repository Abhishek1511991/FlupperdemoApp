package mvvmdemo.mobile.com.flupperdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Stores (

	@SerializedName("store_name")
	@ColumnInfo(name = "store_name")
	val store_name : String
)