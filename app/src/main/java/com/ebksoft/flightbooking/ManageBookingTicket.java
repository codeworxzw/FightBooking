package com.ebksoft.flightbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.HistorySearchTrip;
import com.ebksoft.flightbooking.utils.AppApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chauminhnhut on 3/28/16.
 */
public class ManageBookingTicket extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private CustomAdapter adapter;
    private List<HistorySearchTrip> lstTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manage_booking_ticket_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_manage_booking_ticket));
        initButtonBack();

        listView = (ListView)findViewById(R.id.listView);
    }

    @Override
    protected void loadData() {

        lstTrip = AppApplication.getInstance(this).historySearchTrips;
        adapter = new CustomAdapter(this, lstTrip);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("index", i);
        startActivity(intent);
    }

    public class CustomAdapter extends BaseAdapter {

        private List<HistorySearchTrip> data;
        private LayoutInflater inflater;
        private Context context;

        public CustomAdapter(Context c, List<HistorySearchTrip> news) {
            this.data = news;
            this.inflater = LayoutInflater.from(c);
            this.context = c;
        }

        @Override
        public int getCount() {
            if (null==data)
                return 0;
            return data.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;

            if (null == view) {
                view = this.inflater.inflate(R.layout.layout_manage_booking_row_item, viewGroup, false);

                viewHolder = new ViewHolder();
                viewHolder.tvPlaceFrom = (TextView) view.findViewById(R.id.tvPlaceFrom);
                viewHolder.tvPlaceTo = (TextView) view.findViewById(R.id.tvPlaceTo);
                viewHolder.tvWayTime = (TextView) view.findViewById(R.id.tvWayTime);
                viewHolder.tvPassenger = (TextView) view.findViewById(R.id.tvPassenger);


                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            HistorySearchTrip trackModel = data.get(i);

            viewHolder.tvPlaceFrom.setText(trackModel.From);
            viewHolder.tvPlaceTo.setText(trackModel.To);
            viewHolder.tvWayTime.setText(trackModel.WayTime);
            viewHolder.tvPassenger.setText(trackModel.Passenger);


            return view;
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }

    public class ViewHolder {
        public TextView tvPlaceFrom;
        public TextView tvPlaceTo;
        public TextView tvWayTime;
        public TextView tvPassenger;
    }



}
