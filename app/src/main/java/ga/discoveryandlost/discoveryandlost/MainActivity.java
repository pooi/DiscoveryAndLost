package ga.discoveryandlost.discoveryandlost;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.fragment.FindItemFragment;
import ga.discoveryandlost.discoveryandlost.fragment.RegisterItemFragment;
import ga.discoveryandlost.discoveryandlost.fragment.SellItemFragment;
import ga.discoveryandlost.discoveryandlost.obj.User;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private NavigationAdapter mPagerAdapter;
    private String[] toolbarTitleList = new String[3];
    private static final int startPage = 0;

    private FloatingActionsMenu menu;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarTitleList = new String[3];
        toolbarTitleList[0] = getString(R.string.manage_srt);
        toolbarTitleList[1] = getResources().getString(R.string.view_by_item);
        toolbarTitleList[2] = getResources().getString(R.string.view_by_date);

        user = StartActivity.USER;

        init();

    }


    private void init(){

        floationMenu();

        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setOffscreenPageLimit(toolbarTitleList.length);
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), user);
        viewPager.setAdapter(mPagerAdapter);

        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        navigationTabBar.setTitleSize(30);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_file_document_white_36dp),
                        getColorId(R.color.colorAccent))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_format_list_bulleted_type_white_36dp),
                        getColorId(R.color.colorAccent))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_calendar_today_white_36dp),
                        getColorId(R.color.colorAccent))
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, startPage);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);

    }

    public void floationMenu(){

        menu = (FloatingActionsMenu)findViewById(R.id.multiple_actions);

        FloatingActionButton signup = (FloatingActionButton)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(MainActivity.this);
            }
        });
        signup.setTitle("로그아웃");

    }

    @Override
    public void onBackPressed(){
        new MaterialDialog.Builder(this)
                .title(R.string.ok)
                .content(R.string.are_you_finish_app)
                .positiveText(R.string.finish)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }


    private static class NavigationAdapter extends FragmentStatePagerAdapter {

        User user;

        public NavigationAdapter(FragmentManager fm, User user){
            super(fm);
            this.user = user;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f;
            final int pattern;
            pattern = position%3;
            Bundle bdl = new Bundle(1);
            bdl.putSerializable("user", user);

            switch (pattern) {
                case 0: {
                    f = new RegisterItemFragment();
                    f.setArguments(bdl);
                    break;
                }
                case 1: {
                    f = new FindItemFragment();
                    f.setArguments(bdl);
                    break;
                }
                case 2: {
                    f = new SellItemFragment();
                    f.setArguments(bdl);
                    break;
                }
                default: {
                    f = new Fragment();
                    break;
                }
            }

            return f;
        }

        @Override
        public int getCount(){
            return 3;
        }

    }

}
