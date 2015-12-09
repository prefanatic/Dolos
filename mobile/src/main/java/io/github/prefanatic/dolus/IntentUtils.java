package io.github.prefanatic.dolus;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by cody on 11/30/15.
 */
public class IntentUtils {
    public static void playMusic(Context context, String artist, String album, String song) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);

        // We're looking for a song.
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Media.ENTRY_CONTENT_TYPE);

        // Specify our arguments.
        intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artist);
        intent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM, album);
        intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, song);

        // Pass a legacy search query because Google.
        intent.putExtra(SearchManager.QUERY, artist + " " + album + " " + song);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
