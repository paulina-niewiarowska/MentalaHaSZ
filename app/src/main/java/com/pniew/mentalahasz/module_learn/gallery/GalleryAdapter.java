package com.pniew.mentalahasz.module_learn.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.Picture;

public class GalleryAdapter extends ListAdapter<Picture, GalleryAdapter.PictureHolder> {

    OnPictureClickListener listener;

    private static final DiffUtil.ItemCallback<Picture> DIFF_CALLBACK = new DiffUtil.ItemCallback<Picture>() {
        @Override
        public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
            return (oldItem.getPictureId() == newItem.getPictureId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected GalleryAdapter() {
        super(DIFF_CALLBACK);
    }

    public interface OnPictureClickListener {
        void onPictureClick(Picture p);
    }
    public void setOnPictureClickListener(OnPictureClickListener listener){
        this.listener = listener;
    }



    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture_to_load, parent, false);
        return new PictureHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder holder, int position) {
        Picture currentPicture = getItem(position);
        Glide.with(holder.imageView.getContext())
                .load(currentPicture.getPicturePath())
                .dontAnimate()
                .into(holder.imageView);
//        Bitmap bitmap = currentPicture.getBitmap();
//        holder.imageView.setImageBitmap(bitmap);
        int currentKnowledge = currentPicture.getPictureKnowledgeDegree();
        holder.setAlphaForImage(currentKnowledge);
    }

    class PictureHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private int transparency;
        private int alpha;

        public void setAlphaForImage(int currentKnowledge){
            //255 * 0.X = % alpha
            //255 - opaque, 0 - fully transparent
            alpha = (int) ((currentKnowledge*(255-40))/10f + 40); // alpha > 40 & < 255
            if(alpha > 255){alpha = 255;}
            if(alpha < 0){alpha = 0;}
            imageView.setImageAlpha(alpha);
        }

        public PictureHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION){
                    listener.onPictureClick(getItem(position));
                }
            });

        }
    }
}
