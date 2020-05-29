package mvvmdemo.mobile.com.flupperdemo.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import mvvmdemo.mobile.com.flupperdemo.R

@Entity(tableName = "product")
data class Product (


	@SerializedName("id")
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "product_id")
	val id : Int,

	@SerializedName("name")
	@ColumnInfo(name = "product_name")
	val name : String,

	@SerializedName("desc")
	@ColumnInfo(name = "product_details")
	val desc : String,

	@SerializedName("reguler_price")
	@ColumnInfo(name = "base_price")
	val reguler_price : String,

	@SerializedName("sale_price")
	@ColumnInfo(name = "discounted_price")
	val sale_price : String,

	@SerializedName("product_photo")
	@ColumnInfo(name = "product_photo")
	val product_photo : String

/*
	val colors : List<Colors>,
	val stores : List<Stores>*/
)
{

	@BindingAdapter( "product_photo" )
	fun loadImage(imageView: ImageView, imageUrl:String)
	{
		Glide.with(imageView.getContext())
			.setDefaultRequestOptions(
				RequestOptions()
				.circleCrop())
			.load(imageUrl)
			.placeholder(R.drawable.image_place_holder)
			.into(imageView);
	}

	override fun equals(other: Any?): Boolean {
		return super.equals(other)
	}

}
