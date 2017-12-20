package ga.discoveryandlost.discoveryandlost.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flaviofaria.kenburnsview.KenBurnsView;

import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.activity.FindItemActivity;
import ga.discoveryandlost.discoveryandlost.activity.SellListActivity;
import ga.discoveryandlost.discoveryandlost.obj.User;


/**
 * Created by tw on 2017-10-28.
 */
public class SellItemFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;
    private User user;

    private KenBurnsView kenBurnsView;
    private CardView cv_sell;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        if(getArguments() != null) {
            user = (User)getArguments().getSerializable("user");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // UI
        view = inflater.inflate(R.layout.fragment_sell_item, container, false);
        context = container.getContext();

        init();

        return view;

    }


    public void init(){

        kenBurnsView = (KenBurnsView)view.findViewById(R.id.image);
        kenBurnsView.setImageResource(R.drawable.sell_bg);

        cv_sell = (CardView) view.findViewById(R.id.cv_sell);
        setCardButtonOnTouchAnimation(cv_sell);
        cv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellListActivity.class);
                startActivity(intent);
            }
        });

    }


}
