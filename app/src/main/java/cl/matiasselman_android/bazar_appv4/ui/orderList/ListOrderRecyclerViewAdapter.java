package cl.matiasselman_android.bazar_appv4.ui.orderList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Locale;

import cl.matiasselman_android.bazar_appv4.R;
import cl.matiasselman_android.bazar_appv4.models.Order;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListOrderRecyclerViewAdapter extends RecyclerView.Adapter<ListOrderRecyclerViewAdapter.OrderListViewHolder> {

    private final List<Order> mValues;
    private Context context;

    public ListOrderRecyclerViewAdapter(List<Order> items) {
        mValues = items;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order_list_item, parent, false);
        context = parent.getContext();
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdClient.setText(mValues.get(position).getIdOder() + " - " + mValues.get(position).getClientName());
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mDescription.setText(mValues.get(position).getDescription());
        holder.mAddress.setText(mValues.get(position).getAddress());
        holder.mOpenMap.setOnClickListener(v -> openMap(mValues.get(position).getGeoPoint(), mValues.get(position).getAddress()));
    }

    private void openMap(GeoPoint geoPoint, String mMarkerName) {
        String urlAddress = "http://maps.google.com/maps?q=" + geoPoint.getLatitude() + "," + geoPoint.getLongitude() + "(" + mMarkerName + ")&iwloc=A&hl=es";
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=" + mMarkerName, geoPoint.getLatitude(), geoPoint.getLongitude());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdClient;
        public final TextView mDate;
        public final TextView mDescription;
        public final TextView mAddress;
        public final Button mOpenMap;
        public Order mItem;

        public OrderListViewHolder(View view) {
            super(view);
            mView = view;
            mIdClient = view.findViewById(R.id.tvIdClient);
            mDate = view.findViewById(R.id.tvDate);
            mDescription = view.findViewById(R.id.tvDescription);
            mAddress = view.findViewById(R.id.tvAddress);
            mOpenMap = view.findViewById(R.id.btnOpenMap);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescription.getText() + "'";
        }
    }
}