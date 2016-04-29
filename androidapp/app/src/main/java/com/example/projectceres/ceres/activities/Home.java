package com.example.projectceres.ceres.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.projectceres.ceres.R;
import com.example.projectceres.ceres.adapters.AdapterFragmentsDetalles;
import com.example.projectceres.ceres.efectos.DepthPageTransformer;
import com.example.projectceres.ceres.fragments.ChartsFragment;
import com.example.projectceres.ceres.fragments.MapsFragment;

public class Home extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        this.context = this.getBaseContext();

        this.detallesViewPager();
    }

    private void detallesViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterFragmentsDetalles adapter = new AdapterFragmentsDetalles(getSupportFragmentManager());
        adapter.addFrag(new MapsFragment(), "MapsFragment");
        adapter.addFrag(new ChartsFragment(), "Charts");
        //adapter.addFrag(new FragmentoPeliculasActores(), "Actores");
        //adapter.addFrag(new FragmentoPeliculasImagenes(), "Imagenes");
        viewPager.setAdapter(adapter);
    }
}
