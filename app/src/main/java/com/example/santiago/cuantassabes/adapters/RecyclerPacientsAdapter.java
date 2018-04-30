package com.example.santiago.cuantassabes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Pacient;
import com.example.santiago.cuantassabes.ui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerPacientsAdapter extends RecyclerView.Adapter<RecyclerPacientsAdapter.PacientViewHolder>{
    private Context mContext;
    private List<Pacient> pacientList;
    OnPacientClick onPacientClick;
    public interface OnPacientClick{
        void setOnPacientClick(Pacient pacient);
    }
    public RecyclerPacientsAdapter(Context context, OnPacientClick onPacientClick){
        mContext = context;
        this.onPacientClick = onPacientClick;
    }

    @NonNull
    @Override
    public PacientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pacient,parent,false);
        return new PacientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PacientViewHolder holder, int position) {
        Pacient pacient = pacientList.get(position);
        String photoUrl = pacient.getPhotoUrl();
        if (!photoUrl.isEmpty()){
            Picasso.get().load(photoUrl).transform(new CircleTransform()).into(holder.pacientPhoto);
        }
        holder.pacientName.setText(pacient.getPacientName());
        holder.pacientAge.setText(pacient.getPacientAge()+" años");
    }

    @Override
    public int getItemCount() {
        if (null ==pacientList) return 0;
        return pacientList.size();
    }

    class PacientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView pacientPhoto;
        final TextView pacientName;
        final TextView pacientAge;
        public PacientViewHolder(View itemView) {
            super(itemView);
            pacientPhoto = itemView.findViewById(R.id.pacient_photo);
            pacientName = itemView.findViewById(R.id.pacient_name);
            pacientAge = itemView.findViewById(R.id.pacient_age);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPacientClick.setOnPacientClick(pacientList.get(getAdapterPosition()));
        }
    }
    public void setData(List<Pacient> pacients){
        pacientList = pacients;
        notifyDataSetChanged();
    }
}
