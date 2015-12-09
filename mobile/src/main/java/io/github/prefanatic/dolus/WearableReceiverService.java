package io.github.prefanatic.dolus;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;

import java.nio.ByteBuffer;

import edu.uri.egr.hermes.Hermes;
import timber.log.Timber;

public class WearableReceiverService extends IntentService {

    public WearableReceiverService() {
        super("WearableReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            MessageEvent messageEvent = intent.getParcelableExtra(Hermes.EXTRA_OBJECT);
            ByteBuffer buffer = ByteBuffer.wrap(messageEvent.getData());

            float heartRate = buffer.getFloat();
            //float variableHr = buffer.getFloat();

            Timber.d("Received HR is: %f (%f)", heartRate, 0);
        }
    }
}
