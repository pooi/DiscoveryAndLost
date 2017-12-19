package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.fragment.RegisterDetailFragment;
import ga.discoveryandlost.discoveryandlost.fragment.RegisterSubmitFragment;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.util.CustomViewPager;
import ga.discoveryandlost.discoveryandlost.util.RegisterSelectListener;

public class RegisterNewItemActivity extends BaseActivity implements RegisterSelectListener{

    private MyHandler handler = new MyHandler();

    String itemName;

    private DotIndicator dotIndicator;
    private CustomViewPager viewPager;
    private NavigationAdapter mPagerAdapter;
    private RegisterDetailFragment[] registerDetailFragments;
    private RegisterSubmitFragment submitFragment;

    DalItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_item);

        itemName = getIntent().getStringExtra("item");
        item = new DalItem(getLayoutInflater());
        String imageName = getIntent().getStringExtra("imageName");

        item.setCategory(itemName);
        item.setTempImageName(imageName);

        registerDetailFragments = new RegisterDetailFragment[item.getSize()];

        init();

    }

    private void init(){

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNewItemActivity.this.finish();
            }
        });

        dotIndicator = (DotIndicator) findViewById(R.id.main_indicator_ad);
        dotIndicator.setSelectedDotColor(Color.parseColor("#FF4081"));
        dotIndicator.setUnselectedDotColor(Color.parseColor("#CFCFCF"));
        dotIndicator.setNumberOfItems(registerDetailFragments.length);
        dotIndicator.setSelectedItem(0, false);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(registerDetailFragments.length);
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), item, this);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(position == lastPage+1){
//                    viewPager.setCurrentItem(lastPage, true);
//                }
            }

            @Override
            public void onPageSelected(int position) {
                dotIndicator.setSelectedItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setReviewItemFragments(RegisterDetailFragment f, int position){
        registerDetailFragments[position] = f;
    }
    public RegisterDetailFragment getReviewItemFragment(int position){
        return registerDetailFragments[position];
    }
    public void setRegisterSubmitFragment(RegisterSubmitFragment f){
        this.submitFragment = f;
    }
    public RegisterSubmitFragment getSubmitFragment(){
        return this.submitFragment;
    }

    @Override
    public void select(final int fragmentPosition, View v, int selectAnswerIndex, boolean force) {
        item.updateContent(v, fragmentPosition);
        submitFragment.updateContent();

        Runnable runnable = new Runnable() {
            public void run() {
                if(fragmentPosition+1 < item.getSize())
                    viewPager.setCurrentItem(fragmentPosition + 1, true);
            }
        };

        if(force) {
            handler.postDelayed(runnable, 0);
        }else{
            handler.postDelayed(runnable, 500);
        }
    }

    @Override
    public void submit() {

    }

    private class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    break;
            }
        }
    }

    private static class NavigationAdapter extends FragmentPagerAdapter {

        private int size;
        private DalItem item;
        private RegisterNewItemActivity activity;

        public NavigationAdapter(FragmentManager fm, DalItem item, RegisterNewItemActivity activity) {
            super(fm);
            this.item = item;
            this.size = item.getSize();
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {

            final int pattern = position % size;

            Fragment f;


            Bundle bdl = new Bundle(1);

//            if(pattern == size-1){
//
//                if(activity.getSubmitFragment() == null) {
//
//                    f = new RegisterSubmitFragment();
//                    bdl.putInt("position", pattern);
//                    bdl.putSerializable("item", item);
//                    bdl.putSerializable("listener", activity);
//
//
//                    activity.setRegisterSubmitFragment((RegisterSubmitFragment) f);
//
//                }else{
//                    f = activity.getSubmitFragment();
//                }
//
//            }else {


                if(activity.getReviewItemFragment(pattern) == null){

                    f = new RegisterDetailFragment();
                    bdl.putInt("position", pattern);
                    bdl.putSerializable("item", item);
                    bdl.putSerializable("listener", activity);

                    activity.setReviewItemFragments((RegisterDetailFragment)f, pattern);

                }else {
                    f = activity.getReviewItemFragment(pattern);
                }

//            }

//                if(pattern == size-1){
//                    //ReviewSelectListener listener = (ReviewSelectListener)activity;
////                    bdl.putSerializable("listener", activity);
////                    bdl.putBoolean("isSubmitMode", true);
//                }else{
//                    bdl.putInt("position", pattern);
////                    bdl.putSerializable("item", list.get(pattern));
//                    //ReviewSelectListener listener = (ReviewSelectListener)activity;
////                    bdl.putSerializable("listener", activity);
////                    if(pattern == size-2){
////                        bdl.putBoolean("isEditMode", true);
////                    }
//                }

            f.setArguments(bdl);

            return f;
        }

        @Override
        public int getCount() {
            return size;
        }
    }

}
