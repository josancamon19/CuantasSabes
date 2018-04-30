package com.example.santiago.cuantassabes.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.AgeViewHolder>{
    private List<Category> categoryList;
    private Context context;
    private OnClickCategory onClickCategory;
    public interface OnClickCategory{
        void setOnClickCategory(Category category);
    }
    public RecyclerCategoryAdapter(Context context,OnClickCategory onClickCategory){
        this.context = context;
        this.onClickCategory = onClickCategory;
    }
    @NonNull
    @Override
    public AgeViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category,parent,false);
        return new AgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgeViewHolder holder, int position) {
        Category category = categoryList.get(position);
        String imageUrl = category.getPhotoUrl();
        String ageRange = category.getCategoryName();
        Picasso.get().load(Uri.parse(imageUrl)).into(holder.imageView);
        holder.ageTextView.setText(ageRange);
    }

    @Override
    public int getItemCount() {
        if (null == categoryList) return 0;
        return categoryList.size();
    }

    class AgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    final ImageView imageView;
    final TextView ageTextView;
        public AgeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.age_image);
            ageTextView = itemView.findViewById(R.id.age_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickCategory.setOnClickCategory(categoryList.get(getAdapterPosition()));
        }
    }
    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
}
