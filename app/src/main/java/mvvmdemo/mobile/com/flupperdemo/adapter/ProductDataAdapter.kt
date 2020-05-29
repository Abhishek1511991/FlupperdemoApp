package mvvmdemo.mobile.com.flupperdemo.adapter

import android.content.Context
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mvvmdemo.mobile.com.flupperdemo.R
import mvvmdemo.mobile.com.flupperdemo.databinding.ProductListBinding
import mvvmdemo.mobile.com.flupperdemo.interfaces.CustomClickListener
import mvvmdemo.mobile.com.flupperdemo.model.Product

class ProductDataAdapter(mContext: Context, productList:ArrayList<Product>):RecyclerView.Adapter<ProductDataAdapter.ProductViewHolder>(),
    CustomClickListener {

    var  mContext=mContext
    var productList:ArrayList<Product>?=productList;


    var customClickListener:CustomClickListener?=null;
    interface CustomClickListener {

        fun itemClicked(photoUrl: Product?)
    }

    fun initializeClickListener(customtClick:CustomClickListener)
    {
        this.customClickListener=customtClick
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(mContext,DataBindingUtil.inflate<ProductListBinding>(LayoutInflater.from(parent.getContext()),
            R.layout.product_list, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        if(position <productList?.size!!) {
            productList?.get(position)?.let { holder.bindData(it) }

            holder.dataBindingUtil.setItemClickListener(this)



        }
    }

    override fun getItemCount(): Int {
        if(productList?.size==0)
            return 0
        return productList?.size!!
    }

    fun addProduct(product: Product)
    {
        productList?.add(product)
        notifyDataSetChanged()
    }

    fun setNewProducts(productsList:ArrayList<Product>)
    {
        if(productList?.size!!>0)
        {
            productList?.clear()
            notifyDataSetChanged()


            productList?.addAll(productsList)
            notifyDataSetChanged()
        }
        else
        {
            productList?.addAll(productsList)
            notifyDataSetChanged()
        }


    }


    class ProductViewHolder(mContext: Context,dataBinding:ProductListBinding): RecyclerView.ViewHolder(dataBinding.root)
    {
        var  mContext=mContext
        val dataBindingUtil=dataBinding
        fun bindData(product: Product)
        {
            dataBindingUtil?.setProduct(product)
            dataBindingUtil?.executePendingBindings()
            dataBindingUtil.productRegularPrice
                .setPaintFlags(
                    dataBindingUtil?.productRegularPrice.getPaintFlags()
                            or Paint.STRIKE_THRU_TEXT_FLAG
                )


            /*dataBindingUtil?.productDescription.text=product.desc
            dataBindingUtil?.productRegularPrice.text=product.reguler_price.toString()
            dataBindingUtil?.productSalePrice.text=product.sale_price.toString()
            dataBindingUtil?.productTitle.text=product.name*/

            Glide.with(mContext)
                .load(Uri.parse(product.product_photo))
                .centerCrop()
                .error(R.drawable.image_place_holder)
                .override(200, 200)
                .into(
                    dataBindingUtil?.productPhoto
                )


        }
    }

    override fun cardClicked(photoUrl: Product?) {
        customClickListener?.itemClicked(photoUrl)

    }

}