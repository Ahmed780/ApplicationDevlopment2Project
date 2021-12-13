package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ImageViewHolder> {
    private Context mContext;
    private ArrayList<AdModel> list;
    private OnItemClickListener mListener;

    public MyAdapter2(Context context, ArrayList<AdModel> uploads) {
        mContext = context;
        list = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_ad2, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        AdModel user = list.get(position);
        holder.textViewtitle.setText(user.getTitle());
        holder.textViewprice.setText(user.getPrice());
        holder.textViewdescription.setText(user.getDescription());
        Picasso.get().load(user.getImageUri()).into(holder.imageviews);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,ContactSeller.class);
                intent.putExtra("title",user.getTitle());
                intent.putExtra("price",user.getPrice());
                intent.putExtra("description",user.getDescription());
                intent.putExtra("imageUri",user.getImageUri());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewtitle, textViewprice, textViewdescription;
        public ImageView imageviews;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewtitle = itemView.findViewById(R.id.title_text2);
            textViewprice = itemView.findViewById(R.id.price_text2);
            textViewdescription = itemView.findViewById(R.id.description_text2);
            imageviews = itemView.findViewById(R.id.imageUpload2);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                     mListener.onDeleteClick(position);
                            return true;
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}