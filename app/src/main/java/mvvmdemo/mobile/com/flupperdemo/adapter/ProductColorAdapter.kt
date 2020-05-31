package mvvmdemo.mobile.com.flupperdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mvvmdemo.mobile.com.flupperdemo.databinding.ProductColorBinding
import mvvmdemo.mobile.com.flupperdemo.model.Colors
import mvvmdemo.mobile.com.flupperdemo.model.Product

class ProductColorAdapter(colorsList:ArrayList<Colors>):RecyclerView.Adapter<ProductColorAdapter.ColorViewHolder>() {
    var colorsList:ArrayList<Colors>?=colorsList;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemColorBinding: ProductColorBinding =ProductColorBinding.inflate(layoutInflater, parent, false)
        return ColorViewHolder(mItemColorBinding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color: Colors = colorsList?.get(position)!!
        holder.bindData(color)
    }

    override fun getItemCount(): Int {
        return colorsList?.size!!
    }

    class ColorViewHolder(dataBinding: ProductColorBinding): RecyclerView.ViewHolder(dataBinding.root)
    {
        val colorBindingUtil=dataBinding
        fun bindData(color: Colors)
        {
            colorBindingUtil.setColor(color)
            colorBindingUtil.executePendingBindings()
            //colorBindingUtil.circleColor.getLayoutParams().height = 50
            //colorBindingUtil.circleColor.getLayoutParams().width = 50
        }
    }
}