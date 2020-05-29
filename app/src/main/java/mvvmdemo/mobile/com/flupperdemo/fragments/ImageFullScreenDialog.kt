package mvvmdemo.mobile.com.flupperdemo.fragments


import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import mvvmdemo.mobile.com.flupperdemo.R


class ImageFullScreenDialog :DialogFragment()
{
    val TAG = "example_dialog"

    private var toolbar: Toolbar? = null
    var imageUrl:String?=null;

    fun display(fragmentManager: FragmentManager?,imageUrl:String): ImageFullScreenDialog? {

        val  bundle=Bundle()
        bundle?.putString("url",imageUrl)
        val exampleDialog = ImageFullScreenDialog()
        exampleDialog.arguments=bundle
        fragmentManager?.let { exampleDialog.show(it, TAG) }
        return exampleDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.AppTheme_FullScreenDialog
        )
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.getWindow()?.setLayout(width, height)
            dialog.getWindow()?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.full_screen_image_dialog, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view?.let { super.onViewCreated(it, savedInstanceState) }
        toolbar?.setNavigationOnClickListener({ v -> dismiss() })
       // toolbar?.setTitle("Some Title")
        //toolbar?.inflateMenu(R.menu.example_dialog)
        /*toolbar?.setOnMenuItemClickListener({ item ->
            dismiss()
            true
        })*/


        val imageView=view.findViewById<AppCompatImageView>(R.id.show_image)
        imageUrl=arguments?.getString("url")

        context?.let {
            Glide.with(it)
                .load(Uri.parse(imageUrl))
                .centerCrop()
                .error(R.drawable.image_place_holder)
                .override(200, 200)
                .into(imageView)
        }


    }
}