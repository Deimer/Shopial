package com.puerto.libre.shopial.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Controllers.StoreController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.Store;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creado por Deimer Villa on 3/5/2016.
 */
public class TabProfile extends Fragment {

    //region Variables de adecuacion
    private Context context;
    private UserController userController;
    private SocialController socialController;
    private StoreController storeController;
    private SweetDialog dialog;
    private Validate validate;
    //endregion

    //region Elementos de la vista
    @Bind(R.id.img_header)ImageView img_header;
    @Bind(R.id.profile_image)CircleImageView profile_image;
    @Bind(R.id.lbl_name_user)TextView lbl_name;
    @Bind(R.id.lbl_name_store)TextView lbl_name_store;
    @Bind(R.id.lbl_username)TextView lbl_username;
    @Bind(R.id.lbl_description_profile)TextView lbl_description_profile;
    @Bind(R.id.lbl_number_followers)TextView lbl_number_followers;
    @Bind(R.id.lbl_number_followed)TextView lbl_number_followed;
    @Bind(R.id.lbl_number_likes)TextView lbl_number_products;
    //endregion

    public static TabProfile newInstance() {
        return new TabProfile();
    }

    public TabProfile(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        return view;
    }

    public void setupContext(){
        context = getActivity().getApplicationContext();
        userController = new UserController(context);
        socialController = new SocialController(context);
        storeController = new StoreController(context);
        dialog = new SweetDialog(getActivity());
        validate = new Validate(context);
        getNumbers();
        loadProfile();
    }

    public void loadProfile(){
        User user = userController.show(context);
        System.out.println(user.toString());
        Social social = socialController.show(context);
        Store store = storeController.show(context);
        if(Integer.toString(user.getProfile_id()).equalsIgnoreCase("2")){
            profileSeller(user, store);
        } else if(Integer.toString(user.getProfile_id()).equalsIgnoreCase("3")) {
            profileBuyer(user, social);
        }
    }

    public void profileBuyer(User user, Social social){
        String image_profile = validate.validateStringNull(user.getImage_profile_url());
        if(socialController.session()){
            String image_header = social.getAvatar();
            loadHeaderProfile(image_header);
            lbl_name.setText(social.getFull_name());
            lbl_username.setText(user.getUsername());
        } else {
            loadHeaderNative();
            lbl_name.setText(user.full_name());
            lbl_username.setText(user.getUsername());
        }
        loadPhotoProfile(image_profile);
        lbl_name_store.setVisibility(View.GONE);
        lbl_description_profile.setVisibility(View.GONE);
    }

    public void profileSeller(User user, Store store){
        String image_profile = user.getImage_profile_url();
        loadPhotoProfile(image_profile);
        loadHeaderNative();
        lbl_name_store.setText(store.getStore_name());
        lbl_username.setText(user.getUsername());
        lbl_description_profile.setText(store.getDescription_products());
        lbl_name.setVisibility(View.GONE);
    }

    public void loadPhotoProfile(String imageUrl){
        Picasso.with(getActivity())
                .load(imageUrl)
                .centerCrop()
                .fit()
                .placeholder(R.drawable.user)
                .error(R.drawable.user_error)
                .into(profile_image);
    }

    public void loadHeaderProfile(String imageUrl){
        Picasso.with(context)
                .load(imageUrl)
                .transform(new BlurTransformation(context, 25, 1))
                .centerCrop()
                .fit()
                .error(R.drawable.background_image)
                .into(img_header);
    }

    public void loadHeaderNative(){
        Picasso.with(context)
                .load(R.drawable.background_image)
                .transform(new BlurTransformation(context, 25, 1))
                .centerCrop()
                .fit()
                .into(img_header);
    }

    public void setupLabels(String followers, String likes){
        lbl_number_followers.setText(followers);
        lbl_number_products.setText(likes);
    }

    public void getNumbers(){
        dialog.dialogProgress(getString(R.string.get_data));
        final String url = getString(R.string.url_test);
        int user_id = userController.show(context).getUser_id();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.getNumbers(user_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                String followers = jsonObject.get("followers").getAsString();
                String likes = jsonObject.get("likes").getAsString();
                setupLabels(followers,likes);
                dialog.cancelarProgress();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("TabProfile(getNumbers)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("TabProfile(getNumbers)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

}
