package com.project.android.musisearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.android.musisearch.R;
import com.project.android.musisearch.model.Status;

import java.util.List;


public class AdapterStatus extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context mContext;
    List<Status> dataList;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public AdapterStatus(Context context, List<Status> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DataObjectHolder(inflater.inflate(R.layout.card_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        int id,musisi, initialStatus;
        String musisiName, email, telp, tglLahir, initialStatusName, nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

        id = dataList.get(i).getId();
        musisi = dataList.get(i).getMusisi();
        initialStatus = dataList.get(i).getInitialStatus();
        musisiName = dataList.get(i).getMusisiName();
        email = dataList.get(i).getEmail();
        telp = dataList.get(i).getTelp();
        tglLahir = dataList.get(i).getTglLahir();
        initialStatusName = dataList.get(i).getInitialStatusName();
        nama = dataList.get(i).getNama();
        createdDate = dataList.get(i).getCreatedDate();
        createdByName = dataList.get(i).getCreatedByName();
        modifiedDate = dataList.get(i).getModifiedDate();
        modifiedByName = dataList.get(i).getModifiedByName();
        modelName = dataList.get(i).getModelName();

        ((DataObjectHolder) holder).textName.setText(musisiName);
        ((DataObjectHolder) holder).textDate.setText(createdDate);
        ((DataObjectHolder) holder).textStatus.setText(initialStatusName + " : " + nama);


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
        protected TextView textName, textDate, textTime, textStatus;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            textStatus = itemView.findViewById(R.id.textStatus);
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