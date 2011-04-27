package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

/**
 * Player object responsible for streaming and displaying video from the cloud
 * in the SMIL document.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 * 
 */
public class VideoPlayer extends SingleInstancePlayer implements
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback,
		MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
	/**
	 * The wrapper class that will handle streaming and decoding of the video.
	 */
	private MediaPlayer mMediaPlayer;
	/**
	 * The time into the video where playback should start
	 */
	private double mOffsetInto;
	/**
	 * The surface view that is required to render the video on.
	 */
	private SurfaceView sfView;
	/**
	 * The Holder onto which the actual rendering will take place. this is
	 * derrived from the SurfaceView.
	 */
	private SurfaceHolder sfHolder;
	/**
	 * Flag to tell if the video has been rendered to the screen or not.
	 */
	private boolean videoRendered = false;
	/**
	 * Flag to tell if the surface on which the video will play has been
	 * rendered or not. It is required that the surface in rendered before the
	 * video can be rendered.
	 */
	private boolean surfaceRendered = false;
	/**
	 * The width of the video (as it should be rendered)
	 */
	private int mVideoWidth;
	/**
	 * The width of the vieo (as it should be rendered)
	 */
	private int mVideoHeight;

	/**
	 * Initialization for the video palyer
	 * 
	 * @param resource
	 *            The URI for the video resource to be streamed.
	 * @param begin
	 *            The time that the resource should be started in the SMIL
	 *            document
	 * @param duration
	 *            The time it sould play in the SMIL document
	 * @param offsetInto
	 *            The time at which playback should start within the video
	 * @param width
	 *            The width of the rendered video
	 * @param height
	 *            The height of the rendered video
	 */
	public VideoPlayer(String resource, double begin, double duration,
			double offsetInto, int width, int height) {
		this.resourceURL = resource;
		this.start = begin;
		this.duration = duration;
		this.mOffsetInto = offsetInto;
		this.mVideoWidth = width;
		this.mVideoHeight = height;
	}

	/**
	 * Play the document, if it is already playing, then continue playing. If it
	 * has not been rendered, then render it. This may cause the document to
	 * buffer and the video to become out of sync by a tenth of a second.
	 */
	public void play() {
		if (!this.videoRendered) {
			this.render();
		} else {
			try {
				this.mMediaPlayer.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		this.isPlaying = true;
		this.incrementPlaybackTime();
	}

	/**
	 * Pause the video
	 */
	public void pause() {
		if (this.isPlaying && this.surfaceRendered) {
			try {
				this.mMediaPlayer.pause();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Not implemented
	 */
	public void seekForward() {

	}

	/**
	 * Not implemented
	 */
	public void seekBackward() {

	}

	/**
	 * Render the surface of the video the the screen. Once that surface is done
	 * rendering, the video will render.
	 */
	public void render() {
		Log.w("Video", "Rendering the video");
		this.layout.post(new Runnable() {
			public void run() {
				layout.addView(sfView);
				// layout.setVisibility(View.VISIBLE);
				// sfView.setMinimumHeight(mMediaPlayer.getVideoHeight());
				// sfView.setMinimumWidth(mMediaPlayer.getVideoWidth());
				sfHolder.setFixedSize(mMediaPlayer.getVideoWidth(),
						mMediaPlayer.getVideoHeight());
			}
		});
		this.videoRendered = true;
		this.subject.notifyBuffering();
	}

	/**
	 * Remove the video from the canvas and release the video resources. At this
	 * point, if we want to play the video again, we must re-prepare the video
	 * player object
	 */
	public void unRender() {
		if (this.isPlaying) {
			this.layout.post(new Runnable() {
				public void run() {
					layout.removeView(sfView);
					try {
						mMediaPlayer.stop();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
					// layout.setVisibility(View.INVISIBLE);
				}
			});
		}
		this.mMediaPlayer.release();
	}

	/**
	 * Load the video resources and prepare the the mediaplayer.
	 */
	public void prepare() {
		this.mMediaPlayer = new MediaPlayer();
		try {
			this.mMediaPlayer.reset();
			this.mMediaPlayer.setDataSource(this.resourceURL);
			this.mMediaPlayer.setOnPreparedListener(this);
			this.subject.notifyBuffering();
			this.mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.layout.post(new Runnable() {
			@Override
			public void run() {
				sfView = new SurfaceView(layout.getContext());
				sfView.setLayoutParams(new LayoutParams(
						VideoPlayer.this.mVideoWidth,
						VideoPlayer.this.mVideoHeight));
				sfHolder = sfView.getHolder();
				sfHolder.addCallback(VideoPlayer.this);
				sfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}
		});
	}

	/**
	 * Seek to the correct portion of the video when the video is prepared.
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		this.mMediaPlayer.setOnSeekCompleteListener(this);
		this.mMediaPlayer.seekTo((int) (this.mOffsetInto * 100));
		Log.w("Video", "Prepared video");
	}

	/**
	 * Set the video to display in the surface when it is created and notify
	 * that we a donebuffering to the arbiter.
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mMediaPlayer.setDisplay(sfHolder);
		this.subject.notifyDoneBuffering();
		Log.w("Video", "Surface is ready for rendering!");
		this.surfaceRendered = true;
	}

	/**
	 * Not used
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * Not used
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	/**
	 * Not used
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
	}

	/*
	 * MediaPlayer.onBufferingUpdateListener (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate
	 * (android.media.MediaPlayer, int)
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	/**
	 * Reset the video for playback
	 */
	@Override
	public void reset() {
		this.unRender();
		this.videoRendered = false;
		this.surfaceRendered = false;
		this.prepare();
		super.reset();
	}

	/**
	 * When we are done seeking, release the buffer
	 */
	@Override
	public void onSeekComplete(MediaPlayer mp) {
		this.subject.notifyDoneBuffering();
	}

}
