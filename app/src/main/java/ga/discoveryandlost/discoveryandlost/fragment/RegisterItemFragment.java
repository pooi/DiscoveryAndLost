package ga.discoveryandlost.discoveryandlost.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.io.File;

import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.activity.CameraActivity;
import ga.discoveryandlost.discoveryandlost.obj.User;


/**
 * Created by tw on 2017-10-28.
 */
public class RegisterItemFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;
    private User user;

    private KenBurnsView kenBurnsView;
    private CardView cv_add;

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
        view = inflater.inflate(R.layout.fragment_register_item, container, false);
        context = container.getContext();

        init();

        return view;

    }


    public void init(){

        kenBurnsView = (KenBurnsView)view.findViewById(R.id.image);
        kenBurnsView.setImageResource(R.drawable.register_bg);

        cv_add = (CardView) view.findViewById(R.id.cv_add);
        setCardButtonOnTouchAnimation(cv_add);
        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllImage();
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
            }
        });

    }

    private void removeAllImage(){

        final File dir = new File(Environment.getExternalStorageDirectory(), "/dal/temp");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if(children != null) {
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }

    }


}
