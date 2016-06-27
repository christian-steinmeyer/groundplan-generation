package com.favendo.steinmeyer.groundplan.app;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.favendo.steinmeyer.app.R;
import com.favendo.steinmeyer.groundplan.generation.Groundplan;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    private final String INITIAL_FILE_LOAD_TAG = "initial-file-load-tag";
    private final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FileChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .mimeType("application/*.obj") // Optional MIME type filter
                .tag(INITIAL_FILE_LOAD_TAG).show();
    }

    @Override
    public void onFileSelection(@NonNull final FileChooserDialog dialog, @NonNull final File file) {
        final String tag =
                dialog.getTag(); // gets tag set from Builder, if you use multiple dialogs

        switch (tag) {
            case INITIAL_FILE_LOAD_TAG:
                try {
                    final Groundplan groundplan = new Groundplan(file);
                    showSimpleToast(groundplan.getInformation());
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    showSimpleToast("An error occurred during the handling of the file '" +
                            file.getName() + "'.");
                    e.printStackTrace();
                }
                break;
            default:
                // ?
        }
    }

    private void showSimpleToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
