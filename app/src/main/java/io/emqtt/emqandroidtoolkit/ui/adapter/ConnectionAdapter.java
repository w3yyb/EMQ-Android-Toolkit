package io.emqtt.emqandroidtoolkit.ui.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.ui.OnItemClickListener;
import io.emqtt.emqandroidtoolkit.ui.activity.ConnectionDetailActivity;

/**
 * ClassName: ConnectionAdapter
 * Desc:
 * Created by zhiw on 2017/3/22.
 */

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder> {

    private List<Connection> mConnectionList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public ConnectionAdapter(List<Connection> connectionList) {
        mConnectionList = connectionList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection, parent, false);
        mContext = parent.getContext();
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConnectionViewHolder holder, int position) {
        final Connection connection = mConnectionList.get(position);
        final int pos = holder.getAdapterPosition();
        holder.mClientIdText.setText(connection.getClintId());
        holder.mServerText.setText(connection.getHost() + ":" + connection.getPort());

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, pos, connection);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetailActivity.openActivity(mContext,connection);

            }
        });


    }

    private void showMenu(View v, final int position, final Connection connection) {
        final PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.menu_connection);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemEdit(position, connection);
                        }

                        return true;
                    case R.id.action_delete:
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemDelete(position, connection);
                        }
                        mConnectionList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());

                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mConnectionList.size();
    }


    static class ConnectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.client_id) TextView mClientIdText;
        @BindView(R.id.server) TextView mServerText;
        @BindView(R.id.connection_state) ImageView mConnectionStateImage;
        @BindView(R.id.more) ImageView mMore;

        ConnectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
