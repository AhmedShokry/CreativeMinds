package com.smartvision.creativeminds.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.smartvision.creativeminds.Connection.ServerTool;
import com.smartvision.creativeminds.R;
import com.smartvision.creativeminds.adapters.RecyclerAdapter;
import com.smartvision.creativeminds.listeners.EndlessRecyclerViewScrollListener;
import com.smartvision.creativeminds.models.JsonResponseModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<JsonResponseModel> data;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshAction();
                    }
                }
        );
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<>();
        GetAcceptedProducts(0);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                GetAcceptedProducts(page);

            }
        });


    }

    private void refreshAction() {
        Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void GetAcceptedProducts(int pageNumber) {
        ServerTool.getJsonData(MainActivity.this, pageNumber, 10, new ServerTool.APICallBack<List<JsonResponseModel>>() {

            @Override
            public void onSuccess(final List<JsonResponseModel> response) {
                if (response != null) {

                    for (int i = 0; i < response.size(); i++) {

                        data.add(new JsonResponseModel(response.get(i).getName(),
                                response.get(i).getOwner(),
                                response.get(i).getDescription(),
                                response.get(i).getHtml_url()));


                    }
                    if (data.size() > 0) {
                        adapter = new RecyclerAdapter(data, MainActivity.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                    } else {
//                        noDataTextView.setVisibility(View.VISIBLE);


                    }


                } else {
//                    Toast.makeText(getActivity(), "Can't get Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(int statusCode, ResponseBody responseBody) {

            }
        });

    }


}
