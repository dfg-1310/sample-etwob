package e2b.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.utils.AppConstant;

import e2b.utils.NotificationUtils;
import e2b.utils.StorageUtils;
import e2b.utils.audio.AudioRecordingHandler;
import e2b.utils.audio.AudioRecordingThread;
import e2b.utils.visualizer.VisualizerView;
import e2b.utils.visualizer.renderer.BarGraphRenderer;

import static com.e2b.R.string.selectBtn;


public class AudioRecordingActivity extends BaseActivity {
	private static String fileName = null;
    
	private Button recordBtn, playBtn, selectBtn;
	private VisualizerView visualizerView;
	
	private AudioRecordingThread recordingThread;
	private boolean startRecording = true;
	public static String FileNameArg = "arg_filename";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_rec);
		
		if (!StorageUtils.checkExternalStorageAvailable()) {
			NotificationUtils.showInfoDialog(this, getString(R.string.noExtStorageAvailable));
			return;
		}
		fileName = StorageUtils.getFileName(true);
		
		recordBtn = (Button) findViewById(R.id.recordBtn);
		recordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				record();
			}
		});
		
		playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				play();
			}
		});

		selectBtn = (Button) findViewById(R.id.selectBtn);
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString(AppConstant.FILE_PATH_AUDIO, fileName);
				intent.putExtras(bundle);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

		visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
		setupVisualizer();

		playBtn.setEnabled(false);
		selectBtn.setEnabled(false);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		recordStop();
	}
	
	@Override
	protected void onDestroy() {
		recordStop();
		releaseVisualizer();
		setResult(Activity.RESULT_CANCELED, null);
		super.onDestroy();
	}
	
	private void setupVisualizer() {
		Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 227, 69, 53));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(2, paint, false);
        visualizerView.addRenderer(barGraphRendererBottom);
	}
	
	private void releaseVisualizer() {
		visualizerView.release();
		visualizerView = null;
	}
	
	private void record() {
        if (startRecording) {
        	recordStart();
        }
        else {
        	recordStop();
        }
	}
	
	private void recordStart() {
		startRecording();
    	startRecording = false;
    	recordBtn.setText(R.string.stopRecordBtn);
    	playBtn.setEnabled(false);
		selectBtn.setEnabled(false);
	}
	
	private void recordStop() {
		stopRecording();
		startRecording = true;
    	recordBtn.setText(R.string.recordBtn);
    	playBtn.setEnabled(true);
		selectBtn.setEnabled(true);

	}
	
	private void startRecording() {
	    recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() {
			@Override
			public void onFftDataCapture(final byte[] bytes) {
				runOnUiThread(new Runnable() {
					public void run() {
						if (visualizerView != null) {
							visualizerView.updateVisualizerFFT(bytes);
						}
					}
				});
			}

			@Override
			public void onRecordSuccess() {}

			@Override
			public void onRecordingError() {
				runOnUiThread(new Runnable() {
					public void run() {
						recordStop();
						NotificationUtils.showInfoDialog(AudioRecordingActivity.this, getString(R.string.recordingError));
					}
				});
			}

			@Override
			public void onRecordSaveError() {
				runOnUiThread(new Runnable() {
					public void run() {
						recordStop();
						NotificationUtils.showInfoDialog(AudioRecordingActivity.this, getString(R.string.saveRecordError));
					}
				});
			}
		});
	    recordingThread.start();
    }
    
    private void stopRecording() {
    	if (recordingThread != null) {
    		recordingThread.stopRecording();
    		recordingThread = null;
	    }
    }

    private void play() {
		Intent i = new Intent(AudioRecordingActivity.this, AudioPlaybackActivity.class);
		Log.d(TAG, "audio file name : "+ fileName);
		i.putExtra(FileNameArg, fileName);
		startActivityForResult(i, 0);
	}
}
