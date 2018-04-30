package com.example.santiago.cuantassabes.fragments.pacient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santiago.cuantassabes.ImagesActivity;
import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.adapters.RecyclerCategoryAdapter;
import com.example.santiago.cuantassabes.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryList extends Fragment implements RecyclerCategoryAdapter.OnClickCategory{
    private static final String TAG = CategoryList.class.getSimpleName();
    @BindView(R.id.recycler_age)
    RecyclerView recyclerAge;
    private RecyclerCategoryAdapter recyclerCategoryAdapter;
    private List<Category> categoryList;

    public CategoryList(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerAge.setLayoutManager(linearLayoutManager);
        recyclerAge.setHasFixedSize(true);
        recyclerCategoryAdapter = new RecyclerCategoryAdapter(getContext(),this);
        recyclerAge.setAdapter(recyclerCategoryAdapter);
        recyclerCategoryAdapter.setData(categoryList);
        return rootView;
    }

    public void setData(List<Category> categories){
        categoryList = categories;
    }

    @Override
    public void setOnClickCategory(Category category) {
        String ageParent = category.getAgeParent();
        String categoryName = category.getCategoryName();
        String categoryPhotoUrl =category.getPhotoUrl();
        Intent intent = new Intent(getActivity(), ImagesActivity.class);
        intent.putExtra("name",categoryName);
        intent.putExtra("photo_url",categoryPhotoUrl);
        intent.putExtra("age",ageParent);
        startActivity(intent);
    }
}
