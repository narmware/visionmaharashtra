package com.narmware.visionmaharashtra.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.fragment.FeaturedNewsFragment;
import com.narmware.visionmaharashtra.fragment.NewsFragment;
import com.narmware.visionmaharashtra.pojo.Category;
import com.narmware.visionmaharashtra.pojo.CategoryResponse;
import com.narmware.visionmaharashtra.support.Endpoint;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityTab extends AppCompatActivity implements NewsFragment.OnFragmentInteractionListener,FeaturedNewsFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter pagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public Dialog mNoConnectionDialog;
    public RequestQueue mVolleyRequest;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        init();
    }

    private void init() {
        mVolleyRequest = Volley.newRequestQueue(HomeActivityTab.this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mNoConnectionDialog = new Dialog(HomeActivityTab.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //pagerAdapter.addFragment(new NewsFragment(),"Rajkiya");
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        GetCategories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_activity_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(HomeActivityTab.this, AboutUs.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        List<Fragment> fragments=new ArrayList<>();
        List<String> mFragmentTitleList = new ArrayList<>();

        @Override
        public Fragment getItem(int index) {

            return fragments.get(index);
        }

        public void addFragment(Fragment fragment,String title) {
            fragments.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    public void GetCategories() {
        final ProgressDialog dialog = new ProgressDialog(HomeActivityTab.this);
        dialog.setMessage("Getting Categories...");
        dialog.setCancelable(false);
        dialog.show();

        String url= Endpoint.GET_CATEGORIES;

        Log.e("Cat url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("Cat Json_string",response.toString());
                            Gson gson = new Gson();

                            CategoryResponse categoryResponse=gson.fromJson(response.toString(),CategoryResponse.class);
                            Category[] mlist=categoryResponse.getData();

                            for(Category item:mlist){
                                //categories.add(item);
                                if(item.getCat_id().equals("1"))
                                {
                                    pagerAdapter.addFragment(FeaturedNewsFragment.newInstance(item.getCat_id()),item.getTitle_app());
                                }
                                else{
                                    pagerAdapter.addFragment(NewsFragment.newInstance(item.getCat_id()),item.getTitle_app());
                                }
                            }
                            pagerAdapter.notifyDataSetChanged();
                        } catch (Exception e) {

                            e.printStackTrace();
                            dialog.dismiss();
                        }
                        if(mNoConnectionDialog.isShowing()==true)
                        {
                            mNoConnectionDialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                         dialog.dismiss();
                        showNoConnectionDialog();
                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    public void showNoConnectionDialog() {
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetCategories();
            }
        });
    }
}
