package com.example.projectceres.ceres.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.projectceres.ceres.R;
import com.example.projectceres.ceres.model.Medidas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 8/9/15.
 */
public class AdapterRecyclerPeliculas extends RecyclerView.Adapter<ViewHolderMedidas> {

    //region Variables
    private List<Medidas> items;
    private Context context;
    //endregion

    //region Funciones
    //region Constructores
    public AdapterRecyclerPeliculas(List<Medidas> items, Context context) {
        this.items = items;
        this.context = context;
    }
    //endregion

    //region Funciones del Recycler
    @Override
    public ViewHolderMedidas onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_pelicula, viewGroup, false);

        return new ViewHolderMedidas(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMedidas viewHolderPeliculas, int i) {

        /*Glide.with(this.context)
                .load(this.items.get(i).getPoster())
                .override(350, 400)
                .animate(android.R.anim.slide_in_left)
                .into(viewHolderPeliculas.poster);

        viewHolderPeliculas.titulo.setText(this.items.get(i).getTitulo());*/



    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    //endregion

    //region Funciones auxiliares
    public void addAll(ArrayList<Medidas> peliculas) {
        if (peliculas == null)
            throw new NullPointerException("No puedes pasar una lista nula");

        this.items.addAll(peliculas);
        this.notifyDataSetChanged();
    }
    //endregion
    //endregion
}