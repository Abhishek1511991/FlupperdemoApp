package mvvmdemo.mobile.com.flupperdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mvvmdemo.mobile.com.flupperdemo.databinding.ProductColorBinding
import mvvmdemo.mobile.com.flupperdemo.databinding.ProductStoreBinding
import mvvmdemo.mobile.com.flupperdemo.model.Colors
import mvvmdemo.mobile.com.flupperdemo.model.Store

class ProductStoreAdapter (storesList:ArrayList<Store>):RecyclerView.Adapter<ProductStoreAdapter.StoreViewHolder>() {

    var storeList:ArrayList<Store>?=storesList;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductStoreAdapter.StoreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemColorBinding: ProductStoreBinding =ProductStoreBinding.inflate(layoutInflater, parent, false)
        return ProductStoreAdapter.StoreViewHolder(mItemColorBinding)
    }

    override fun onBindViewHolder(holder: ProductStoreAdapter.StoreViewHolder, position: Int) {
        val color: Store = storeList?.get(position)!!
        holder.bindData(color)
    }

    override fun getItemCount(): Int {
        return storeList?.size!!
    }



    class StoreViewHolder(dataBinding: ProductStoreBinding): RecyclerView.ViewHolder(dataBinding.root)
    {
        val storeBindingUtil=dataBinding
        fun bindData(storeData: Store)
        {
            storeBindingUtil?.setStore(storeData)
            storeBindingUtil?.executePendingBindings()
        }
    }
}