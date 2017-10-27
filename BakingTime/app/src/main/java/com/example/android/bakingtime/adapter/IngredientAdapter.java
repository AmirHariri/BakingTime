package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Recepie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Amir on 10/13/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context mContext;
    public final ArrayList<Recepie.Ingredient> mIngredients;

    public IngredientAdapter(Activity context, ArrayList<Recepie.Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recepieView = inflater.inflate(R.layout.ingredient_list_item,parent,false);
        return new IngredientViewHolder(recepieView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {

        Recepie.Ingredient currentIngredient = mIngredients.get(position);
        // to concatenating order number in front of the ingredients:
        int ingredientOrderNumber = position + 1;
        String orderNumberAndQuantity = String.valueOf(ingredientOrderNumber)+". " +
                String.valueOf(currentIngredient.getQuantity());
        String measaure = currentIngredient.getMeasure() + " of ";

        holder.quantityText.setText(orderNumberAndQuantity);
        holder.mesaureText.setText(measaure);
        holder.ingredientText.setText(currentIngredient.getSingleIngredient());
    }




    @Override
    public int getItemCount() {
        return mIngredients.size();
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_quantity) TextView quantityText;
        @BindView(R.id.tv_mesaure) TextView mesaureText;
        @BindView(R.id.tv_ingredient_name) TextView ingredientText;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        }

}
