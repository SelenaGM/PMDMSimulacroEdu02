package com.example.pmdmsimulacroedu02;

import android.content.Intent;
import android.os.Bundle;

import com.example.pmdmsimulacroedu02.adapters.ProductosModelAdapter;
import com.example.pmdmsimulacroedu02.modelos.ProductoModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.pmdmsimulacroedu02.databinding.ActivityMainBinding;

import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //PARA RECOGER EL OBJETO DEL ADDPRODUCTACTIVITY
    private ArrayList<ProductoModel> productoModelsList;
    private ActivityResultLauncher<Intent> launcherAddProducto;

//CUANDO YA TENEMOS EL ADAPTER Y EL RECYCLER
    private ProductosModelAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    //NumberFormat
    private NumberFormat numberFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        numberFormat = NumberFormat.getCurrencyInstance();


        setSupportActionBar(binding.toolbar);
        productoModelsList = new ArrayList<>();
        //LLAMAMOS UNA VEZ DESPUES DE INSTANCIAR EL ARRAYLIST
        calculaValores();
        //DESPUES DE INICIALIZAR LA LISTA SERA EL MOMENTO DE INICIALIZAR EL ADAPTER; ANTES NO O SERA NULL LA LISTA
        adapter = new ProductosModelAdapter(productoModelsList, R.layout.producto_view_holder, this);
        //el 1 es una columna
        layoutManager = new GridLayoutManager(this,1);

        binding.contentMain.contenedor.setLayoutManager(layoutManager);
        binding.contentMain.contenedor.setAdapter(adapter);


        inicializaLaunchers();



        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherAddProducto.launch(new Intent(MainActivity.this, AddProductoActivity.class));
            }
        });
    }

    private void inicializaLaunchers() {
        launcherAddProducto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            if(result.getData() != null && result.getData().getExtras() != null){
                                ProductoModel p = (ProductoModel) result.getData().getExtras().getSerializable("PROD");
                                productoModelsList.add(p);
                                //PARA ENSEÑARNOS
                                adapter.notifyItemInserted(productoModelsList.size()-1);
                                //CALCULA VALORES CUANDO INSERTO UN ELEMENTO A LA LISTA
                                calculaValores();
                            }
                        }
                    }
                }
        );
    }
    //EN ESTA FUNCION PODRAN ACCEDER TANTO EL MAIN COMO EL ADAPTER
    public void calculaValores(){
        int cantidad = 0 ;
        float precio = 0;

        for (ProductoModel p: productoModelsList) {
            cantidad += p.getCantidad();
            precio += p.getCantidad() * p.getPrecio();

        }

        binding.contentMain.lblCantidadTotalMain.setText(String.valueOf(cantidad));
        binding.contentMain.lblPrecioTotalMain.setText(numberFormat.format(precio));

    }
}