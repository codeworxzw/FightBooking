package com.ebksoft.flightbooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ebksoft.flightbooking.utils.CommonUtils;

//import com.facebook.share.widget.LikeView;

/**
 * Created by chauminhnhut on 3/27/16.
 */
public class GetPromotionActivity extends BaseActivity implements View.OnClickListener {

//    private LikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_promotion_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {
        initTitle(getString(R.string.title_get_promotion));
        initButtonBack();

//        likeView = (LikeView)findViewById(R.id.likeView);
//        likeView.setObjectIdAndType(
//                "https://www.facebook.com/AndroidProgrammerGuru",
//                LikeView.ObjectType.PAGE);
//
//        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
//        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);


        findViewById(R.id.rlFb).setOnClickListener(this);
        findViewById(R.id.rlGooglePlus).setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {

        if (R.id.rlFb == view.getId()) {
            startActivity(CommonUtils.getOpenFacebookIntent(this));
        }

        if (R.id.rlGooglePlus == view.getId()) {
            startActivity(CommonUtils.getOpenGooglePlusIntent(this));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "OnActivityResult...");
    }
}
