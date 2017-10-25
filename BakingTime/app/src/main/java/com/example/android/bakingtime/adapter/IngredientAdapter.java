package com.example.android.bakingtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Amir on 10/13/2017.
 */

public class IngredientAdapter extends ArrayAdapter<Recepie.Ingredient> {

    private Context mContext;
    public final ArrayList<Recepie.Ingredient> mIngredients;

    public IngredientAdapter(Activity context, ArrayList<Recepie.Ingredient> ingredients) {
        super(context, 0, ingredients);
        mContext = context;
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        View listItemView = convertView;
        if( listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ingredient_list_item, parent, false);
            viewHolder = new ViewHolder(listItemView);
            listItemView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) listItemView.getTag();

        Recepie.Ingredient currentIngredient = mIngredients.get(position);
        // to concatenating order number in front of the ingredients:
        int ingredientOrderNumber = position + 1;
        String orderNumberAndQuantity = String.valueOf(ingredientOrderNumber)+". " +
                String.valueOf(currentIngredient.getQuantity());
        String measaure = currentIngredient.getMeasure() + " of ";

        viewHolder.quantityText.setText(orderNumberAndQuantity);
        viewHolder.mesaureText.setText(measaure);
        viewHolder.ingredientText.setText(currentIngredient.getSingleIngredient());

        return listItemView;
    }

    static class ViewHolder{
        @BindView(R.id.tv_quantity) TextView quantityText;
        @BindView(R.id.tv_mesaure) TextView mesaureText;
        @BindView(R.id.tv_ingredient_name) TextView ingredientText;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }


    }

}
