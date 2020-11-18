package com.project.android.musisearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.android.musisearch.R;
import com.project.android.musisearch.model.GroupBand;
import com.project.android.musisearch.model.Status;
import com.project.android.musisearch.ui.main.groupBand.MemberBandActivity;

import java.util.List;


public class AdapterBand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context mContext;
    List<GroupBand> dataList;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public AdapterBand(Context context, List<GroupBand> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DataObjectHolder(inflater.inflate(R.layout.card_band, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final int id,musisi;
        String nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

        id = dataList.get(i).getId();
        musisi = dataList.get(i).getMusisi();

        nama = dataList.get(i).getNama();
        createdDate = dataList.get(i).getCreatedDate();
        createdByName = dataList.get(i).getCreatedByName();
        modifiedDate = dataList.get(i).getModifiedDate();
        modifiedByName = dataList.get(i).getModifiedByName();

        ((DataObjectHolder) holder).textName.setText(nama);
        ((DataObjectHolder) holder).textDate.setText(createdDate);
        ((DataObjectHolder) holder).relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MemberBandActivity.class);
                i.putExtra("id", id);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //protected ImageView imageFoto;
        protected CircularImageView imageProfile;
        protected TextView textName, textDate;
        protected RelativeLayout relative;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
            textDate = itemView.findViewById(R.id.textDate);
            relative = itemView.findViewById(R.id.relative);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    //region Loadmore
    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
    //endregion

}