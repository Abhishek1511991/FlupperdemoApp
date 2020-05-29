package mvvmdemo.mobile.com.flupperdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Colors (

	@SerializedName("color")
	@ColumnInfo(name = "product_color_code")
	val color : Int
)