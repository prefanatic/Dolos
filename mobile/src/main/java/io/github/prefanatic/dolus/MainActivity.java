package io.github.prefanatic.dolus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.echonest.api.v4.Song;
import com.google.android.gms.wearable.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.uri.egr.hermes.Hermes;
import edu.uri.egr.hermeswear.HermesWearable;
import io.github.prefanatic.dolus.widget.HeartRateView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cody on 12/8/15.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.check) Button checkButton;
    @Bind(R.id.heart_rate) HeartRateView heartRateView;
    @Bind(R.id.info_text) TextView infoText;
    @Bind(R.id.recycler) RecyclerView recycler;

    private Subscription busStop;
    private SongAdapter adapter;
    private List<String> songFilterList = new ArrayList<>();
    private int collectiveHr;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new SongAdapter(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        busStop = Hermes.Bus.observe(Application.BUS_HR, Integer.class)
                .take(5)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(this::searchForSongs)
                .doOnSubscribe(() -> collectiveHr = 0)
                .subscribe(hr -> {
                    collectiveHr += hr;
                    heartRateView.setRate(hr);
                });

        /*
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .take(5)
                .doOnCompleted(() -> {
                    collectiveHr = 500;
                    searchForSongs();
                })
                .subscribe(l -> {
                    b++;
                    heartRateView.setRate(b);
                });
        */
    }

    private void searchForSongs() {
        collectiveHr /= 5;
        queryHeartRate(false);
        infoText.setText("Loading songs...");

        BpmSearch.getTopSongsByTempo(collectiveHr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(list -> list)
                .doOnSubscribe(() -> songFilterList.clear())
                .filter(song -> !songFilterList.contains(song.getReleaseName()))
                .doOnNext(song -> songFilterList.add(song.getReleaseName()))
                .toList()
                .subscribe(list -> {
                    Collections.sort(list, comparator);
                    adapter.setSongs(list);

                    AnimUtils.hide(infoText);
                    AnimUtils.show(recycler);
                });
    }

    @Override
    protected void onDestroy() {
        if (busStop != null && !busStop.isUnsubscribed())
            busStop.unsubscribe();

        super.onDestroy();
    }

    @OnClick(R.id.check)
    public void onCheckClicked() {
        queryHeartRate(true);

        AnimUtils.hide(checkButton);
        AnimUtils.show(infoText);
        infoText.setText("Checking your heart rate...");
    }

    private void queryHeartRate(boolean enabled) {
        HermesWearable.Node.getNodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Node::getId)
                .flatMap(id -> HermesWearable.Message.sendMessage(id, enabled ? "query-hr" : "stop"))
                .subscribe();
    }

    Comparator<Song> comparator = (lhs, rhs) -> {
        try {
            return Double.compare(rhs.getTempo(), lhs.getTempo());
        } catch (Exception e) {
            return -1;
        }
    };
}
