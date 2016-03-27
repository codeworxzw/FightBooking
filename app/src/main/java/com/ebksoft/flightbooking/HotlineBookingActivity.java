package com.ebksoft.flightbooking;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.Contact;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chauminhnhut on 3/27/16.
 */
public class HotlineBookingActivity extends BaseActivity implements AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private List<Contact> contactList;
    private ListView listView;

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

        initContactList();

        CustomAdapter adapter = new CustomAdapter(this, contactList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Contact contact = (Contact) adapterView.getItemAtPosition(i);
        selectedPhone = contact.phone.replace(".", "");

        openCallphoneWithAskPermission();
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

    private void initContactList() {
        contactList = new ArrayList<>();

        Contact p1 = new Contact();
        p1.title = "Hotline 1";
        p1.phone = "0169.7515.825";
        p1.name = "gặp Nhựt";

        Contact p2 = new Contact();
        p2.title = "Hotline 2";
        p2.phone = "0915.38.55.57";
        p2.name = "gặp Hiền";

        Contact p3 = new Contact();
        p3.title = "Hotline 3";
        p3.phone = "0915.38.55.58";
        p3.name = "gặp Lợi";

        Contact p4 = new Contact();
        p4.title = "Hotline 4";
        p4.phone = "0915.38.55.59";
        p4.name = "gặp Hương";

        contactList.add(p1);
        contactList.add(p2);
        contactList.add(p3);
        contactList.add(p4);
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

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Contact c = contactList.get(i);

            viewHolder.title.setText(c.title + ":");
            viewHolder.name.setText(c.phone + " - " + c.name);

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
    }

}
