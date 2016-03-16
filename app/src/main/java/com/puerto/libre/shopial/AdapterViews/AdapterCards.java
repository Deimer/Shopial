package com.puerto.libre.shopial.AdapterViews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.puerto.libre.shopial.Objects.Album;
import com.puerto.libre.shopial.R;
import com.puerto.libre.shopial.views.GridPhotos;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Deimer Villa on 3/11/2016.
 */
public class AdapterCards extends RecyclerView.Adapter<AdapterCards.AdapterView> {

    private Context context;
    public List<Album> requests = new ArrayList<>();

    public AdapterCards(Context context, List<Album> requests){
        this.context = context;
        this.requests = requests;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_albums_social, parent, false);
        return new AdapterView(layoutView);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    @Override
    public void onBindViewHolder(AdapterView holder, int position) {
        Album album = requests.get(position);
        String id = album.getId();
        String name = album.getName();
        String date = album.getCreated_time();
        holder.lbl_id_album.setText(id);
        holder.lbl_name_album.setText(name);
        holder.lbl_date_album.setText(date);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, GridPhotos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_album", requests.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    public class AdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private ItemClickListener clickListener;
        @Bind(R.id.lbl_name_album)TextView lbl_name_album;
        @Bind(R.id.lbl_date_album)TextView lbl_date_album;
        @Bind(R.id.lbl_id_album)TextView lbl_id_album;
        private ItemClickListener clickListener;
        public AdapterView(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition());
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

}