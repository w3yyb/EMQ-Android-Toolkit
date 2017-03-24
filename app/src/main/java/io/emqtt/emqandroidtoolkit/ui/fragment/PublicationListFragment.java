package io.emqtt.emqandroidtoolkit.ui.fragment;


import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.ui.base.BaseFragment;


public class PublicationListFragment extends BaseFragment {

    public static final String TAG = "Publication";


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

    }

    @Override
    protected void setUpData() {

    }
}
