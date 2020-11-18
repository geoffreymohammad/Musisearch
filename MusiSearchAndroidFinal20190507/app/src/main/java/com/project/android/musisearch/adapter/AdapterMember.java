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
import com.project.android.musisearch.model.Member;
import com.project.android.musisearch.model.Musisi;
import com.project.android.musisearch.ui.main.groupBand.MemberBandActivity;

import java.util.List;


public class AdapterMember extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context mContext;
    List<Member> dataList;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public AdapterMember(Context context, List<Member> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DataObjectHolder(inflater.inflate(R.layout.card_member, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final int id, musisi, groupBand, createdby;
        String namaMusisi, namaBand;
        id = dataList.get(i).getId();
        musisi = dataList.get(i).getMusisi();
        groupBand = dataList.get(i).getGroupBand();
        createdby = dataList.get(i).getCreatedby();
        namaMusisi = dataList.get(i).getNamaMusisi();
        namaBand = dataList.get(i).getNamaBand();


        ((DataObjectHolder) holder).textName.setText(namaMusisi);
        ((DataObjectHolder) holder).relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        protected TextView textName;
        protected RelativeLayout relative;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
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