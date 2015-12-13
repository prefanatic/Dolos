package io.github.prefanatic.dolus;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.wearable.MessageEvent;

import java.nio.ByteBuffer;

import edu.uri.egr.hermes.Hermes;
import edu.uri.egr.hermeswear.HermesWearable;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by cody on 12/8/15.
 */
public class DispatchHandoffService extends Service {
    private Subscription sensorSubscription;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (sensorSubscription != null && !sensorSubscription.isUnsubscribed())
            sensorSubscription.unsubscribe();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasCategory(HermesWearable.SUBJECT_MESSAGE_RECEIVED)) {
            MessageEvent event = intent.getParcelableExtra(Hermes.EXTRA_OBJECT);

            if (event.getPath().equals("query-hr")) {
                Intent activity = new Intent(this, MainActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activity);

                sensorSubscription = Hermes.Sensor.observeSensor(Sensor.TYPE_HEART_RATE, SensorManager.SENSOR_DELAY_NORMAL)
                        .subscribe(e -> {
                            ByteBuffer buffer = ByteBuffer.allocate(4).putInt(Math.round(e.values[0]));
                            Hermes.Bus.push(Application.BUS_HR, Math.round(e.values[0]));
                            HermesWearable.Message.sendMessage(event.getSourceNodeId(), "hr-updated", buffer.array())
                                    .subscribe(res -> Timber.d("Sent HR value (%f) as a result: %d", e.values[0], res));
                        });
            } else if (event.getPath().equals("stop")) {
                Hermes.Bus.push(Application.BUS_STOP, true);
                stopSelf();
            }
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
