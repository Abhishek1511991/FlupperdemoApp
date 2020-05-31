package mvvmdemo.mobile.com.flupperdemo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClickEvent:ViewModel() {


    //private val uploadAuction: SingleLiveEvent<Int> = SingleLiveEvent<Int>()
   // private val activityTypeEvent = MutableLiveData<Int?>()

    val activityTypeEvent: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val captureEventClickType: LiveData<Int?>
        get() = activityTypeEvent

    fun callEventClickType(value:Int) {
        activityTypeEvent.value = value
    }

}