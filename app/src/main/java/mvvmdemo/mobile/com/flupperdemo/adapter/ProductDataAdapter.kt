package mvvmdemo.mobile.com.flupperdemo.adapter

import android.content.Context
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mvvmdemo.mobile.com.flupperdemo.R
import mvvmdemo.mobile.com.flupperdemo.databinding.ProductListBinding
import mvvmdemo.mobile.com.flupperdemo.interfaces.CustomClickListener
import mvvmdemo.mobile.com.flupperdemo.model.Colors
import mvvmdemo.mobile.com.flupperdemo.model.Product
import mvvmdemo.mobile.com.flupperdemo.model.Store
import java.io.File

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
        return ProductViewHolder(mContext,DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
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
                .setPaintFlags(dataBindingUtil?.productRegularPrice.getPaintFlags()
                            or Paint.STRIKE_THRU_TEXT_FLAG)


            if(URLUtil.isValidUrl(product.product_photo))
            {
                Glide.with(mContext)
                    .load(Uri.parse(product.product_photo))
                    .placeholder(R.drawable.shimmer)
                    .centerCrop()
                    .error(R.drawable.error_img)
                    .override(200, 200)
                    .into(
                        dataBindingUtil?.productPhoto
                    )
            }
            else{
                Glide.with(mContext)
                    .load(File(product.product_photo))
                    .placeholder(R.drawable.shimmer)
                    .centerCrop()
                    .error(R.drawable.error_img)
                    .override(200, 200)
                    .into(
                        dataBindingUtil?.productPhoto
                    )
            }




            var colorArray=if(product?.colors?.size>0) product.colors else ArrayList<Colors>()

            val productColorAdapter=ProductColorAdapter(colorArray)
            dataBindingUtil.setColorListAdapter(productColorAdapter)

            var storeArray=if(product?.stores?.size>0) product.stores else ArrayList<Store>()
            val productStoreAdapter=ProductStoreAdapter(storeArray)
            dataBindingUtil.setStoreListAdapter(productStoreAdapter)


        }
    }

    override fun cardClicked(photoUrl: Product?) {
        customClickListener?.itemClicked(photoUrl)

    }

}