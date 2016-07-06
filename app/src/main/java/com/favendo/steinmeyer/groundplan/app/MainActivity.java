package com.favendo.steinmeyer.groundplan.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.favendo.steinmeyer.app.R;
import com.favendo.steinmeyer.groundplan.generation.Groundplan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    private final String INITIAL_FILE_LOAD_TAG = "initial-file-load";
    private final String TAG = "Main Activity";

    private TextView informationTextView;
    private ProgressBar progressBar;
    private Button startButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        informationTextView = (TextView) findViewById(R.id.information_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(view -> openChooseFileDialog());
        imageView = (ImageView) findViewById(R.id.image_view);
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
                String svgString = groundplan.generateSVG();
                SVG svg = SVG.getFromString(svgString);


                // Create a canvas to draw onto
                if (svg != null && svg.getDocumentWidth() != -1) {
                    exportSVGFile(file.getName(), svgString);
                    int width = (int) Math.ceil(svg.getDocumentWidth());
                    int height = (int) Math.ceil(svg.getDocumentHeight());
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);

                    // Render our document onto our canvas
                    svg.renderToCanvas(canvas);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    });
                }
//                Log.d(TAG, svgFile.getName());
                return groundplan.getInformation();
            } catch (SVGParseException e) {
                showSimpleToast(e.getLocalizedMessage());
                e.printStackTrace();
                return null;
            } catch (IllegalArgumentException e) {
                showSimpleToast(e.getLocalizedMessage());
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
                showSimpleToast("An error occurred during the handling of the file '" +
                        file.getName() + "'.");
                e.printStackTrace();
                return "See log for error.";
            }
        }

        private void exportSVGFile(final String filename, final String svg) {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    MainActivity.this.getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("AsyncTask", "failed to create directory");
                }
            }

            String newFilename = filename.substring(0, filename.lastIndexOf(".")) + ".svg";
            Log.d("AsyncTask", filename + " --> " + newFilename);
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + newFilename);
            try (FileOutputStream out = new FileOutputStream(mediaFile)) {
                out.write(svg.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
                                           .tag(INITIAL_FILE_LOAD_TAG).show();
    }
}
