package io.github.prefanatic.dolus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.wearable.Node;

import edu.uri.egr.hermeswear.HermesWearable;

/**
 * Created by cody on 12/8/15.
 */
public class MainActivity extends AppCompatActivity {
    private Button testButton;
    private boolean enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = (Button) findViewById(R.id.test);
        testButton.setOnClickListener(button -> {
            HermesWearable.Node.getNodes()
                    .map(Node::getId)
                    .flatMap(id -> HermesWearable.Message.sendMessage(id, enabled ? "stop" : "query-hr"))
                    .subscribe();

            enabled = true;
        });

    }
}
