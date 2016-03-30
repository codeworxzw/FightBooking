package com.ebksoft.flightbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.News;
import com.ebksoft.flightbooking.model.ResponseObj.BookingResultResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.flightbooking.model.ResponseObj.ListNewsResObj;
import com.ebksoft.flightbooking.model.TicketInfo;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.ImageUtils;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauminhnhut on 3/28/16.
 */
public class ListNewsActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private CustomAdapter adapter;
    private List<News> lstNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_news_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_list_news));
        initButtonBack();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

    }

    @Override
    protected void loadData() {

        lstNews = new ArrayList<>();
        adapter = new CustomAdapter(this, lstNews);
        listView.setAdapter(adapter);

        getListNews(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        News news = (News)adapter.getItem(i);

        Intent intent = new Intent(this, DetailNewsActivity.class);
        intent.putExtra("ID", news.ID);

        startActivity(intent);
    }

    private void getListNews(int page) {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);
        params.put("page", page);

        AppRequest.getListNews(this, params, true, new DataRequestCallback<ListNewsResObj>() {
            @Override
            public void onResult(ListNewsResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();


                if (null != result) {
                    if (result.status.equals("0")) {

                        for (int i = 0; i < result.data.size(); i++) {
                            lstNews.add(result.data.get(i));
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

    public class CustomAdapter extends BaseAdapter {

        private List<News> data;
        private LayoutInflater inflater;
        private Context context;

        public CustomAdapter(Context c, List<News> news) {
            this.data = news;
            this.inflater = LayoutInflater.from(c);
            this.context = c;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;

            if (null == view) {
                view = this.inflater.inflate(R.layout.layout_news_row_item, viewGroup, false);

                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                viewHolder.desc = (TextView) view.findViewById(R.id.desc);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            News news = data.get(i);

            viewHolder.title.setText(news.Title);
            viewHolder.desc.setText(news.Description);
            ImageUtils.load2(this.context, viewHolder.imageView, news.Image);

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
        public ImageView imageView;
        public TextView title;
        public TextView desc;
    }


}
