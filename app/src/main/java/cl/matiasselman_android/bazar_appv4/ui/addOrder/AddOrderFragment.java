package cl.matiasselman_android.bazar_appv4.ui.addOrder;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import cl.matiasselman_android.bazar_appv4.R;
import cl.matiasselman_android.bazar_appv4.models.Order;

public class AddOrderFragment extends Fragment {

    private AddOrderViewModel mViewModel;
    private static String TAG = AddOrderFragment.class.getSimpleName();
    private EditText etIdOrder, etClient, etDescription, etDate;
    final Calendar myCalendar = Calendar.getInstance();
    Place adress = null;

    public static AddOrderFragment newInstance() {
        return new AddOrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_order, container, false);
        etIdOrder = root.findViewById(R.id.etIdOrder);
        etClient = root.findViewById(R.id.etClient);
        etDescription = root.findViewById(R.id.etDescription);
        etDate = root.findViewById(R.id.etDate);
        root.findViewById(R.id.btnAddOrder).setOnClickListener(view -> createOrder());
        return root;
    }

    private void createOrder() {
        if (!validateFields()) return;
        createOrderBd(etIdOrder.getText().toString(),
            etClient.getText().toString(),
            etDescription.getText().toString(),
            etDate.getText().toString(),
            adress);
    }

    private void createOrderBd(String idOder, String clientName, String description, String date, Place adress) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        GeoPoint latLng = new GeoPoint(adress.getLatLng().latitude, adress.getLatLng().longitude);
        CollectionReference collectionReference = firestore.collection("Ordenes");
        Order newOrder = new Order(idOder,
            clientName,
            description,
            date,
            adress.getName(),
            adress.getAddress(),
            latLng);

        collectionReference.document(email).collection(date).document().set(newOrder);

        Toast.makeText(getActivity(), "Orden creada exitosamente", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    public boolean validateFields() {
        boolean validacion = true;

        if (etIdOrder.getText().toString().isEmpty()) {
            validacion = false;
            etIdOrder.setError(getString(R.string.login_error_no_titulo));
        }
        if (etClient.getText().toString().isEmpty()) {
            validacion = false;
            etClient.setError(getString(R.string.login_error_no_cliente));
        }
        if (etDescription.getText().toString().isEmpty()) {
            validacion = false;
            etDescription.setError(getString(R.string.login_error_no_descripcion));
        }
        if (etDate.getText().toString().isEmpty()) {
            validacion = false;
            etDate.setError(getString(R.string.login_error_no_fecha));
        }

        if(adress == null){
            validacion = false;
            Toast.makeText(getActivity(), "Debe ingresar la direccion", Toast.LENGTH_LONG).show();
        }

        return validacion;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddOrderViewModel.class);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        etDate.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
            getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                Log.i(TAG, "Id: " + place.getId());
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Address: " + place.getAddress());
                Log.i(TAG, "LatLng: " + place.getLatLng().toString());
                adress = place;
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "Ha ocurrido un error: " + status);
                adress = null;
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

}