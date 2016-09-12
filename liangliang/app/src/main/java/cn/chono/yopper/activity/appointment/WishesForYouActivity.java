package cn.chono.yopper.activity.appointment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.WishesForYouAdapter;
import cn.chono.yopper.utils.CheckUtil;

/**
 * 征婚寄语
 * Created by cc on 16/3/25.
 */
public class WishesForYouActivity extends MainFrameActivity implements WishesForYouAdapter.OnItemSendClickLitener {

    private ListView listView;

    private WishesForYouAdapter adapter;

    private Resources rcs;

    private String content;

    private EditText appointment_write_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.personals_send_message_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("content")) {
            content = bundle.getString("content");
        }

        initView();

    }

    private void initView() {

        rcs = getResources();

        getTvTitle().setText("对他/她的寄语");


        TextView tvOption = gettvOption();
        tvOption.setVisibility(View.VISIBLE);
        tvOption.setText("确认");
        tvOption.setTextColor(getResources().getColor(R.color.color_ff7462));

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getOptionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = appointment_write_et.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putSerializable("content", content);

                Intent intent = new Intent(WishesForYouActivity.this, MarriageSeekingActivity.class);
                intent.putExtras(bundle);


                setResult(YpSettings.MOVEMENT_SEND_MESSAGE, intent);
                finish();

            }
        });


        appointment_write_et = (EditText) findViewById(R.id.appointment_write_et);

        if(!CheckUtil.isEmpty(content)){
            appointment_write_et.setText(content);
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new WishesForYouAdapter(this);
        adapter.setOnItemClickLitener(this);
        listView.setAdapter(adapter);

        String[] send_message = rcs.getStringArray(R.array.send_message);

        adapter.setData(send_message);
        adapter.setSelectItem(content);

    }

    @Override
    public void onItemClickLitener(int position, String contentStr) {
        this.content = contentStr;
        adapter.setSelectItem(content);
        appointment_write_et.setText(content);
        appointment_write_et.requestFocus();  //这是关键
    }
}
