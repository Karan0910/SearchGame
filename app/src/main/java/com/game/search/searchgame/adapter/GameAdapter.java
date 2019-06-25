package com.game.search.searchgame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import com.game.search.searchgame.R;
import com.game.search.searchgame.model.Game;

/**
 * Created by Karan on 25-06-2019.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Game> gameList;
    private List<Game> gameListFiltered;
    private GameAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, genre,ratingCount;
        public ImageView thumbnail;
        public RatingBar ratingBar;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            genre = view.findViewById(R.id.genre);
            thumbnail = view.findViewById(R.id.thumbnail);
            ratingCount= view.findViewById(R.id.rating_count);
            ratingBar= view.findViewById(R.id.ratingBar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected game in callback
                    listener.onGameSelected(gameListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public GameAdapter(Context context, List<Game> gameList, GameAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.gameList = gameList;
        this.gameListFiltered = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    // setting data to each row item field
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Game game = gameListFiltered.get(position);
        holder.name.setText(game.getTitle());
        holder.genre.setText(game.getGenre());
        holder.ratingCount.setText(game.getrCount());
        holder.ratingBar.setNumStars(Integer.parseInt(game.getRating()));

        Glide.with(context)
                .load(game.getImgURL())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return gameListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    gameListFiltered = gameList;
                } else {
                    List<Game> filteredList = new ArrayList<>();
                    for (Game row : gameList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getGenre().contains(charSequence)||
                                row.getSubgenre().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    gameListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = gameListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                gameListFiltered = (ArrayList<Game>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface GameAdapterListener {
        void onGameSelected(Game game);
    }
}