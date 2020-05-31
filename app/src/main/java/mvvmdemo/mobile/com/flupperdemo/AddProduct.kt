package mvvmdemo.mobile.com.flupperdemo

import android.Manifest
import android.Manifest.permission
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mvvmdemo.mobile.com.flupperdemo.adapter.ProductColorAdapter
import mvvmdemo.mobile.com.flupperdemo.adapter.ProductStoreAdapter
import mvvmdemo.mobile.com.flupperdemo.database.AppDatabase
import mvvmdemo.mobile.com.flupperdemo.databinding.AddProductBinding
import mvvmdemo.mobile.com.flupperdemo.model.Colors
import mvvmdemo.mobile.com.flupperdemo.model.Product
import mvvmdemo.mobile.com.flupperdemo.model.Store
import mvvmdemo.mobile.com.flupperdemo.utility.CheckResult
import mvvmdemo.mobile.com.flupperdemo.utility.ImageFilePath
import mvvmdemo.mobile.com.flupperdemo.view_model.ClickEvent
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddProduct:AppCompatActivity() {



    var addProductBinding:AddProductBinding?=null
    var productStoreAdapter: ProductStoreAdapter? = null
    var productColorAdapter: ProductColorAdapter? = null
    private var listOfColor: ArrayList<Colors>? = null
    private var listOfStores: ArrayList<Store>? = null
    var addProductActivityClickHandlers:AddProductActivityClickHandlers?=null
    var mCurrentPhotoPath: String? = null;
    private var currentFile: File? = null
    private var mPhotoFile: File? = null
    var product:Product?=null;
    var photoCaptureUri:String?=null
    private var PERMISSION_REQUEST_TYPE = 0

    companion object {

        private var eventViewModel:ClickEvent?=null

        private var PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_PERMISSION_READ_WRITE = 101
        private const val DIALOG_PERMISSION_REASON = 102
        private const val PHOTO_CAPTURE_REQUEST_CODE = 103
        private const val GALLERY_CHOOSE_REQUEST_CODE = 104
        private const val COLOR_PICKER_REQUEST_CODE = 105
        private const val STORE_NAME_REQUEST_CODE = 106
        private const val PRODUCT_SELECT_REQUEST_CODE = 107
        private const val SAVE_UPDTAE_REQUEST_CODE = 108

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addProductBinding=DataBindingUtil.setContentView<AddProductBinding>(this,R.layout.add_product)
        //addProductBinding?.executePendingBindings()


        supportActionBar?.setTitle(getString(R.string.add_product));

        listOfColor = ArrayList<Colors>()
        listOfStores = ArrayList<Store>()

        productColorAdapter = ProductColorAdapter(listOfColor as ArrayList<Colors>)
        addProductBinding?.setColorListAdapter(productColorAdapter)

        productStoreAdapter = ProductStoreAdapter(listOfStores as ArrayList<Store>)
        addProductBinding?.setStoreListAdapter(productStoreAdapter)
        addProductActivityClickHandlers=AddProductActivityClickHandlers()

        val bundle :Bundle ?=intent.extras
        if(bundle!=null)
        {
            product=bundle.getParcelable("data") as Product?
            addProductBinding?.setProduct(product)


            addProductBinding?.productImage?.let {


                Handler().postDelayed(
                    Runnable { Glide.get(this@AddProduct).clearMemory() },
                    0
                )

                AsyncTask.execute(Runnable { Glide.get(this@AddProduct).clearDiskCache() })

                if(URLUtil.isValidUrl(product?.product_photo))
                {
                    Glide.with(this@AddProduct)
                        .load(Uri.parse(product?.product_photo))
                        .placeholder(R.drawable.shimmer)
                        .centerCrop()
                        .circleCrop()
                        .error(R.drawable.error_img)
                        .override(200, 200)
                        .apply(RequestOptions.circleCropTransform())
                        .into(it)

                }
                else
                {
                    Glide.with(this@AddProduct)
                        .load(File(product?.product_photo))
                        .placeholder(R.drawable.shimmer)
                        .centerCrop()
                        .circleCrop()
                        .error(R.drawable.error_img)
                        .override(200, 200)
                        .apply(RequestOptions.circleCropTransform())
                        .into(it)

                }


                addProductBinding?.executePendingBindings()

                if(product?.stores?.size!! >0) {
                    addProductBinding?.storeListAdapter?.storeList?.addAll(product?.stores!!)
                    addProductBinding?.storeListAdapter?.notifyDataSetChanged()
                }

                if(product?.colors?.size!!>0) {
                    addProductBinding?.colorListAdapter?.colorsList?.addAll(product?.colors!!)
                    addProductBinding?.colorListAdapter?.notifyDataSetChanged()
                }
            }

        }
        else
        {
            product=Product(0,"","","","","", ArrayList(),ArrayList())
            addProductBinding?.setProduct(product)
        }

        addProductBinding?.setClickHandler(addProductActivityClickHandlers)

        eventViewModel= ViewModelProvider(this).get(ClickEvent::class.java)

        eventViewModel?.captureEventClickType?.observe(this, Observer {

            Log.e("","")

            when (it) {
                SAVE_UPDTAE_REQUEST_CODE ->
                {
                    product?.name=addProductBinding?.name?.text.toString()
                    product?.desc=addProductBinding?.description?.text.toString()
                    product?.reguler_price=addProductBinding?.regularPrice?.text.toString()
                    product?.sale_price=addProductBinding?.salesPrice?.text.toString()
                    product?.colors= listOfColor as ArrayList<Colors>
                    product?.stores= listOfStores as ArrayList<Store>

                    var msg=CheckResult.checkDataValidation(product!!)
                    if(msg.trim().isEmpty())
                    {
                        GlobalScope.launch {

                            AppDatabase.instance?.DataDao()?.insert(product)
                            finish()
                        }
                    }
                    else
                    {
                        Toast.makeText(this@AddProduct,msg,Toast.LENGTH_SHORT).show()
                    }
                }
                PRODUCT_SELECT_REQUEST_CODE ->  selectDialogForImageCapture()
                COLOR_PICKER_REQUEST_CODE ->    ChooseColorFromPicker()
                STORE_NAME_REQUEST_CODE ->
                {
                    var mBottomSheetDialog:BottomSheetDialog?=null
                    val bottomSheetLayout: View =LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null)
                    bottomSheetLayout.findViewById<View>(R.id.button_close).setOnClickListener { mBottomSheetDialog?.dismiss() }
                    bottomSheetLayout.findViewById<View>(R.id.button_ok).setOnClickListener {

                        var storeNameText: TextInputEditText = bottomSheetLayout.findViewById<TextInputEditText>(R.id.store_name)
                        if(storeNameText?.text.toString().trim().isEmpty())
                            storeNameText?.hint="Store Name should not be blank"
                        else
                        {
                            listOfStores?.add(Store(storeNameText?.text.toString()))
                            productStoreAdapter?.notifyDataSetChanged()
                            mBottomSheetDialog?.dismiss()
                        }
                    }

                    mBottomSheetDialog = BottomSheetDialog(this)
                    mBottomSheetDialog?.setContentView(bottomSheetLayout)
                    mBottomSheetDialog?.show()
                }
                else-> Toast.makeText(this@AddProduct,"Event not matched from your action",Toast.LENGTH_SHORT).show()
            }
        })

    }

    class AddProductActivityClickHandlers  {
        fun OnSaveClicked(view: View?) {eventViewModel?.callEventClickType(SAVE_UPDTAE_REQUEST_CODE)}
        fun clickToAddPhoto(view: View?) {eventViewModel?.callEventClickType(PRODUCT_SELECT_REQUEST_CODE)}
        fun clickToChooseColor(view: View?) {eventViewModel?.callEventClickType(COLOR_PICKER_REQUEST_CODE)}
        fun clickToAddProductStoreName(view: View?) {eventViewModel?.callEventClickType(STORE_NAME_REQUEST_CODE)}

    }

    fun ChooseColorFromPicker()
    {//https://github.com/kristiyanP/colorpicker
        val colorPicker = ColorPicker(this)
        colorPicker.show()
        colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
            override fun onChooseColor(position: Int, colorCode: Int) {
                val color=colorCode
                color?.let { Colors(it) }?.let { listOfColor?.add(it) }
                productColorAdapter?.notifyDataSetChanged()
            }

            override fun onCancel() {

            }

        })
    }



    private fun dispatchGalleryIntent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED){

                PERMISSION_REQUEST_TYPE=1
                requestPermission()
            }
            else{
                chooseImageFromGallery()
            }
        }
        else{
            chooseImageFromGallery()
        }





    }

    fun chooseImageFromGallery()
    {


        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (pickPhoto?.resolveActivity(packageManager) != null)
            startActivityForResult(pickPhoto, GALLERY_CHOOSE_REQUEST_CODE)

    }


    private fun capturePictureIntent() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            if (isExternalStoragePermissionGranted()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(packageManager) != null) { // Create the File where the photo should go
                    try {
                        currentFile = createImageFile()

                        val photoURI = currentFile?.let { FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", it) }
                        photoCaptureUri=photoURI?.toString()
                        if (photoURI != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                                takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }


                            if (takePictureIntent?.resolveActivity(getPackageManager()) != null)
                                startActivityForResult(takePictureIntent, PHOTO_CAPTURE_REQUEST_CODE)
                        }
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            }
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            try {
                showDialog(DIALOG_PERMISSION_REASON)
            } catch (e: java.lang.Exception) {
            }

        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermission()) {
                    PERMISSION_REQUEST_TYPE=0
                    requestPermission()
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result1 =ContextCompat.checkSelfPermission(applicationContext, permission.CAMERA)
        val result2 = ContextCompat.checkSelfPermission(applicationContext,permission.READ_EXTERNAL_STORAGE)
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(permission.CAMERA, permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File? { // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        var image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image;
    }



    private fun isExternalStoragePermissionGranted(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_READ_WRITE)
            false
        }
    }

    private fun setPic(currentPhotoPath: String): String? {
        try {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            val stream1 = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, stream1)
            val byteArray1 = stream1.toByteArray()
            return Base64.encodeToString(byteArray1, Base64.DEFAULT)
        } catch (e1: OutOfMemoryError) {
            var options: BitmapFactory.Options;
            try {
                options = BitmapFactory.Options()
                options?.inSampleSize = 2

                val bitmap1 = BitmapFactory.decodeFile(currentPhotoPath, options)
                val stream1 = ByteArrayOutputStream()
                bitmap1?.compress(Bitmap.CompressFormat.JPEG, 10, stream1)
                val byteArray1 = stream1.toByteArray()
                return Base64.encodeToString(byteArray1, Base64.DEFAULT)
            } catch (e: java.lang.Exception) {
                return "";
            }
        } catch (e: Exception) {
        }
        return ""
    }


    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>, grantResults: IntArray
    )
    {

        if (requestCode == REQUEST_PERMISSION_READ_WRITE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePictureIntent()

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
        else if(requestCode==PERMISSION_REQUEST_CODE)
        {
            if (grantResults.size > 0) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val StorageAccepted =grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && StorageAccepted) {
                    if(PERMISSION_REQUEST_TYPE==1)  chooseImageFromGallery()
                    else capturePictureIntent()

                }
            }
        }
    }


    private fun selectDialogForImageCapture() {



        var mBottomSheetDialog:BottomSheetDialog?=null
        val bottomSheetLayout: View =LayoutInflater.from(this).inflate(R.layout.image_select_option, null)
        bottomSheetLayout.findViewById<View>(R.id.btn_cancel).setOnClickListener { mBottomSheetDialog?.dismiss() }
        bottomSheetLayout.findViewById<View>(R.id.btn_camera).setOnClickListener {
            mBottomSheetDialog?.dismiss()
            capturePictureIntent()
        }

        bottomSheetLayout.findViewById<View>(R.id.btn_gallery).setOnClickListener {
            mBottomSheetDialog?.dismiss()
            dispatchGalleryIntent()
        }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.setContentView(bottomSheetLayout)
        mBottomSheetDialog?.setCanceledOnTouchOutside(false)
        mBottomSheetDialog?.setCancelable(false);
        mBottomSheetDialog?.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentFile?.absolutePath!!)

            if (bitmap != null) {
                addProductBinding?.productImage?.setImageBitmap(bitmap)
                product?.product_photo=photoCaptureUri!!
            } else {
                var bitmap: Bitmap? = getBitmap()
                if (bitmap != null) {
                    addProductBinding?.productImage?.setImageBitmap(bitmap)
                    product?.product_photo=photoCaptureUri!!
                } else
                    Toast.makeText(this@AddProduct,"Capture again",Toast.LENGTH_SHORT).show()


            }

        }
        else if (requestCode == GALLERY_CHOOSE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.getData();
            try {


                mPhotoFile = selectedImage.let { File(getRealPathFromUri(selectedImage)) }
                addProductBinding?.productImage?.let {


                    Handler().postDelayed(
                        Runnable { Glide.get(this@AddProduct).clearMemory() },
                        0
                    )

                    AsyncTask.execute(Runnable { Glide.get(this@AddProduct).clearDiskCache() })

                    if(URLUtil.isValidUrl(mPhotoFile?.absolutePath!!))
                    {
                        Glide.with(this@AddProduct)
                            .load(Uri.parse(ImageFilePath.isDucumentUri(selectedImage!!,this@AddProduct)))
                            .placeholder(R.drawable.shimmer)
                            .centerCrop()
                            .circleCrop()
                            .error(R.drawable.error_img)
                            .override(200, 200)
                            .apply(RequestOptions.circleCropTransform())
                            .into(it)

                        product?.product_photo=ImageFilePath.isDucumentUri(selectedImage!!,this@AddProduct)
                    }
                    else
                    {
                        Glide.with(this@AddProduct)
                            .load(mPhotoFile)
                            .placeholder(R.drawable.shimmer)
                            .centerCrop()
                            .circleCrop()
                            .error(R.drawable.error_img)
                            .override(200, 200)
                            .apply(RequestOptions.circleCropTransform())
                            .into(it)


                        product?.product_photo=mPhotoFile?.absolutePath!!
                    }

                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

        }

    }

    private fun getPathFromURI(uri: Uri):String {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        var selectionArgs: Array<String>?=null
        if (path?.contains("/document/image:")!!) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri))
            }
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor?.moveToFirst()!!) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                return cursor.getString(columnIndex)
            }

            cursor.close()
        } catch (e: Exception) { }
        return ""
    }



    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj =arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index: Int = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)!!
            cursor?.moveToFirst()
            cursor?.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }

    }

    private fun getBitmap(): Bitmap? {
        var options: BitmapFactory.Options;
        try {
            val bitmap = BitmapFactory.decodeFile(currentFile?.absolutePath!!)
            return bitmap;
        } catch (e: OutOfMemoryError) {
            try {
                options = BitmapFactory.Options()
                options?.inSampleSize = 2

                val bitmap = BitmapFactory.decodeFile(currentFile?.absolutePath!!, options)
                return bitmap
            } catch (e: java.lang.Exception) {
                return null;
            }
        }
    }

}

