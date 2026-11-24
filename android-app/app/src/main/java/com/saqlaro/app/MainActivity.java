package com.saqlaro.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    int bg = Color.rgb(10, 12, 20);
    int panel = Color.rgb(25, 29, 42);
    int accent = Color.rgb(104, 112, 255);
    int white = Color.WHITE;
    int muted = Color.rgb(165, 170, 190);

    EditText linkInput;
    TextView status, platform, preview;
    Button checkButton, downloadButton;
    LinearLayout formatsBox;

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

    TextView heading(String value) {
        TextView t = text(value, 18, white);
        t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    LinearLayout card() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18, 18, 18, 18);
        box.setBackground(shape(panel, 20));
        return box;
    }

    void space(LinearLayout root, int height) {
        View v = new View(this);
        root.addView(v, new LinearLayout.LayoutParams(-1, height));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22, 38, 22, 25);

        scroll.addView(root);
        setContentView(scroll);

        TextView logo = text("SAQLARO", 30, white);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView subtitle = text(
            "YOUR MEDIA • YOUR LIBRARY", 11, muted
        );
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 7, 0, 35);
        root.addView(subtitle);

        root.addView(heading("Media yuklab olish"));

        TextView description = text(
            "Ruxsat berilgan media havolasini kiriting.",
            14, muted
        );
        description.setPadding(0, 9, 0, 20);
        root.addView(description);

        LinearLayout inputCard = card();

        linkInput = new EditText(this);
        linkInput.setSingleLine(true);
        linkInput.setHint("https://...");
        linkInput.setHintTextColor(Color.GRAY);
        linkInput.setTextColor(white);
        linkInput.setTextSize(15);
        linkInput.setPadding(12, 10, 12, 10);
        linkInput.setBackground(shape(
            Color.rgb(37, 41, 57), 14
        ));

        inputCard.addView(linkInput);
        root.addView(inputCard);

        checkButton = new Button(this);
        checkButton.setText("HAVOLANI TEKSHIRISH");
        checkButton.setTextColor(white);
        checkButton.setAllCaps(false);
        checkButton.setTextSize(15);
        checkButton.setBackground(shape(accent, 16));

        LinearLayout.LayoutParams buttonParams =
            new LinearLayout.LayoutParams(-1, 56);
        buttonParams.topMargin = 15;
        root.addView(checkButton, buttonParams);

        space(root, 22);

        LinearLayout platformCard = card();
        platformCard.addView(heading("Platforma"));

        platform = text("Hali aniqlanmadi", 15, muted);
        platform.setPadding(0, 12, 0, 0);
        platformCard.addView(platform);
        root.addView(platformCard);

        space(root, 15);

        LinearLayout previewCard = card();
        previewCard.addView(heading("Media preview"));

        preview = text(
            "Havola tekshirilgandan keyin media ma'lumotlari shu yerda ko'rsatiladi.",
            14, muted
        );
        preview.setPadding(0, 12, 0, 0);
        previewCard.addView(preview);
        root.addView(previewCard);

        space(root, 15);

        LinearLayout formatCard = card();
        formatCard.addView(heading("Formatni tanlang"));

        formatsBox = new LinearLayout(this);
        formatsBox.setOrientation(LinearLayout.VERTICAL);
        formatsBox.setPadding(0, 12, 0, 0);

        TextView formatInfo = text(
            "Video • Rasm • Audio\nHozircha mavjud emas",
            14, muted
        );
        formatsBox.addView(formatInfo);
        formatCard.addView(formatsBox);
        root.addView(formatCard);

        downloadButton = new Button(this);
        downloadButton.setText("YUKLAB OLISH — TEZ ORADA");
        downloadButton.setTextColor(muted);
        downloadButton.setAllCaps(false);
        downloadButton.setEnabled(false);
        downloadButton.setBackground(shape(
            Color.rgb(43, 46, 60), 16
        ));

        LinearLayout.LayoutParams downloadParams =
            new LinearLayout.LayoutParams(-1, 56);
        downloadParams.topMargin = 16;
        root.addView(downloadButton, downloadParams);

        status = text(
            "Server bilan bog'lanishga tayyor.", 13, muted
        );
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 20, 0, 10);
        root.addView(status);

        TextView footer = text(
            "SAQLARO • VERSION 1.0", 11, Color.GRAY
        );
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, 25, 0, 0);
        root.addView(footer);

        checkButton.setOnClickListener(v -> {
            String link = linkInput.getText().toString().trim();

            if (link.isEmpty()) {
                status.setText("Iltimos, havola kiriting.");
            } else if (!link.startsWith("https://")) {
                status.setText(
                    "Havola https:// bilan boshlanishi kerak."
                );
            } else {
                checkLink(link);
            }
        });
    }

    private void checkLink(String link) {
        checkButton.setEnabled(false);
        status.setText("Server bilan bog'lanilmoqda...");
        platform.setText("Tekshirilmoqda...");
        preview.setText("Media ma'lumotlari kutilmoqda...");

        new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL apiUrl = new URL(
                    "https://saqlaro.onrender.com/api/check"
                );

                connection = (HttpURLConnection)
                    apiUrl.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.setDoOutput(true);

                connection.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                );
                connection.setRequestProperty(
                    "Accept", "application/json"
                );

                JSONObject request = new JSONObject();
                request.put("url", link);

                try (OutputStream output =
                    connection.getOutputStream()) {
                    output.write(
                        request.toString().getBytes("UTF-8")
                    );
                }

                int code = connection.getResponseCode();

                InputStream stream =
                    code >= 200 && code < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

                StringBuilder result = new StringBuilder();

                if (stream != null) {
                    try (BufferedReader reader =
                        new BufferedReader(
                            new InputStreamReader(stream, "UTF-8")
                        )) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    }
                }

                JSONObject response =
                    new JSONObject(result.toString());

                boolean success =
                    response.optBoolean("success", false);

                boolean accepted =
                    response.optBoolean("accepted", false);

                String detectedPlatform =
                    response.optString("platform", "Noma'lum");

                String message =
                    response.optString(
                        "message", "Server javobi olindi."
                    );

                boolean previewAvailable =
                    response.optBoolean(
                        "previewAvailable", false
                    );

                JSONArray formats =
                    response.optJSONArray("formats");

                runOnUiThread(() -> {
                    if (success && accepted) {
                        platform.setText(
                            detectedPlatform + " aniqlandi"
                        );

                        if (previewAvailable) {
                            preview.setText(
                                "Media ma'lumotlari mavjud."
                            );
                        } else {
                            preview.setText(
                                "Media preview hozircha ulanmagan."
                            );
                        }

                        formatsBox.removeAllViews();

                        if (formats != null &&
                            formats.length() > 0) {

                            for (int i = 0;
                                i < formats.length(); i++) {

                                TextView item = text(
                                    formats.optString(i),
                                    14, muted
                                );
                                item.setPadding(0, 6, 0, 6);
                                formatsBox.addView(item);
                            }

                        } else {
                            TextView item = text(
                                "Video • Rasm • Audio\nHozircha mavjud emas",
                                14, muted
                            );
                            formatsBox.addView(item);
                        }

                        status.setText(message);

                    } else {
                        platform.setText("Aniqlanmadi");
                        preview.setText(
                            "Havola tekshirilmadi."
                        );
                        status.setText("Xatolik: " + message);
                    }

                    checkButton.setEnabled(true);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    status.setText(
                        "Server bilan bog'lanib bo'lmadi. Internetni tekshiring."
                    );
                    checkButton.setEnabled(true);
                });

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
