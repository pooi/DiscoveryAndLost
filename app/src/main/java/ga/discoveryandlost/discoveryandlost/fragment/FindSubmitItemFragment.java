package ga.discoveryandlost.discoveryandlost.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;

import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.util.RegisterSelectListener;

public class FindSubmitItemFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;

    private LinearLayout li_qa;
    private Button submitBtn;

    private DalItem item;

    private RegisterSelectListener selectListener;


    private int position;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        if(getArguments() != null) {
            position = getArguments().getInt("position");
            item = (DalItem)getArguments().getSerializable("item");
            selectListener = (RegisterSelectListener)getArguments().getSerializable("listener");
//            qaView = (View)getArguments().getSerializable("view");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find_submit, container, false);
        context = container.getContext();

        initUI();

        return view;
    }

    private void initUI(){

        li_qa = (LinearLayout)view.findViewById(R.id.li_qa);
        submitBtn = (Button)view.findViewById(R.id.btn_submit);

//        qaView = item.getQueryView(position);
//        li_qa.addView(qaView);

//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(selectListener != null) {
//                    selectListener.select(position, qaView, -1, false);
//                }
//            }
//        });


        updateContent();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.submit();
            }
        });

    }

    public void updateContent(){

        li_qa.removeAllViews();

        for(int i=0; i<item.getSize(); i++){

            View v = item.getQueryView(getLayoutInflater(), i);
            MaterialEditText editText = (MaterialEditText)v.findViewById(R.id.edit_answer);
            editText.setEnabled(false);
            li_qa.addView(v);

        }

    }




}
