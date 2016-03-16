package com.puerto.libre.shopial.AdapterViews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hanks.library.AnimateCheckBox;
import com.puerto.libre.shopial.Objects.RequestImage;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Deimer Villa on 3/12/2016.
 */
public class AdapterPhotos extends BaseAdapter {

    private Context context;
    public List<AnimateCheckBox> checkBoxes = new ArrayList<>();
    public List<RequestImage> requests = new ArrayList<>();

    public AdapterPhotos(Context context, List<RequestImage> requests){
        this.context = context;
        this.requests = requests;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public RequestImage getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item_image, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        final RequestImage item = getItem(position);
        Picasso.with(context).load(requests.get(position).getUrl_image())
                .centerCrop().fit()
                .placeholder(R.drawable.background_image)
                .error(R.drawable.background_image)
                .into(holder.img_photo);
        holder.cb_selected.setId(item.getUrl_image().hashCode());
        holder.cb_selected.setTag(item.getId_media());
        addCheckbox(holder.cb_selected);
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.img_photo_grid)ImageView img_photo;
        @Bind(R.id.cb_selected_photo)AnimateCheckBox cb_selected;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void addCheckbox(AnimateCheckBox checkBox){
        boolean flag = true;
        for (int i = 0; i < checkBoxes.size(); i++) {
            int id = checkBoxes.get(i).getId();
            if(id == checkBox.getId()){
                flag = false;
                break;
            }
        }
        if(flag){
            checkBoxes.add(checkBox);
        }
    }

}