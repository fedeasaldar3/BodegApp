package com.example.bodegapp.Objetos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bodegapp.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ProductosViewHolder>{

    List<Producto> productos;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void  onDeleteClick(int position);
        void  onEditClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public Adapter(List<Producto> productos) {
        this.productos = productos;
    }


    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler, viewGroup, false);
        ProductosViewHolder holder = new ProductosViewHolder(v, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder productosViewHolder, int i) {
        Producto producto = productos.get(i);
        productosViewHolder.tv_referencia.setText(producto.getReferencia());
        productosViewHolder.tv_bodega.setText(producto.getLugar());
        //int val = producto.getPiso();
        productosViewHolder.tv_piso.setText(String.valueOf(producto.getPiso()));
        productosViewHolder.tv_cama.setText(producto.getCama());
        productosViewHolder.tv_parte.setText(producto.getParte());

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView tv_referencia, tv_bodega, tv_piso, tv_cama, tv_parte;
        public ImageView iDelete, iEdit;

        public ProductosViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tv_referencia = (TextView) itemView.findViewById(R.id.text_referencia);
            tv_bodega = (TextView) itemView.findViewById(R.id.text_bodega);
            tv_piso = (TextView) itemView.findViewById(R.id.text_piso);
            tv_cama = (TextView) itemView.findViewById(R.id.text_cama);
            tv_parte = (TextView) itemView.findViewById(R.id.parte2);

            iDelete = (ImageView) itemView.findViewById(R.id.iDelete);
            iEdit = (ImageView) itemView.findViewById(R.id.iEdit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            iDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            iEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

        }

    }

    public void filterList(ArrayList<Producto> filteredList){
        productos = filteredList;
        notifyDataSetChanged();
    }

}
