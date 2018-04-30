package com.example.santiago.cuantassabes.adapters;

import android.content.Context;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Category;
import com.example.santiago.cuantassabes.model.Image;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class RecyclerImagesAdapter extends RecyclerView.Adapter<RecyclerImagesAdapter.ImagesViewHolder> {
    private Context mContext;
    private List<Image> mImageList;
    OnImageClick mOnImageClick;

    public interface OnImageClick {
        void setOnImageClick(Image image);
    }

    public RecyclerImagesAdapter(Context context, OnImageClick onImageClick) {
        mContext = context;
        mOnImageClick = onImageClick;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Image image = mImageList.get(position);
        String imageUrl = image.getPhotoUrl();
        Picasso.get().load(Uri.parse(imageUrl)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mImageList) return 0;
        return mImageList.size();
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Image imageClicked = mImageList.get(getAdapterPosition());
            mOnImageClick.setOnImageClick(imageClicked);
        }
    }

    public void setData(List<Image> imageList) {
        mImageList = imageList;
        notifyDataSetChanged();
    }
}
