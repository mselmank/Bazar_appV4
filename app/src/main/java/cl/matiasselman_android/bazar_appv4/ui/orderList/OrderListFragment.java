package cl.matiasselman_android.bazar_appv4.ui.orderList;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cl.matiasselman_android.bazar_appv4.R;
import cl.matiasselman_android.bazar_appv4.models.Order;

public class OrderListFragment extends Fragment {

    private OrderListViewModel orderListViewModel;
    private ListOrderRecyclerViewAdapter orderListAdapter;
    private EditText etDate;
    private String email = "";

    final Calendar myCalendar = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        orderListViewModel = new ViewModelProvider(this).get(OrderListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_order_list, container, false);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        orderListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        etDate = root.findViewById(R.id.etDate);

        Context context = root.getContext();
        RecyclerView recyclerView = root.findViewById(R.id.listOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        root.findViewById(R.id.btnFilter).setOnClickListener(view -> orderListViewModel.getOrderList(etDate.getText().toString(), email));

        orderListViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> data) {
                orderListAdapter = new ListOrderRecyclerViewAdapter(data);
                recyclerView.setAdapter(orderListAdapter);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateLabel();

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

        orderListViewModel.getOrderList(etDate.getText().toString(), email);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }
}