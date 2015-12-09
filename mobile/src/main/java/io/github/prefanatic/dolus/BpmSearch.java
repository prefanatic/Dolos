package io.github.prefanatic.dolus;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by cody on 11/18/15.
 */
public class BpmSearch {
    private static final String API_KEY = "IX2YB7HFFXVRELRNS";
    private static final EchoNestAPI echo = new EchoNestAPI(API_KEY);
    private static final float tempoRange = 0.1f;
    private static final int BPM = 60;

    public static Observable<List<Artist>> searchForSong(String songName) {
        return Observable.create(new Observable.OnSubscribe<List<Artist>>() {
            @Override
            public void call(Subscriber<? super List<Artist>> subscriber) {
                try {
                    subscriber.onNext(echo.searchArtists("Arcade Fire"));
                } catch (EchoNestException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<List<Song>> getTopSongsByTempo() {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                try {
                    SongParams params = new SongParams();
                    params.setResults(100);
                    params.sortBy("song_hotttnesss", false);

                    params.setMaxTempo(BPM + (BPM * tempoRange));
                    params.setMinTempo(BPM - (BPM * tempoRange));

                    subscriber.onNext(echo.searchSongs(params));
                } catch (EchoNestException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
