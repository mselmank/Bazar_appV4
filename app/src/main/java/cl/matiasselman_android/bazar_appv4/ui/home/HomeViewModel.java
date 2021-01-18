package cl.matiasselman_android.bazar_appv4.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    private MutableLiveData<String> mEmail;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Bienvenido, elige tu opcion en el menu lateral");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mEmail = new MutableLiveData<>();
        assert user != null;
        mEmail.setValue(user.getEmail());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getEmail() {
        return mEmail;
    }
}