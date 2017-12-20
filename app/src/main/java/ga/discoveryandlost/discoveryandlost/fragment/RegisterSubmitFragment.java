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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.Information;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.util.ParsePHP;
import ga.discoveryandlost.discoveryandlost.util.RegisterSelectListener;

public class RegisterSubmitFragment extends BaseFragment {

    // UI
    private View view;
    private Context context;

    private LinearLayout li_qa;
    private Button submitBtn;

    private RoundedImageView imageView;
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

        view = inflater.inflate(R.layout.fragment_register_submit, container, false);
        context = container.getContext();

        initUI();

        return view;
    }

    private void initUI(){

        imageView = (RoundedImageView)view.findViewById(R.id.img);
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

        File imgFile = new  File(Environment.getExternalStorageDirectory() + "/dal/temp/"+item.getTempImageName());

        if(imgFile.exists()){

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(bitmap);

//            myImage.setImageBitmap(myBitmap);

        }

        updateContent();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectListener.submit();
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

    public void updateContent(){

        li_qa.removeAllViews();

        for(int i=0; i<item.getSize(); i++){

            View v = item.getQueryView(getLayoutInflater(), i);
            MaterialEditText editText = (MaterialEditText)v.findViewById(R.id.edit_answer);
            editText.setEnabled(false);
            if(editText.getText().toString() == null || editText.getText().toString().isEmpty()){
                editText.setHint("-");
            }
            li_qa.addView(v);

        }

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
