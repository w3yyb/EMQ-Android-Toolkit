package io.emqtt.emqandroidtoolkit.ui.fragment;


import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.ui.base.BaseFragment;


public class PublicationFragment extends BaseFragment {

    public static final String TAG = "Publication";


    public PublicationFragment() {
        // Required empty public constructor
    }


    public static PublicationFragment newInstance() {
        PublicationFragment fragment = new PublicationFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_publication;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
}
