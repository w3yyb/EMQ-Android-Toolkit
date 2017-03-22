package io.emqtt.emqandroidtoolkit.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;

/**
 * ClassName: ConnectionAdapter
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder> {

    private List<Connection> mConnectionList;

    public ConnectionAdapter(List<Connection> connectionList) {
        mConnectionList = connectionList;
    }

    @Override
    public ConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection, parent, false);
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConnectionViewHolder holder, int position) {
        Connection connection = mConnectionList.get(position);
        holder.mClientIdText.setText(connection.getClintId());
        holder.mServerText.setText(connection.getClintId() + ":" + connection.getHost());


    }

    @Override
    public int getItemCount() {
        return mConnectionList.size();
    }


    static class ConnectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.client_id)
        TextView mClientIdText;
        @BindView(R.id.server)
        TextView mServerText;
        @BindView(R.id.connection_state)
        ImageView mConnectionStateImage;

        ConnectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
