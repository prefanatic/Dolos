package io.github.prefanatic.dolus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HeartRateView extends FrameLayout {
    private ImageView heart;
    private TextView rate;

    public HeartRateView(Context context) {
        this(context, null);
    }

    public HeartRateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        heart = new ImageView(getContext());
        heart.setImageResource(R.drawable.ic_heart);

        rate = new TextView(getContext());
        rate.setGravity(Gravity.CENTER);

        // TODO: 12/11/2015 Fix this madness.
        //rate.setTextAppearance(android.R.style.TextAppearance_Material_Display1);
        rate.setTextAppearance(getContext(), android.R.style.TextAppearance_Material_Display1);

        addView(heart);
        addView(rate);
    }

    public void setRate(int r) {
        rate.setText(String.format("%d", r));

        //FastOutLinearInInterpolator interpolator = new FastOutLinearInInterpolator();
        heart.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                //.setDuration(150L)
                //.setInterpolator(interpolator)
                .withEndAction(() -> heart.animate()
                        .scaleX(1f)
                        .scaleY(1f));

        rate.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .withEndAction(() -> rate.animate()
                        .scaleX(1f)
                        .scaleY(1f));
    }
}
