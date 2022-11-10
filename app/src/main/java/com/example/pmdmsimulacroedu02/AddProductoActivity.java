package com.example.pmdmsimulacroedu02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pmdmsimulacroedu02.databinding.ActivityAddProductoBinding;
import com.example.pmdmsimulacroedu02.modelos.ProductoModel;

public class AddProductoActivity extends AppCompatActivity {

    private ActivityAddProductoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //ACTIVIDAD DE CREAR, RECOGE LA INFORMACION QUE ESTA EN LOS TEXT Y LOS ENVIA CON EL BUNDLE Y EL INTENT
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAgregarProductoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = binding.txtNombreProductoAdd.getText().toString();
                String cantidadS = binding.txtCantidadProductoAdd.getText().toString();
                String precioS = binding.txtPrecioProductoAdd.getText().toString();
                if(!nombre.isEmpty() && !cantidadS.isEmpty() && !precioS.isEmpty()){
                    ProductoModel p = new ProductoModel(nombre,Integer.parseInt(cantidadS), Float.parseFloat(precioS));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PROD", p);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}