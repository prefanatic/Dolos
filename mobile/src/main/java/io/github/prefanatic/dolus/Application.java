package io.github.prefanatic.dolus;

import com.google.android.gms.wearable.Wearable;

import edu.uri.egr.hermes.Hermes;

/**
 * Created by cody on 12/8/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Hermes.Config config = new Hermes.Config()
                .enableDebug(true)
                .addApi(Wearable.API);

        Hermes.init(this, config);
    }
}
