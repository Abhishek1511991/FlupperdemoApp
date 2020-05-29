package mvvmdemo.mobile.com.flupperdemo

import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import mvvmdemo.mobile.com.flupperdemo.adapter.ProductDataAdapter
import mvvmdemo.mobile.com.flupperdemo.databinding.ActivityMainBinding
import mvvmdemo.mobile.com.flupperdemo.fragments.ImageFullScreenDialog
import mvvmdemo.mobile.com.flupperdemo.model.Product
import mvvmdemo.mobile.com.flupperdemo.utility.SwipeController
import mvvmdemo.mobile.com.flupperdemo.utility.SwipeControllerActions
import mvvmdemo.mobile.com.flupperdemo.view_model.ProductRecordViewModel


class MainActivity : AppCompatActivity() {

    var productRecordViewModel:ProductRecordViewModel?=null
    var activityMainBinding:ActivityMainBinding?=null
    var productDataAdapter: ProductDataAdapter?=null
    var productList:ArrayList<Product>?=null;
    var swipeController: SwipeController? = null  //https://github.com/FanFataL/swipe-controller-demo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding=DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        val recyclerView = activityMainBinding?.viewEmployees
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.setHasFixedSize(true)

        productRecordViewModel = ViewModelProvider(this).get(ProductRecordViewModel::class.java)
        productList=ArrayList<Product>();
        productDataAdapter = ProductDataAdapter(this@MainActivity,productList!!)
        productDataAdapter?.initializeClickListener(object:ProductDataAdapter.CustomClickListener
        {
            override fun itemClicked(photoUrl: Product?) {
                ImageFullScreenDialog().display(supportFragmentManager,photoUrl?.product_photo!!)

            }

        })
        recyclerView!!.adapter = productDataAdapter

        swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                productRecordViewModel?.delete(productDataAdapter?.productList?.removeAt(position))

            }

            override fun onLeftClicked(position: Int) {
                Toast.makeText(this@MainActivity,"Left Click",Toast.LENGTH_SHORT).show()
            }
        })

        val itemTouchhelper = ItemTouchHelper(swipeController!!)
        itemTouchhelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                swipeController!!.onDraw(c)
            }
        })



        getAllProduct()
    }

    fun getAllProduct()
    {
        productRecordViewModel?.getAllProducts()?.observe(
            this, object : Observer<ArrayList<Product>> {
                override fun onChanged(@Nullable products: ArrayList<Product>) {
                    if(products?.size>0)
                        productDataAdapter?.setNewProducts(products as ArrayList<Product>)
                }
            })
    }

}
