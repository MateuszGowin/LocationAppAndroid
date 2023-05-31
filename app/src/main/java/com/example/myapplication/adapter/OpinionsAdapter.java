package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Opinion;

import java.text.SimpleDateFormat;
import java.util.List;

public class OpinionsAdapter extends RecyclerView.Adapter<OpinionsAdapter.ViewHolder> {

    private List<Opinion> opinions;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_name;
        private final TextView tv_date;
        private final TextView tv_opinion;
        private final RatingBar rb_rate;
        public ViewHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CommentsAdpter","Element" + getAdapterPosition() + " clicked.");
                }
            });
            tv_name = (TextView) v.findViewById(R.id.user_name);
            tv_date = (TextView) v.findViewById(R.id.opinion_date);
            tv_opinion = (TextView) v.findViewById(R.id.user_opinion);
            rb_rate = (RatingBar) v.findViewById(R.id.user_rate);
        }

        public TextView getTv_name() {
            return tv_name;
        }

        public TextView getTv_date() {
            return tv_date;
        }

        public TextView getTv_opinion() {
            return tv_opinion;
        }

        public RatingBar getRb_rate() {
            return rb_rate;
        }
    }

    public OpinionsAdapter(List<Opinion> opinions) {
        this.opinions = opinions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment_view, viewGroup , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Opinion opinion = opinions.get(position);
        String user = opinion.getUser().getFirstName() + " " + opinion.getUser().getLastName();
        viewHolder.getTv_name().setText(user);
        viewHolder.getRb_rate().setRating((float) opinion.getRate());
        String date = new SimpleDateFormat("dd-MM-yyyy").format(opinion.getUpload_date());
        viewHolder.getTv_date().setText(date);
        viewHolder.getTv_opinion().setText(opinion.getComment());
    }

    @Override
    public int getItemCount() {
        return opinions.size();
    }
}
