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
import ga.discoveryandlost.discoveryandlost.activity.CameraActivity;
import ga.discoveryandlost.discoveryandlost.obj.User;


/**
 * Created by tw on 2017-10-28.
 */
public class FindItemFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;
    private User user;

    private KenBurnsView kenBurnsView;
    private CardView cv_find;

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
        view = inflater.inflate(R.layout.fragment_find_item, container, false);
        context = container.getContext();

        init();

        return view;

    }


    public void init(){

        kenBurnsView = (KenBurnsView)view.findViewById(R.id.image);
        kenBurnsView.setImageResource(R.drawable.loading);

        cv_find = (CardView) view.findViewById(R.id.cv_find);
        setCardButtonOnTouchAnimation(cv_find);
        cv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
            }
        });

    }


}
