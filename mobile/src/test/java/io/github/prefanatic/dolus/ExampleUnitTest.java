package io.github.prefanatic.dolus;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;

import org.junit.Test;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;

import static org.junit.Assert.assertEquals;

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
        Song song = BpmSearch.getTopSongsByTempo(50)
                .flatMapIterable(songs -> songs)
                .toBlocking()
                .first();

        try {
            System.out.println(song.getReleaseName() + " has a tempo of " + song.getTempo() + " BPM");
        } catch (EchoNestException e) {
            System.out.println("WOW FUCK " + e.getMessage());
        }
    }
}