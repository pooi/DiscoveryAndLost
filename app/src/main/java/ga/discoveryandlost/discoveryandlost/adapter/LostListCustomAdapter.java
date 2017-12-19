package ga.discoveryandlost.discoveryandlost.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ppamorim.dragger.DraggerPosition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.activity.RequestLostActivity;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.obj.DalLost;
import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;
import ga.discoveryandlost.discoveryandlost.util.OnAdapterSupport;
import ga.discoveryandlost.discoveryandlost.util.OnLoadMoreListener;


/**
 * Created by tw on 2017-10-01.
 */
public class LostListCustomAdapter extends RecyclerView.Adapter<LostListCustomAdapter.ViewHolder> {

    // UI
    private Context context;
//    private SearchPatientActivity activity;

    //    private MaterialNavigationDrawer activity;
    private OnAdapterSupport onAdapterSupport;

    public ArrayList<DalLost> list;
    public DalItem dalItem;

    // 무한 스크롤
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;

    // 생성자
    public LostListCustomAdapter(Context context, ArrayList<DalLost> list, DalItem dalItem, RecyclerView recyclerView, OnAdapterSupport listener) {
        this.context = context;
        this.list = list;
        this.dalItem = dalItem;
        this.onAdapterSupport = listener;
//        this.activity = (SearchPatientActivity) activity;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            recyclerView.addOnScrollListener(new ScrollListener() {
                @Override
                public void onHide() {
                    hideViews();
                }

                @Override
                public void onShow() {
                    showViews();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_list_custom_item,null);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DalLost lost = list.get(position);
        final int pos = position;

        holder.tv_category.setText(lost.getCategory());
        holder.tv_color.setText(lost.getColor());
        holder.tv_date.setText("습득일 : " + AdditionalFunc.getDateString(lost.getRegisteredDate()));
        holder.tv_match.setText("70%");

        Picasso.with(context)
                .load(getImgUrl(lost.getCategory()))
                .into(holder.img);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RequestLostActivity.class);
                intent.putExtra("item", dalItem);
                intent.putExtra("lost", lost);
                onAdapterSupport.redirectActivity(intent);
            }
        });

    }

    public String getImgUrl(String category) {

        String url;

        switch (category){
            case "mouse":
            case "마우스":
                url = "http://nearby.cf/dal_category/wallet.jpg";
                break;
            case "tumbler":
            case "텀블러":
                url = "http://nearby.cf/dal_category/tumbler.jpg";
                break;
            case "laptop":
            case "노트북":
                url = "http://nearby.cf/dal_category/laptop.jpg";
                break;
            case "wallet":
            case "지갑":
                url = "http://nearby.cf/dal_category/wallet.jpg";
                break;
            case "watch":
            case "손목시계":
                url = "http://nearby.cf/dal_category/watch.jpg";
                break;
            default:
                url = "http://nearby.cf/dal_category/default.jpg";
                break;
        }
        return url;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    private void hideViews() {
        if (onAdapterSupport != null) {
            onAdapterSupport.hideView();
        }
    }

    private void showViews() {
        if (onAdapterSupport != null) {
            onAdapterSupport.showView();
        }
    }

    // 무한 스크롤
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setLoaded() {
        loading = false;
    }

    public abstract class ScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 30;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                // End has been reached
                // Do something
                loading = true;
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
            }
            // 여기까지 무한 스크롤

            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }
            // 여기까지 툴바 숨기기
        }

        public abstract void onHide();
        public abstract void onShow();

    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        Rect rect;

        CardView cv;
        TextView tv_category;
        TextView tv_color;
        TextView tv_date;
        TextView tv_match;

        ImageView img;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView)v.findViewById(R.id.cv);
            setCardButtonOnTouchAnimation(cv, cv);
            tv_category = (TextView)v.findViewById(R.id.tv_category);
            tv_color = (TextView)v.findViewById(R.id.tv_color);
            tv_date = (TextView)v.findViewById(R.id.tv_date);
            tv_match = (TextView)v.findViewById(R.id.tv_match);
            img = (ImageView)v.findViewById(R.id.img);
        }

        public void setCardButtonOnTouchAnimation(final View v, final View animV){

            View.OnTouchListener onTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                System.out.println(motionEvent.getAction());
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN: {
                            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//                        System.out.println("action down");
                            Animation anim = new ScaleAnimation(
                                    1f, 0.95f, // Start and end values for the X axis scaling
                                    1f, 0.95f, // Start and end values for the Y axis scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                            anim.setFillAfter(true); // Needed to keep the result of the animation
                            anim.setDuration(300);
                            animV.startAnimation(anim);
                            animV.requestLayout();
                            return true;
                        }
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP: {
//                        System.out.println("action up");
                            Animation anim = new ScaleAnimation(
                                    0.95f, 1f, // Start and end values for the X axis scaling
                                    0.95f, 1f, // Start and end values for the Y axis scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                            anim.setFillAfter(true); // Needed to keep the result of the animation
                            anim.setDuration(300);
                            animV.startAnimation(anim);
                            animV.requestLayout();
                            if(!rect.contains(v.getLeft() + (int) motionEvent.getX(), v.getTop() + (int) motionEvent.getY())){
                                // User moved outside bounds
                            }else{
                                v.callOnClick();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            };
            v.setOnTouchListener(onTouchListener);
        }
    }

}
