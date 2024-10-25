package com.salestracker.sales.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salestracker.sales.R;
import com.salestracker.sales.adapters.BusinessAdapter;
import com.salestracker.sales.application.AppController;
import com.salestracker.sales.listeners.EndlessRecyclerOnScrollListener;
import com.salestracker.sales.modals.BusinessEntry;
import com.salestracker.sales.utils.PreferenceUtils;
import com.salestracker.sales.utils.PublicMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tarun on 22/5/16.
 */
public class BusinessesListFragment extends Fragment {

    private static String TAG = "BusinessesListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<BusinessEntry> mItems;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressBar pbBusiness;
    private ProgressBar pbBusinessMore;

    public static String GET_BUSINESSES_TAG = "getBusinessesTag";

    String url = AppController.BASE_URL + "/apis/businesses/";
    String rowUrl = AppController.BASE_URL + "/apis/businesses/";
    private PreferenceUtils preferenceUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_businesses_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Businesses");
        preferenceUtils = PreferenceUtils.getInstance(AppController.getInstance());

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.businessesSwipeRefreshLayout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvBusinesses);
        pbBusiness = (ProgressBar) view.findViewById(R.id.pbBusiness);
        pbBusinessMore = (ProgressBar) view.findViewById(R.id.pbBusinessMore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mItems = new ArrayList<>();

        /*getBusinesses(url, true);*/

        mAdapter = new BusinessAdapter(getActivity(), mItems);

        mRecyclerView.setAdapter(mAdapter);

        /**
         * Implementing swipe to refresh
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if (url != null) {
                    url = url.split("\\?")[0];
                    getBusinesses(url, true);
                } else*/
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    final private static int ANIMATION_TIME = 150; // this time is same as that of menu animation.

    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private void getBusinesses(String businessUrl, final boolean refresh) {
        if (businessUrl == null) return;

        // Scroll listener has been added here becuase after each refresh we need to re intialize the listener so that
        // it leaves off its previous item count.
        if (refresh) {
            if (endlessRecyclerOnScrollListener != null) {
                mRecyclerView.removeOnScrollListener(endlessRecyclerOnScrollListener);
            }
            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    getBusinesses(url, false);
                }
            };
            mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
            if (mItems == null || mItems.size() == 0)
                pbBusiness.setVisibility(View.VISIBLE);
            else if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout.setRefreshing(true);
        } else {
            pbBusinessMore.setVisibility(View.VISIBLE);
        }

        StringRequest strReq = new StringRequest(Request.Method.GET,
                businessUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (BusinessesListFragment.this == null)
                    return;
                pbBusiness.setVisibility(View.GONE);
                pbBusinessMore.setVisibility(View.GONE);
                if (refresh) {
                    mItems.clear();
                }
                populateRecyclerViewItems(response);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error);
                PublicMethods.handleError(error, getActivity());
                if (BusinessesListFragment.this != null) {
                    pbBusiness.setVisibility(View.GONE);
                    pbBusinessMore.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                /*headers.put("Authorization", "Token " + preferenceUtils.getGreymeterAuthToken());*/
                return headers;
            }
        };
        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq, GET_BUSINESSES_TAG);
    }

    private void populateRecyclerViewItems(String response) {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            JsonNode node = mapper.readTree(response);

            try {
                if (node.get("next").isNull())
                    url = null;
                else
                    url = node.get("next").asText();
            } catch (Exception e) {
                url = null;
            }

            node = node.get("results");
            TypeReference<List<BusinessEntry>> typeRef = new TypeReference<List<BusinessEntry>>(){};
            List<BusinessEntry> list = mapper.readValue(node.traverse(), typeRef);

            mItems.addAll(list);
            mAdapter.notifyDataSetChanged();
        } catch (JsonGenerationException e)
        {
            e.printStackTrace();
        } catch (JsonMappingException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }
}
