package com.narmware.visionmaharashtra.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.narmware.visionmaharashtra.MyApplication;
import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.activity.WebViewActivity;
import com.narmware.visionmaharashtra.adapter.FeaturedNewsAdapter;
import com.narmware.visionmaharashtra.adapter.NewsAdapter;
import com.narmware.visionmaharashtra.pojo.Banner;
import com.narmware.visionmaharashtra.pojo.BannerResponse;
import com.narmware.visionmaharashtra.pojo.NewsItem;
import com.narmware.visionmaharashtra.pojo.NewsResponse;
import com.narmware.visionmaharashtra.support.Endpoint;
import com.narmware.visionmaharashtra.support.SupportFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeaturedNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeaturedNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedNewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CAT_ID = "cat_id";
    // TODO: Rename and change types of parameters
    private String cat_id;
    public RequestQueue mVolleyRequest;
    ArrayList<NewsItem> newsItems;
    RecyclerView newsRecycler;
    FeaturedNewsAdapter newsAdapter;


    ArrayList<Banner> mBannerImages;
    SliderLayout mSlider;
    PagerIndicator custom_indicator;

    private OnFragmentInteractionListener mListener;
    public Dialog mNoConnectionDialog;

    public FeaturedNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeaturedNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeaturedNewsFragment newInstance(String cat_id) {
        FeaturedNewsFragment fragment = new FeaturedNewsFragment();
        Bundle args = new Bundle();
        args.putString(CAT_ID, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cat_id = getArguments().getString(CAT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_featured_news, container, false);

        init(view);
        GetBanners();
        return view;
    }

    private void init(View view) {
        mVolleyRequest = Volley.newRequestQueue(getContext());
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        newsRecycler=view.findViewById(R.id.news_recycler);
        mSlider=view.findViewById(R.id.slider);
        custom_indicator=view.findViewById(R.id.custom_indicator);

        mBannerImages=new ArrayList<>();

        setNewsAdapter();
        GetNewsByCat();
    }

    public void setNewsAdapter() {
        newsItems = new ArrayList<>();

        SnapHelper snapHelper = new LinearSnapHelper();

        newsAdapter = new FeaturedNewsAdapter(getContext(), newsItems);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        newsRecycler.setLayoutManager(linearLayoutManager);
        newsRecycler.setItemAnimator(new DefaultItemAnimator());
        //snapHelper.attachToRecyclerView(mRecyclerView);
        newsRecycler.setAdapter(newsAdapter);
        newsRecycler.setNestedScrollingEnabled(false);
        newsRecycler.setFocusable(false);

        newsAdapter.notifyDataSetChanged();
    }

    private void setSlider() {
        // HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        HashMap<String,String> file_maps = new HashMap<String, String>();

        for(int i=0;i<mBannerImages.size();i++)
        {
            // Log.e("Banner slider size",mBannerImages.get(i).getBanner_title());
            file_maps.put(mBannerImages.get(i).getBanner_title(),mBannerImages.get(i).getBanner_img());
        }
        //file_maps.put("Hannibal", R.drawable.pre_mar_1);

        for(String name : file_maps.keySet()){
            //textSliderView displays image with text title
            TextSliderView textSliderView = new TextSliderView(getActivity());

            //DefaultSliderView displays only image
            //DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mSlider.addSlider(textSliderView);
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    String url = getBannerUrl(slider.getUrl());

                    if(!url.equals("")) {
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra(Endpoint.URL, url);
                        startActivity(intent);
                    }
                   // Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                }
            });
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        //mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomIndicator(custom_indicator);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setFitsSystemWindows(true);
        mSlider.setDuration(3000);

    }
    private String getBannerUrl(String imageUrl) {
        for(Banner banner : mBannerImages) {
            if(banner.getBanner_img() == imageUrl) {
                return banner.getBanner_url();
            }
        }
        return null;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void GetNewsByCat() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Getting Categories...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        param.put(Endpoint.CAT_ID,cat_id);

        String url= SupportFunctions.appendParam(Endpoint.GET_NEWS,param);

        Log.e("News url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("News Json_string",response.toString());
                            Gson gson = new Gson();

                            NewsResponse newsResponse=gson.fromJson(response.toString(),NewsResponse.class);
                            NewsItem[] mlist=newsResponse.getData();

                            for(NewsItem item:mlist){
                                newsItems.add(item);
                            }
                            newsAdapter.notifyDataSetChanged();

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

    public void GetBanners() {
       /* final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Getting Categories...");
        dialog.setCancelable(false);
        dialog.show();*/

        String url= Endpoint.GET_BANNERS;

        Log.e("Banner url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("Banner Json_string",response.toString());
                            Gson gson = new Gson();

                            BannerResponse bannerResponse=gson.fromJson(response.toString(),BannerResponse.class);
                            Banner[] mlist=bannerResponse.getData();

                            for(Banner item:mlist){
                                mBannerImages.add(item);
                            }
                            setSlider();

                        } catch (Exception e) {

                            e.printStackTrace();
                            //dialog.dismiss();
                        }
                        if(mNoConnectionDialog.isShowing()==true)
                        {
                            mNoConnectionDialog.dismiss();
                        }
                       // dialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        //dialog.dismiss();
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

            }
        });
    }
}
