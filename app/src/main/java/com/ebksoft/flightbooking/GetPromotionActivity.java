package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ebksoft.flightbooking.model.ResponseObj.GetSocialResObj;
import com.ebksoft.flightbooking.model.SocialModel;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;

import java.util.HashMap;

//import com.facebook.share.widget.LikeView;

/**
 * Created by chauminhnhut on 3/27/16.
 */
public class GetPromotionActivity extends BaseActivity implements View.OnClickListener {

    //    private LikeView likeView;
    private SocialModel fbModel, gooMoDel;
    private View viewContainner;

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

        viewContainner = findViewById(R.id.viewContainner);
    }

    @Override
    protected void loadData() {

        if (CommonUtils.isNetWorkAvailable(this))
            getSocial();
        else
            CommonUtils.showToastNoInternetConnecton(this);



    }

    private void getSocial() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);

        AppRequest.getSocial(this, params, true, new DataRequestCallback<GetSocialResObj>() {
            @Override
            public void onResult(GetSocialResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();


                if (null != result) {
                    if (result.status.equals("0")) {

                        for (int i = 0; i < result.data.size(); i++) {
                            if (result.data.get(i).Type == 1) {
                                fbModel = result.data.get(i);
                            } else {
                                gooMoDel = result.data.get(i);
                            }
                        }

                        viewContainner.setVisibility(View.VISIBLE);

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

        if (R.id.rlFb == view.getId()) {
            if (fbModel != null)
                startActivity(CommonUtils.getOpenFacebookIntent(this, fbModel));
        }

        if (R.id.rlGooglePlus == view.getId()) {
            if (gooMoDel != null)
                startActivity(CommonUtils.getOpenGooglePlusIntent(this, gooMoDel));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "OnActivityResult...");
    }
}
