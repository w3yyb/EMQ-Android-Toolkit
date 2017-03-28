package io.emqtt.emqandroidtoolkit.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Publication;
import io.emqtt.emqandroidtoolkit.ui.adapter.PublicationRecyclerViewAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.BaseFragment;


public class PublicationListFragment extends BaseFragment {

    public static final String TAG = "Publication";

    @BindView(R.id.publication_list) RecyclerView mPublicationRecyclerView;

    private PublicationRecyclerViewAdapter mAdapter;


    public PublicationListFragment() {
        // Required empty public constructor
    }


    public static PublicationListFragment newInstance() {
        PublicationListFragment fragment = new PublicationListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_publication_list;
    }

    @Override
    protected void setUpView() {
        mPublicationRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));

    }

    @Override
    protected void setUpData() {

        List<Publication>  list = new ArrayList<>();

        mAdapter = new PublicationRecyclerViewAdapter(list);
        mPublicationRecyclerView.setAdapter(mAdapter);

    }

    public void insertData(Publication publication){
        mAdapter.insertData(publication);

    }


}
