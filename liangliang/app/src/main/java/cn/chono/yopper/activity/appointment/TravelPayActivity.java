package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.RadioAdapter;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.TravelPay;
import cn.chono.yopper.utils.DbHelperUtils;

/**
 * 旅行费用
 * Created by cc on 16/3/22.
 */
public class TravelPayActivity extends MainFrameActivity {

    private GridView travel_the_way;
    private ListView travel_cost;


    private TravelPay mTravelPay;

    private Resources res;

    private RadioAdapter thewayAdapter, costAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.travel_pay_activity);

        res = getResources();
        mTravelPay = new TravelPay();
        mTravelPay = (TravelPay) getIntent().getExtras().getSerializable("mTravelPay");
        initView();
        setTheWayLobleData();
        setPayLobleData();

    }

    private void initView() {


        getTvTitle().setText("出行方式和费用");

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        travel_the_way = (GridView) findViewById(R.id.travel_the_way);
        travel_cost = (ListView) findViewById(R.id.travel_cost);


        thewayAdapter = new RadioAdapter(this);
        costAdapter = new RadioAdapter(this);
        thewayAdapter.setOnItemClickLitener(new TheWay());
        costAdapter.setOnItemClickLitener(new EMU());

        travel_the_way.setAdapter(thewayAdapter);
        travel_cost.setAdapter(costAdapter);

    }

    private void setTheWayLobleData() {
        String[] theWay = res.getStringArray(R.array.travel_the_way);
        thewayAdapter.setData(theWay);

        if (null != mTravelPay)
            thewayAdapter.setSelectItem(mTravelPay.getTheWay());


    }


    private void setPayLobleData() {

        int userid = LoginUser.getInstance().getUserId();

        int sex = 0;
        BaseUser baseUser = DbHelperUtils.getBaseUser(userid);

        if (null != baseUser) {
            sex = baseUser.getSex();

        }


        if (1 == sex) {//男

            String[] travel_male = res.getStringArray(R.array.travel_male);
            costAdapter.setData(travel_male);
            costAdapter.setSelectItem(mTravelPay.getPay());

        } else {//女

            String[] travel_female = res.getStringArray(R.array.travel_female);
            costAdapter.setData(travel_female);
            costAdapter.setSelectItem(mTravelPay.getPay());


        }


    }

    private class TheWay implements RadioAdapter.OnItemTravelClickLitener {

        @Override
        public void onItemClickLitener(int position, String context) {

            mTravelPay.setTheWay(context);
            mTravelPay.setPay("");
            thewayAdapter.setSelectItem(context);
            setPayLobleData();

        }
    }

    private class EMU implements RadioAdapter.OnItemTravelClickLitener {

        @Override
        public void onItemClickLitener(int position, String context) {
            mTravelPay.setPay(context);
            costAdapter.setSelectItem(context);
        }
    }

    @Override
    public void finish() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("mTravelPay", mTravelPay);

        Intent intent = new Intent(TravelPayActivity.this, TravelActivity.class);
        intent.putExtras(bundle);
        setResult(YpSettings.TRAVE_PAY, intent);

        super.finish();
    }
}
