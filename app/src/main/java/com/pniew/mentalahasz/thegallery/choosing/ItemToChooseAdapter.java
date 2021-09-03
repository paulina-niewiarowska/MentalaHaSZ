package com.pniew.mentalahasz.thegallery.choosing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.repository.AThing;


public class ItemToChooseAdapter<T extends AThing> extends ListAdapter<T, ItemToChooseAdapter<T>.ItemHolder> {

    private onItemClickListener<T> listener;

    public interface onItemClickListener<T> {
        void onItemClick(T item);
    }
    //will be used on the activity
    public void setOnItemClickListener(onItemClickListener<T> listener){this.listener = listener; }


    //constructor with methods that allow reaction to any change in the Observers
    protected ItemToChooseAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return (oldItem.getId() == newItem.getId() && oldItem.getClass() == newItem.getClass());
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem.equals(newItem);
            }
        });
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_choose, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(getItem(position) instanceof ArtPeriod){
            ArtPeriod currentPeriod = (ArtPeriod) getItem(position);
            holder.nameOfTheItem.setText(currentPeriod.getArtPeriodName());
            holder.datingOfTheItem.setText(currentPeriod.getArtPeriodDating());

        } else if(getItem(position) instanceof Movement){
            Movement currentMovement = (Movement) getItem(position);
            holder.nameOfTheItem.setText(currentMovement.getMovementName());
            holder.datingOfTheItem.setText(currentMovement.getMovementDating());

        } else if (getItem(position) instanceof Type){
            Type currentType = (Type) getItem(position);
            holder.nameOfTheItem.setText(currentType.getTypeName());
            holder.datingOfTheItem.setVisibility(View.GONE);
        }
    }

    public T getTAt(int position){
        return getItem(position);
    }



// ITEM HOLDER NESTED CLASS



    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView nameOfTheItem;
        private TextView datingOfTheItem;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            nameOfTheItem = itemView.findViewById(R.id.text_view_item_to_choose_name);
            datingOfTheItem = itemView.findViewById(R.id.text_view_item_to_choose_dating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }



}
