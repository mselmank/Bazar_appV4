package cl.matiasselman_android.bazar_appv4.ui.orderMaps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cl.matiasselman_android.bazar_appv4.models.Order;

public class MapsListViewModel extends ViewModel {
    private MutableLiveData<List<Order>> mListOrder = new MutableLiveData<>();

    public MapsListViewModel() {
    }

    public LiveData<List<Order>> getOrders() {
        return mListOrder;
    }

    public void getOrderList(String filter, String email) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("Ordenes").document(email).collection(filter);

        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Order> mData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String idOder = document.getString("idOder");
                            String clientName = document.getString("clientName");
                            String description = document.getString("description");
                            String date = document.getString("date");
                            String idPlace = document.getString("idPlace");
                            String placeName = document.getString("placeName");
                            String address = document.getString("address");
                            GeoPoint geoPoint = document.getGeoPoint("geoPoint");
                            mData.add(new Order(idOder,
                                    clientName,
                                    description,
                                    date,
                                    idPlace,
                                    placeName,
                                    address,
                                    geoPoint));
                        }
                        mListOrder.postValue(mData);
                    } else {
                        Log.e("Error", "Error getting documents: ", task.getException());
                        mListOrder.postValue(null);
                    }
                });

        /*val retroInstance = RetroInstance.getRetroInstance(context).create(
                RetroService::class.java
        )
        val call = retroInstance.getDataFromAPICategory(input)
        call.enqueue(object : Callback<ApiCategoryResponse> {
            override fun onResponse(
                    call: Call<ApiCategoryResponse>,
            response: Response<ApiCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    val destination = response.body()
                    destination?.let {
                        recyclerLisDataCategoriaLevel1.postValue(response.body()!!.payload.data)
                    }
                } else {
                    Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT)
                            .show()
                    recyclerLisDataCategoriaLevel1.postValue(null)
                }
            }

            override fun onFailure(call: Call<ApiCategoryResponse>, t: Throwable) {
                t.printStackTrace()
                recyclerLisDataCategoriaLevel1.postValue(null)
            }
        })*/
    }
}