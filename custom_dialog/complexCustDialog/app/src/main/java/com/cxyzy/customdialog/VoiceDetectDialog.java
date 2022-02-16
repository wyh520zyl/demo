package com.cxyzy.customdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class VoiceDetectDialog extends Dialog {
    private SparseIntArray layouts = new SparseIntArray();
    private ImageView voiceDetectCloseIv;
    private View reDetectLayout;

    public interface Styles {
        Integer LISTENING = 1;
        Integer DETECTING = 2;
        Integer RESULT_FAILED = 3;
    }

    public VoiceDetectDialog(@NonNull Activity activity, Integer style) {
        super(activity);
        initLayouts();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout(style));
        initViews();
        addListeners();
    }

    public void changeStyle(int style) {
        setContentView(getLayout(style));
        initViews();
        addListeners();
    }

    private void initViews() {
        voiceDetectCloseIv = findViewById(R.id.voiceDetectCloseIv);
        reDetectLayout = findViewById(R.id.reDetectLayout);
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    private void addListeners() {
        if (voiceDetectCloseIv != null) {
            RxView.clicks(voiceDetectCloseIv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe((Consumer) o -> VoiceDetectDialog.this.cancel());
        }

        if (reDetectLayout != null) {
            RxView.clicks(reDetectLayout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe((Consumer) o -> changeStyle(Styles.LISTENING));
        }

    }

    private @LayoutRes
    Integer getLayout(Integer style) {
        return layouts.get(style);
    }

    private void initLayouts() {
        layouts.put(Styles.LISTENING, R.layout.dialog_voice_listening);
        layouts.put(Styles.DETECTING, R.layout.dialog_voice_detecting);
        layouts.put(Styles.RESULT_FAILED, R.layout.dialog_voice_detect_failed);
    }
}
