package ga.discoveryandlost.discoveryandlost.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.util.RegisterSelectListener;

public class RegisterDetailFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;

    private LinearLayout li_qa;
    private Button nextBtn;

    private DalItem item;
    private View qaView;

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

        view = inflater.inflate(R.layout.fragment_register_detail, container, false);
        context = container.getContext();

        initUI();

        return view;
    }

    private void initUI(){

        li_qa = (LinearLayout)view.findViewById(R.id.li_qa);
        nextBtn = (Button)view.findViewById(R.id.nextBtn);

        qaView = item.getQueryView(getLayoutInflater(), position);
        li_qa.addView(qaView);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectListener != null) {
                    selectListener.select(position, qaView, -1, false);
                }
            }
        });

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                checkNextEnable();
//            }
//        });
//
//        if(isEditMode){
//
//            String qu = (position+1) + ". " + question;
//            tv_question.setText(qu);
//            nextBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectListener.select(position, questionID, getEditAnswer(), -1, true);
//                }
//            });
//            editText.setVisibility(View.VISIBLE);
//            nextBtn.setVisibility(View.VISIBLE);
//
//        }else if(isSubmitMode){
//
//            tv_question.setText("종합");
//            nextBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectListener.submit();
//                }
//            });
//            nextBtn.setText("제출");
//            nextBtn.setEnabled(true);
//            nextBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//            nextBtn.setVisibility(View.VISIBLE);
//
//        }else{
//
//            String qu = (position+1) + ". " + question;
//            tv_question.setText(qu);
//            editText.setVisibility(View.GONE);
//            nextBtn.setVisibility(View.GONE);
//            makeAnswerList();
//
//        }

    }

//    public void addSubmitContent(String question, String answer){
//        if(isSubmitMode) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("question", question);
//            map.put("answer", answer);
//            submitList.add(map);
//            makeSubmitList();
//        }
//    }
//    public void setSubmitContent(int position, String answer){
//        if(isSubmitMode){
//            HashMap<String, String> map = submitList.get(position);
//            map.put("answer", answer);
//            submitList.set(position, map);
//            makeSubmitList();
//        }
//    }




}
