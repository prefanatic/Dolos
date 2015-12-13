package io.github.prefanatic.dolus;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;

import java.nio.ByteBuffer;

import edu.uri.egr.hermes.Hermes;
import edu.uri.egr.hermes.manipulators.FileLog;

public class WearableReceiverService extends IntentService {
    private FileLog log;

    public WearableReceiverService() {
        super("WearableReceiverService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        log = new FileLog("heart_rate");
        log.setHeaders("Time", "BPM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            MessageEvent messageEvent = intent.getParcelableExtra(Hermes.EXTRA_OBJECT);
            ByteBuffer buffer = ByteBuffer.wrap(messageEvent.getData());

            int heartRate = buffer.getInt();
            //float variableHr = buffer.getFloat();

            log.write(log.time(), heartRate);

            Hermes.Bus.push(Application.BUS_HR, heartRate);
        }
    }
}
