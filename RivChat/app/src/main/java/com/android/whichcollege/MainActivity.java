package com.android.whichcollege;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.whichcollege.data.StaticConfig;
import com.android.whichcollege.service.ServiceUtils;
import com.android.whichcollege.ui.FriendsFragment;
import com.android.whichcollege.ui.GroupFragment;
import com.android.whichcollege.ui.LoginActivity;
import com.android.whichcollege.ui.UserProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout = null;
   // public static String STR_FRIEND_FRAGMENT = "FRIEND";
   // public static String STR_GROUP_FRAGMENT = "GROUP";
   // public static String STR_INFO_FRAGMENT = "INFO";

  // private FloatingActionButton floatButton;
    private ViewPagerAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private FriendsFragment friendsFragment;
    private GroupFragment groupFragment;
    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          //  setSupportActionBar(toolbar);

        friendsFragment = new FriendsFragment();
        groupFragment = new GroupFragment();
        userProfileFragment = new UserProfileFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //floatButton = (FloatingActionButton) findViewById(R.id.fab);
        initTab();
        initFirebase();
    }

    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    MainActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        ServiceUtils.stopServiceFriendChat(getApplicationContext(), false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        ServiceUtils.startServiceFriendChat(getApplicationContext());
        super.onDestroy();
    }

    /**
     * Khoi tao 3 tab
     */
    private void initTab() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.click_color));
        tabLayout.setTabTextColors(getResources().getColor(R.color.grey_500), getResources().getColor(R.color.click_color));
        tabLayout.setupWithViewPager(viewPager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        adapter.addFrag(new FriendsFragment(), "Friends");
        adapter.addFrag(new GroupFragment(), "Chat");
        adapter.addFrag(new UserProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);

        //setupTabIcons();
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_person);
        tabLayout.getTabAt(1).setIcon(R.drawable.chat_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_infor);

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.click_color), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_500), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_500), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.click_color), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_500), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

  /* private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FriendsFragment(), STR_FRIEND_FRAGMENT);
        adapter.addFrag(new GroupFragment(), STR_GROUP_FRAGMENT);
        adapter.addFrag(new UserProfileFragment(), STR_INFO_FRAGMENT);
       // floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(0)).onClickFloatButton.getInstance(this));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ServiceUtils.stopServiceFriendChat(MainActivity.this.getApplicationContext(), false);
                if (adapter.getItem(position) instanceof FriendsFragment) {
                  //  floatButton.setVisibility(View.INVISIBLE);
                   // floatButton.setOnClickListener(((FriendsFragment) adapter.getItem(position)).onClickFloatButton.getInstance(MainActivity.this));
                   // floatButton.setImageResource(R.drawable.plus);
                } else if (adapter.getItem(position) instanceof GroupFragment) {

                   // floatButton.setVisibility(View.INVISIBLE);
                  //  floatButton.setOnClickListener(((GroupFragment) adapter.getItem(position)).onClickFloatButton.getInstance(MainActivity.this));
                   // floatButton.setImageResource(R.drawable.ic_float_add_group);
                } else {
                   // floatButton.setVisibility(View.INVISIBLE);
                }
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
*/


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
//            if (data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_LOGIN)) {
//                authUtils.signIn(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
//            } else if (data.getStringExtra(STR_EXTRA_ACTION).equals(RegisterActivity.STR_EXTRA_ACTION_REGISTER)) {
//                authUtils.createUser(data.getStringExtra(STR_EXTRA_USERNAME), data.getStringExtra(STR_EXTRA_PASSWORD));
//            }else if(data.getStringExtra(STR_EXTRA_ACTION).equals(LoginActivity.STR_EXTRA_ACTION_RESET)){
//                authUtils.resetPassword(data.getStringExtra(STR_EXTRA_USERNAME));
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            this.finish();
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Toast.makeText(this, "Rivchat version 1.0", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter hien thi tab
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return mFragmentTitleList.get(position);
        }
    }
}