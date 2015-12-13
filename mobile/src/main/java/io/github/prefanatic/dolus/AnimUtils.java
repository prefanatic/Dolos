package io.github.prefanatic.dolus;

import android.view.View;

public class AnimUtils {
    public static void hide(View view) {
        if (view.getVisibility() == View.GONE) return;

        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(view.getHeight() * 2f)
                .alpha(0f)
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    public static void show(View view) {
        if (view.getVisibility() == View.VISIBLE) return;

        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        view.setTranslationY(view.getHeight() * 2f);
        view.animate()
                .translationY(0f)
                .alpha(1f)
                .start();
    }
}
