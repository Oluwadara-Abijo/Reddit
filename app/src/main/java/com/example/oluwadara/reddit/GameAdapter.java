package com.example.oluwadara.reddit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameAdapterViewHolder> {

    Context mContext;
    private String[] mGameData;

    /*
     * An on-click handler  for an Activity to interface with the RecyclerView
     */
    private final GameAdapterOnClickHandler mClickHandler;

    /*
     * The interface that receives onClick messages
     */
    public interface GameAdapterOnClickHandler {
        void onClick(String gameData);
    }

    public GameAdapter (Context context, GameAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mClickHandler = onClickHandler;
    }

    /*
     * Cache of the children views for a game list item
     */
    public class GameAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitleTextView;
        TextView mScoreTextView;
        TextView mSubredditTextView;

        public GameAdapterViewHolder (View view) {
            super(view);
            mTitleTextView = view.findViewById(R.id.title_tv);
            mScoreTextView = view.findViewById(R.id.score_tv);
            mSubredditTextView = view.findViewById(R.id.subreddit_tv);
            view.setOnClickListener(this);

        }

        /*
         * Called by the child views during a click
         * @param v - The view that was clicked
         */

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String clickData = mGameData[adapterPosition];
            mClickHandler.onClick(clickData);

        }
    }



    /**
     * Called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  The view type of the item at position for the purposes of view recycling
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public GameAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GameAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, the contents of the ViewHolder is updated to display the game
     * details for this particular position, using the "position" argument that is conveniently
     * passed.
     *
     * @param holder The ViewHolder that should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull GameAdapterViewHolder holder, int position) {
        String gameData = mGameData[position];
        String[] parts = gameData.split("-");

        String title = parts[0];
        String score = parts[1];
        String subreddit = parts[2];
        String url = parts[3];

        holder.mTitleTextView.setText(title);
        holder.mSubredditTextView.setText(subreddit);
        holder.mScoreTextView.setText(score);

    }

    /**
     * This method returns the number of items to display
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mGameData) return 0;
        return mGameData.length;
    }

    /**
     * This method is used to set the game details on a GameAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param gameData The new weather data to be displayed.
     */
    public void setGameData(String[] gameData) {
        mGameData = gameData;
        notifyDataSetChanged();
    }

}
