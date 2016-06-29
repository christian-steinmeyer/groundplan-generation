package com.favendo.steinmeyer.groundplan.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.favendo.steinmeyer.app.R;
import com.favendo.steinmeyer.groundplan.generation.Groundplan;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    private final String INITIAL_FILE_LOAD_TAG = "initial-file-load";
    private final String TAG = "Main Activity";

    private TextView informationTextView;
    private ProgressBar progressBar;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        informationTextView = (TextView) findViewById(R.id.information_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(view -> openChooseFileDialog());
    }

    @Override
    public void onFileSelection(@NonNull final FileChooserDialog dialog, @NonNull final File file) {
        new GroundPlanGenerationTask().execute(file);
    }

    private void showSimpleToast(final String message) {
        runOnUiThread(() -> Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show());
    }

    private class GroundPlanGenerationTask extends AsyncTask<File, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            startButton.setClickable(false);
            informationTextView.setText(R.string.loading);
        }

        @Override
        protected String doInBackground(final File... files) {
            final Groundplan groundplan;
            File file = files[0]; // only receives one file
            try {
                groundplan = new Groundplan(file);
                return groundplan.getInformation();
            } catch (IllegalArgumentException e) {
                showSimpleToast(e.getLocalizedMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
                showSimpleToast("An error occurred during the handling of the file '" +
                        file.getName() + "'.");
                e.printStackTrace();
                return "See log for error.";
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            if (result == null) {
                openChooseFileDialog();
            } else {
                progressBar.setVisibility(View.GONE);
                informationTextView.setText(result);
                showSimpleToast(result);
            }
            startButton.setClickable(true);
        }
    }

    private void openChooseFileDialog() {
        new FileChooserDialog.Builder(this).initialPath(
                Environment.getExternalStorageDirectory().getPath() + "/TangoConstructor")
                                           .tag(INITIAL_FILE_LOAD_TAG)
                                           .show();
    }
}
