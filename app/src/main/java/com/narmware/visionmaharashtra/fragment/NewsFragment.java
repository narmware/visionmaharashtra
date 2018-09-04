package com.narmware.visionmaharashtra.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.adapter.NewsAdapter;
import com.narmware.visionmaharashtra.pojo.Category;
import com.narmware.visionmaharashtra.pojo.CategoryResponse;
import com.narmware.visionmaharashtra.pojo.NewsItem;
import com.narmware.visionmaharashtra.pojo.NewsResponse;
import com.narmware.visionmaharashtra.support.Endpoint;
import com.narmware.visionmaharashtra.support.SupportFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CAT_ID = "cat_id";

    // TODO: Rename and change types of parameters
    private String cat_id;
    public Dialog mNoConnectionDialog;
    public RequestQueue mVolleyRequest;
    private OnFragmentInteractionListener mListener;


    ArrayList<NewsItem> newsItems;
    RecyclerView newsRecycler;
    NewsAdapter newsAdapter;
    public static LinearLayout mEmptyDataLayout;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int temp_id;
    public static boolean loading = true;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String cat_id) {
        NewsFragment fragment = new NewsFragment();
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
        View view= inflater.inflate(R.layout.fragment_news, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        mVolleyRequest = Volley.newRequestQueue(getContext());
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        newsRecycler=view.findViewById(R.id.news_recycler);
        mEmptyDataLayout=view.findViewById(R.id.empty_data_layout);

        setNewsAdapter();
        GetNewsByCat();
    }

    public void setNewsAdapter() {
        newsItems = new ArrayList<>();

        SnapHelper snapHelper = new LinearSnapHelper();

        newsAdapter = new NewsAdapter(getContext(), newsItems);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        newsRecycler.setLayoutManager(linearLayoutManager);
        newsRecycler.setItemAnimator(new DefaultItemAnimator());
        //snapHelper.attachToRecyclerView(mRecyclerView);
        newsRecycler.setAdapter(newsAdapter);
        newsRecycler.setNestedScrollingEnabled(false);
        newsRecycler.setFocusable(false);

        newsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    int size=newsItems.size();
                    Log.v("...", ""+visibleItemCount+"  "+linearLayoutManager.findLastVisibleItemPosition()+"   "+size);

                    int current_id= Integer.parseInt(newsItems.get(newsItems.size()-1).getV_id());

                    if(temp_id > current_id)
                    {
                        loading=true;
                    }
                    if (loading) {

                            if (linearLayoutManager.findLastVisibleItemPosition() == newsItems.size() - 1) {
                                //if(pastVisiblesItems == dharamshalaItems.size()-3 ){
                                loading = false;
                                HashMap<String, String> param = new HashMap();
                                String pos = newsItems.get(newsItems.size() - 1).getV_id();
                                temp_id = Integer.parseInt(pos);
                                param.put(Endpoint.VIDEO_ID, pos);
                                String url = SupportFunctions.appendParam(Endpoint.GET_NEWS, param);
                                //GetDharamshalas(url);

                                Log.v("...", "Last Item Wow !" + newsItems.size());
                                //Do pagination.. i.e. fetch new data
                            }

                    }
                }

            }
        });

        newsAdapter.notifyDataSetChanged();
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

    public void showNoConnectionDialog() {
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    GetNewsByCat();
            }
        });
    }
}
