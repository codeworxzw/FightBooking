package com.ebksoft.flightbooking;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.Contact;
import com.ebksoft.flightbooking.model.ResponseObj.GetHotlinesResObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauminhnhut on 3/27/16.
 */
public class HotlineBookingActivity extends BaseActivity implements AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private List<Contact> contactList;
    private ListView listView;
    private CustomAdapter adapter;

    private static final int PERMISSION_REQUEST_CALL_PHONE = 0;

    private String selectedPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hotline_booking_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_hotline_booking));
        initButtonBack();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

    }

    @Override
    protected void loadData() {

        contactList = new ArrayList<>();
        adapter = new CustomAdapter(this, contactList);
        listView.setAdapter(adapter);

        if (CommonUtils.isNetWorkAvailable(this))
            getHotlines();
        else
            CommonUtils.showToastNoInternetConnecton(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Contact contact = (Contact) adapterView.getItemAtPosition(i);
        selectedPhone = contact.Phone.replace(".", "");

        openCallphoneWithAskPermission();
    }

    private void getHotlines() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);

        AppRequest.getHotlines(this, params, true, new DataRequestCallback<GetHotlinesResObj>() {
            @Override
            public void onResult(GetHotlinesResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();


                if (null != result) {
                    if (result.status.equals("0")) {

                        for (int i = 0; i < result.data.size(); i++) {
                            contactList.add(result.data.get(i));
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    private void openCallphoneWithAskPermission() {

        // Check if the Call phone permission has been granted

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhone));
            startActivity(intent);

        } else {
            // Permission is missing and must be requested.
            requestCallPhonePermission();
        }
    }

    private void requestCallPhonePermission() {

        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {

            //Show the reason to using this permisson
            showDialog();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCallphoneWithAskPermission();

            } else {
                CommonUtils.showToast(this, "Access deny");

            }
        }
    }

    private void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        ActivityCompat.requestPermissions(HotlineBookingActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                PERMISSION_REQUEST_CALL_PHONE);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        String title = "Thông báo";
        String message = "Bạn phải cấp quyền cho ứng dụng để có thể sử dụng tính năng gọi. Tiếp tục ?";

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(message).setPositiveButton("Tiếp tục", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
    }

    public class CustomAdapter extends BaseAdapter {

        private List<Contact> contactList;
        private LayoutInflater inflater;
        private Context context;

        public CustomAdapter(Context c, List<Contact> contacts) {
            this.inflater = LayoutInflater.from(c);
            this.context = c;
            this.contactList = contacts;
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;

            if (null == view) {
                view = this.inflater.inflate(R.layout.layout_hotline_booking_row_item, viewGroup, false);

                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Contact c = contactList.get(i);

            viewHolder.title.setText(c.Title + ":");
            viewHolder.name.setText(c.Phone + " - " + c.Name);

            ImageUtils.load2(context, viewHolder.imageView, c.Image);

            return view;
        }

        @Override
        public Contact getItem(int i) {
            return contactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }

    public class ViewHolder {
        public TextView title;
        public TextView name;
        public ImageView imageView;
    }

}
