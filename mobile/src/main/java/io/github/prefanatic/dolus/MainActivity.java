package io.github.prefanatic.dolus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.wearable.Node;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.uri.egr.hermes.Hermes;
import edu.uri.egr.hermeswear.HermesWearable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cody on 12/8/15.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.test) Button testButton;

    private Subscription busStop;
    private boolean enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        busStop = Hermes.Bus.observe(Application.BUS_HR, Float.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hr -> {
                    testButton.setText(String.format("%f", hr));
                });
    }

    @Override
    protected void onDestroy() {
        if (busStop != null && !busStop.isUnsubscribed())
            busStop.unsubscribe();

        super.onDestroy();
    }

    @OnClick(R.id.test)
    public void onTestClicked() {
        HermesWearable.Node.getNodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Node::getId)
                .flatMap(id -> HermesWearable.Message.sendMessage(id, enabled ? "stop" : "query-hr"))
                .subscribe();

        enabled = !enabled;
    }
}
