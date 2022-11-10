package com.example.pmdmsimulacroedu02.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmdmsimulacroedu02.MainActivity;
import com.example.pmdmsimulacroedu02.R;
import com.example.pmdmsimulacroedu02.modelos.ProductoModel;

import java.text.NumberFormat;
import java.util.List;

//IMPLEMENTAR LOS METODOS DEL ADAPTER
public class ProductosModelAdapter extends RecyclerView.Adapter<ProductosModelAdapter.ProductoVH> {

    private List<ProductoModel> objects;
    private int resource;
    private Context context;

    private NumberFormat nf;
    private MainActivity main;

    //CONSTRUCTOR
    public ProductosModelAdapter(List<ProductoModel> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        nf = NumberFormat.getCurrencyInstance();
        //Esto es para instanciar un MainActivity y poder llamar a la función calculaValores desde aquí.
        main = (MainActivity) context;
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ESTE SIEMPRE ES IGUAL
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        productoView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {

        ProductoModel p = objects.get(position);
        holder.lblNombre.setText(p.getNombre());
        holder.lblCantidad.setText(String.valueOf(p.getCantidad()));
        holder.lblPrecio.setText(nf.format(p.getPrecio()));

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pasale el producto y la posición con el adapter.
                confirmDelete(p, holder.getAdapterPosition()).show();
            }
        });
        //ESTO ES PARA EDITAR
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProducto(p,holder.getAdapterPosition()).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return objects.size();
    }
    
    private AlertDialog editProducto(ProductoModel p, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(p.getNombre());
        
        //PARA PODER APROVECHAR LA VISTA DE AGREGAR
        View productoView = LayoutInflater.from(context).inflate(R.layout.activity_add_producto,null);
        
        //Creamos las variables y les añadimos el valor nuevo.
        EditText txtNombre = productoView.findViewById(R.id.txtNombreProductoAdd);
        txtNombre.setVisibility(View.GONE);    //con el setVisibility GONE ni está visible ni ocupa el espacio    
        Button btn = productoView.findViewById(R.id.btnAgregarProductoAdd);
        btn.setVisibility(View.GONE);
        EditText txtCantidad = productoView.findViewById(R.id.txtCantidadProductoAdd);
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        EditText txtPrecio = productoView.findViewById(R.id.txtPrecioProductoAdd);
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        
        //para que se muestre como le cuerpo del alert
        builder.setView(productoView);
        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()){
                    //SI LE DAS A MODIFICAR TE CAMBIARA LA CANTIDAD
                    p.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    p.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    notifyItemChanged(position);
                    //CALCULA VALORES CUANDO MODIFICAMOS UN ELEMENTO
                    main.calculaValores();

                }else{
                    Toast.makeText(context, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    private AlertDialog confirmDelete(ProductoModel p, int position){
        //ESTO PARA QUE SALGA EL MENSAJE DE CONFIRMACIoN Y SI LE DAS A SI QUE BORRE EN LA FUNCION
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Estás seguro?");
        builder.setCancelable(false);
        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objects.remove(p);
                notifyItemRemoved(position);
                //CALCULA VALORES CUANDO ELIMINAMOS UN ELEMENTO
                main.calculaValores();

            }
        });
        return builder.create();
    }

    public class ProductoVH extends RecyclerView.ViewHolder {

      TextView lblNombre, lblCantidad, lblPrecio;
      ImageButton btnEliminar;



        //CREAMOS EL CONSTRUCTOR del productoVH
        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            //El itemview es quien tiene el xml inflado
            lblNombre = itemView.findViewById(R.id.lblNombreProductoCard);
            lblCantidad = itemView.findViewById(R.id.lblCantidadProductoCard);
            lblPrecio = itemView.findViewById(R.id.lblPrecioProductoCard);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProductoCard);
        }
    }


}
