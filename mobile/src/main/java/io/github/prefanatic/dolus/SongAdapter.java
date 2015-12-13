package io.github.prefanatic.dolus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echonest.api.v4.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    public List<Song> songs = new ArrayList<>();
    private final Context context;

    public SongAdapter(Context context) {
        this.context = context;
    }

    public void setSongs(List<Song> songs) {
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_song, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        //Timber.d("Song Image: %d", song.getReleaseImage());
        holder.name.setText(song.getReleaseName());
        holder.artist.setText(song.getArtistName());

        try {
            holder.bpm.setText(String.format("%.0f BPM", song.getTempo()));
        } catch (Exception e) {
            //Timber.e(e, "Wow");
            holder.bpm.setText("Unknown BPM");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.song_name) TextView name;
        @Bind(R.id.song_artist) TextView artist;
        @Bind(R.id.song_bpm) TextView bpm;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                Song song = songs.get(getAdapterPosition());

                IntentUtils.playMusic(context, song.getArtistName(), song.getAudio(), song.getReleaseName());
            });
        }
    }
}
