package io.emqtt.emqandroidtoolkit.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Publication;

/**
 * ClassName: PublicationRecyclerViewAdapter
 * Desc:
 * Created by zhiw on 2017/3/27.
 */

public class PublicationRecyclerViewAdapter extends RecyclerView.Adapter<PublicationRecyclerViewAdapter.ViewHolder>{

    private List<Publication> mPublicationList;

    public PublicationRecyclerViewAdapter(List<Publication> publicationList) {
        mPublicationList = publicationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Publication publication = mPublicationList.get(position);
        holder.topicText.setText("Topic:" + publication.getTopic());
        holder.QoSText.setText("QoS:" + publication.getQoS());
        holder.payloadText.setText("Payload:" + publication.getPayload());
        holder.retainedText.setVisibility(publication.isRetained() ? View.VISIBLE : View.INVISIBLE);
        holder.timeText.setText(publication.getTime());

    }

    @Override
    public int getItemCount() {
        return mPublicationList.size();
    }

    public void insertData(Publication publication) {
        mPublicationList.add(0, publication);
        notifyItemInserted(0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.topic) TextView topicText;
        @BindView(R.id.payload) TextView payloadText;
        @BindView(R.id.QoS) TextView QoSText;
        @BindView(R.id.retained) TextView retainedText;
        @BindView(R.id.time) TextView timeText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }


    }
}
