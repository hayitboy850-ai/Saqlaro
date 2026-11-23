package com.saqlaro.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    int bg = Color.rgb(12, 14, 20);
    int panel = Color.rgb(25, 28, 38);
    int accent = Color.rgb(100, 110, 255);
    EditText linkInput;
    TextView status;

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 45, 24, 24);

        scroll.addView(root);
        setContentView(scroll);

        TextView logo = text("SAQLARO", 30, Color.WHITE);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView subtitle = text(
            "YOUR MEDIA • YOUR LIBRARY", 11,
            Color.LTGRAY
        );
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 8, 0, 35);
        root.addView(subtitle);

        TextView heading = text(
            "Media yuklab olish", 23, Color.WHITE
        );
        heading.setTypeface(null, Typeface.BOLD);
        root.addView(heading);

        TextView description = text(
            "O'zingizga tegishli yoki yuklashga ruxsat berilgan media havolasini kiriting.",
            14, Color.LTGRAY
        );
        description.setPadding(0, 10, 0, 22);
        root.addView(description);

        linkInput = new EditText(this);
        linkInput.setSingleLine(true);
        linkInput.setHint("https://...");
        linkInput.setHintTextColor(Color.GRAY);
        linkInput.setTextColor(Color.WHITE);
        linkInput.setTextSize(15);
        linkInput.setPadding(18, 15, 18, 15);
        linkInput.setBackground(shape(panel, 18));

        root.addView(linkInput, new LinearLayout.LayoutParams(
            -1, 58
        ));

        Button download = new Button(this);
        download.setText("HAVOLANI TEKSHIRISH");
        download.setTextColor(Color.WHITE);
        download.setBackground(shape(accent, 18));

        LinearLayout.LayoutParams bp =
            new LinearLayout.LayoutParams(-1, 58);
        bp.topMargin = 18;
        root.addView(download, bp);

        status = text(
            "Hozircha demo rejim. Yuklash xizmati keyingi bosqichda ulanadi.",
            13, Color.LTGRAY
        );
        status.setPadding(4, 20, 4, 20);
        root.addView(status);

        TextView footer = text(
            "SAQLARO  •  VERSION 1.0", 11, Color.GRAY
        );
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, 50, 0, 0);
        root.addView(footer);

        download.setOnClickListener(v -> {
            String url = linkInput.getText().toString().trim();

            if (url.isEmpty()) {
                status.setText("Iltimos, havola kiriting.");
            } else if (!url.startsWith("https://")) {
                status.setText("Havola https:// bilan boshlanishi kerak.");
            } else {
                status.setText(
                    "Havola qabul qilindi. Yuklash xizmati hali ulanmagan."
                );
            }
        });
    }
}
