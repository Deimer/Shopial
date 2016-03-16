package com.puerto.libre.shopial.AdapterViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.puerto.libre.shopial.Objects.RequestImage;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Deimer Villa on 3/7/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterView> {

    private Context context;
    public List<RequestImage> requests = new ArrayList<>();

    public RecyclerAdapter(Context context, List<RequestImage> requests){
        this.context = context;
        this.requests = requests;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photos_social, parent, false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(AdapterView holder, int position) {
        Picasso.with(context).load(requests.get(position).getUrl_image())
                .centerCrop().fit()
                .placeholder(R.drawable.background_image)
                .error(R.drawable.background_image)
                .into(holder.img_photo);
        String description = requests.get(position).getDescription();
        String id_media = requests.get(position).getId_media();
        holder.lbl_detail.setText(description);
        holder.cb_id_media.setId(id_media.hashCode());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class AdapterView extends RecyclerView.ViewHolder {
        @Bind(R.id.img_photo)ImageView img_photo;
        @Bind(R.id.lbl_detail)TextView lbl_detail;
        @Bind(R.id.cb_id_media)CheckBox cb_id_media;
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
