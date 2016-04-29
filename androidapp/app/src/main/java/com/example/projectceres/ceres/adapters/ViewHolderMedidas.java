package com.example.projectceres.ceres.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectceres.ceres.R;


/**
 * Created by Proyectos on 24/4/16.
 */
public class ViewHolderMedidas extends RecyclerView.ViewHolder{

    //region Variables
    public ImageView poster;
    public TextView titulo;
    public TextView valor;
    //endregion

    //region Funciones
    //region Constructores
    public ViewHolderMedidas(View view) {
        super(view);

        this.poster = (ImageView) view.findViewById(R.id.poster_pelicula);
        this.titulo = (TextView) view.findViewById(R.id.titulo_categoria);
        this.valor = (TextView) view.findViewById(R.id.titulo_pelicula);
    }
    //endregion
    //endregion
}