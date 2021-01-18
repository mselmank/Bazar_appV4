package cl.matiasselman_android.bazar_appv4.ui.orderList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrderListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}