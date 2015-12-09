package io.github.prefanatic.dolus;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;

import org.junit.Test;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void echoNest() {
        BpmSearch.searchForSong("LOL")
                .flatMapIterable(new Func1<List<Artist>, Iterable<Artist>>() {
                    @Override
                    public Iterable<Artist> call(List<Artist> artists) {
                        return artists;
                    }
                })
                .subscribe(new Action1<Artist>() {
                    @Override
                    public void call(Artist artist) {
                        System.out.println("Found the artist: " + artist);
                    }
                });
    }

    @Test
    public void getGoodShit() {
        BpmSearch.getTopSongsByTempo()
                .flatMapIterable(new Func1<List<Song>, Iterable<Song>>() {
                    @Override
                    public Iterable<Song> call(List<Song> songs) {
                        return songs;
                    }
                })
                .subscribe(new Action1<Song>() {
                    @Override
                    public void call(Song song) {
                        try {
                            System.out.println(song.getReleaseName() + " has a tempo of " + song.getTempo() + " BPM");
                        } catch (EchoNestException e) {
                            System.out.println("WOW FUCK " + e.getMessage());
                        }
                    }
                });
    }
}