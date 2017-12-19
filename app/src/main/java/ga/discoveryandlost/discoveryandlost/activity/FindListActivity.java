package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.adapter.LostListCustomAdapter;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.obj.DalLost;
import ga.discoveryandlost.discoveryandlost.util.OnAdapterSupport;

public class FindListActivity extends BaseActivity implements OnAdapterSupport {

    DalItem item;
    ArrayList<DalLost> list;

    private TextView tv_msg;

    // Recycle View
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    private LostListCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_list);

        Intent intent = getIntent();

        item = (DalItem)intent.getSerializableExtra("item");
        list = (ArrayList<DalLost>)intent.getSerializableExtra("list");

        init();

    }

    private void init(){

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindListActivity.this.finish();
            }
        });

        tv_msg = (TextView)findViewById(R.id.tv_msg);

        if(list.size() > 0){
            tv_msg.setVisibility(View.GONE);
        }else{
            tv_msg.setVisibility(View.VISIBLE);
        }

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        makeList();

    }

    public void makeList(){

        if(list.size() > 0){
            tv_msg.setVisibility(View.GONE);
        }else{
            tv_msg.setVisibility(View.VISIBLE);
            setFadeInAnimation(tv_msg);
        }

        adapter = new LostListCustomAdapter(getApplicationContext(), list, item, rv, this);

        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void showView() {

    }

    @Override
    public void hideView() {

    }

    @Override
    public void redirectActivityForResult(Intent intent) {
        startActivityForResult(intent, 0);
    }

    @Override
    public void redirectActivity(Intent intent) {
        startActivity(intent);
    }

}
