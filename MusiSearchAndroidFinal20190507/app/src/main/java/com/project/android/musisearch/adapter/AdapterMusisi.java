package com.project.android.musisearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.android.musisearch.R;
import com.project.android.musisearch.model.Musisi;
import com.project.android.musisearch.ui.main.search.SearchActivity;

import java.util.List;


public class AdapterMusisi extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context mContext;
    List<Musisi> dataList;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public AdapterMusisi(Context context, List<Musisi> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DataObjectHolder(inflater.inflate(R.layout.card_musisi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final int id, gender, genre, region, instrument1, instrument2, instrument3, total, isLikes;
        final String nama, email, telp, tglLahir, genderName, genreName, regionName, instrument1Name, instrument2Name, instrument3Name, createdDate, createdByName, modifiedDate, modifiedByName, modelName;

        id = dataList.get(i).getId();
        gender = dataList.get(i).getGender();
        genre = dataList.get(i).getGenre();
        region = dataList.get(i).getRegion();
        instrument1 = dataList.get(i).getInstrument1();
        instrument2 = dataList.get(i).getInstrument2();
        instrument3 = dataList.get(i).getInstrument3();
        total = dataList.get(i).getTotal();
        nama = dataList.get(i).getNama();
        email = dataList.get(i).getEmail();
        telp = dataList.get(i).getTelp();
        tglLahir = dataList.get(i).getTglLahir();
        genderName = dataList.get(i).getGenderName();
        genreName = dataList.get(i).getGenreName();
        regionName = dataList.get(i).getRegionName();
        instrument1Name = dataList.get(i).getInstrument1Name();
        instrument2Name = dataList.get(i).getInstrument2Name();
        instrument3Name = dataList.get(i).getInstrument3Name();
        createdDate = dataList.get(i).getCreatedDate();
        createdByName = dataList.get(i).getCreatedByName();
        modifiedDate = dataList.get(i).getModifiedDate();
        modifiedByName = dataList.get(i).getModifiedByName();
        modelName = dataList.get(i).getModelName();
        isLikes = dataList.get(i).getIsLikes();
        ((DataObjectHolder) holder).textName.setText(nama);
        ((DataObjectHolder) holder).textEmail.setText(email);
        ((DataObjectHolder) holder).textGenre.setText(genreName);
        ((DataObjectHolder) holder).textInstrument.setText(instrument1Name);
        ((DataObjectHolder) holder).textLikes.setText(String.format(mContext.getResources().getString(R.string.likes), total));
        ((DataObjectHolder) holder).imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof SearchActivity) {
                    if (mContext instanceof SearchActivity) {
                        ((SearchActivity) mContext).findIDEmail(email);
                    }
                }
            }
        });
        if (isLikes == 1) {
            ((DataObjectHolder) holder).imgAddLikes.setVisibility(View.GONE);
        } else {
            ((DataObjectHolder) holder).imgAddLikes.setVisibility(View.VISIBLE);
            ((DataObjectHolder) holder).imgAddLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof SearchActivity) {
                        ((SearchActivity) mContext).insertLikes(id);
                    }
                }
            });
        }

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
        protected ImageView imgChat, imgAddLikes;
        protected TextView textName, textLikes, textEmail, textGenre, textInstrument;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            imgChat = itemView.findViewById(R.id.imgChat);
            imgAddLikes = itemView.findViewById(R.id.imgAddLikes);
            textName = itemView.findViewById(R.id.textName);
            textLikes = itemView.findViewById(R.id.textLikes);
            textEmail = itemView.findViewById(R.id.textEmail);
            textGenre = itemView.findViewById(R.id.textGenre);
            textInstrument = itemView.findViewById(R.id.textInstrument);
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