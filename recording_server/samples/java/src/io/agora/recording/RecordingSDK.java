package io.agora.recording;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.agora.recording.common.Common.AudioFrame;
import io.agora.recording.common.Common.VideoFrame;
import io.agora.recording.common.Common.VideoMixingLayout;
import io.agora.recording.common.RecordingConfig;
import io.agora.recording.common.RecordingEngineProperties;
import io.agora.recording.common.Common.AudioVolumeInfo;

public class RecordingSDK {

	private List<RecordingEventHandler> recordingEventHandlers = null;
  private long nativeObjectHandle = 0;

	/** Load Java library. */

	static {
		System.loadLibrary("recording");
	}

  /** Main methods that can be invoked by your app.*/
	public RecordingSDK() {
		recordingEventHandlers = new ArrayList<RecordingEventHandler>();
	}

  /** To register observer to receive Recording event notification. */
	public void registerOberserver(RecordingEventHandler recordingEventHandler) {
		if (!recordingEventHandlers.contains(recordingEventHandler)) {
			recordingEventHandlers.add(recordingEventHandler);
		}
	}

  /** To remove previously registered observer. */
	public void unRegisterOberserver(RecordingEventHandler recordingEventHandler) {
		if (recordingEventHandlers.contains(recordingEventHandler)) {
			recordingEventHandlers.remove(recordingEventHandler);
		}
	}

  /** This method creates a channel and enables the recording application to join the channel.
   *
   * @param appId Set `appId` of the recording application the same as that of the Native/Web SDK. For more information, see <a href="https://docs.agora.io/en/Recording/token?platform=All%20Platforms">Getting an App ID</a>.
   *
   * @param channelKey The dynamic key for authentication. Set `channelKey` of the recording application the same as that of the Native/Web SDK. If the Native/Web SDK uses a token, `channelKey` must be set as the token. For more information, see <a href="https://docs.agora.io/en/Recording/token?platform=All%20Platforms">Use Security Keys</a>. In the Recording SDK, `requestToken` and `renewToken` are private interfaces. Therefore, ensure that you set <a href="https://docs.agora.io/en/Recording/token?platform=All%20Platforms#Generate_Token">expireTimestamp</a> as 0 when generating a token, which means that the privilege, once generated, never expires.
   *
   * @param name The name of the channel to be recorded.
   *
   * @param uid The unique identifier of a user. A channel does not accept duplicate uids. Otherwise, there will be unpredictable behaviors.
   * <ul>
   *   <li>If you set `uid` as 0, the SDK randomly assigns a uid and returns it in the {@link io.agora.recording.RecordingEventHandler#onJoinChannelSuccess onJoinChannelSuccess}.</li>
   *   <li>If you set your own `uid`, it should be a 32-bit unsigned integer ranging from 1 to (2<sup>32</sup>-1).User ID.</li>
   * </ul>
   * @param config Detailed recording configuration. See {@link io.agora.recording.common.RecordingConfig RecordingConfig}.
   *
   * @param logLevel Sets the log level. Only logs in the levels preceding the selected level are generated.
   *
   * @return
   * <ul>
   *   <li>0: Success.</li>
   *   <li>< 0: Failure.</li>
   * </ul>
   */
	public native boolean createChannel(String appId, String channelKey, String name, int uid, RecordingConfig config, int logLevel);

	/** This method allows the recording application to leave the channel and release the thread resources.
	 *
	 * @param nativeHandle The recording engine.
	 *
	 * @return
	 * <ul>
	 *   <li>0: Success.</li>
	 *   <li>< 0: Failure.</li>
	 * </ul>
	 */
  public boolean leaveChannel(long nativeHandle) {
    if (nativeObjectHandle != 0) {
      long temp = nativeObjectHandle;
      nativeObjectHandle = 0;
      return nativeLeaveChannel(temp);
    }
    return false;
  }
	public native boolean nativeLeaveChannel(long nativeHandle);

	/** This method sets the video mixing layout.
	 *
	 * @note If you record video in composite recording mode, you must call this method to set the video layout.
	 *
	 * @param nativeHandle The recording engine.
	 *
	 * @param layout Layout setting. See {@link io.agora.recording.common.Common.VideoMixingLayout VideoMixingLayout}.

	 *
	 * @return
	 * <ul>
	 *   <li>0: Success.</li>
	 *   <li>< 0: Failure.</li>
	 * </ul>
	 */
	public int setVideoMixingLayout(long nativeHandle, VideoMixingLayout layout) {
    if (nativeObjectHandle != 0) {
      return nativeSetVideoMixingLayout(nativeObjectHandle, layout);
    }
    return -1;
  }

  private native int nativeSetVideoMixingLayout(long natvieHandle, VideoMixingLayout layout);

	/** This method manually starts a recording.
	 *
	 * The method is only valid when you set {@link io.agora.recording.common.RecordingConfig#triggerMode triggerMode} in {@link io.agora.recording.common.RecordingConfig RecrordingConfig} as 1 (manually) when joining the channel.
	 *
	 * @param nativeHandle The recording engine.
	 *
	 * @return
	 * <ul>
	 *   <li>0: Success.</li>
	 *   <li>< 0: Failure.</li>
	 * </ul>
	 */
	public int startService(long nativeHandle) {
    if (nativeObjectHandle != 0) {
      return nativeStartService(nativeObjectHandle);
    }
    return -1;
  }

  private native int nativeStartService(long nativeHandle);

	/** This method manually pauses the recording.
	 *
	 * The method is only valid when you set {@link io.agora.recording.common.RecordingConfig#triggerMode triggerMode} in {@link io.agora.recording.common.RecordingConfig RecrordingConfig} as 1 (manually) when joining the channel.
	 *
	 * @param nativeHandle The recording engine.
	 *
	 * @return
	 * <ul>
	 *   <li>0: Success.</li>
	 *   <li>< 0: Failure.</li>
	 * </ul>
	 */
	public int stopService(long nativeHandle) {
    if (nativeObjectHandle != 0) {
      return nativeStopService(nativeObjectHandle);
    }
    return -1;
  }

  private native int nativeStopService(long nativeHandle);

	/** This method allows you to retrieve the recording properties.
	 *
   * @note
   * <ul>
   *   <li>Call this method after joining the channel.</li>
   *   <li>The recording properties only include the relative path of the recording files.</li>
   *   <li>Both the `getProperties` method and the {@link RecordingEventHandler#onUserJoined onUserJoined} callback report the relative path of the recorded files and recording log. The difference between these two functions is that the recording SDK only triggers the {@link RecordingEventHandler#onUserJoined onUserJoined} callback when a remote user joins the channel. </li>
   * </ul>
	 *
	 * @param nativeHandle The recording engine.
	 *
	 * @return RecordingEngineProperties. See {@link io.agora.recording.common.RecordingEngineProperties RecordingEngineProperties}.
	 */
  public RecordingEngineProperties getProperties(long nativeHandle) {
    if (nativeObjectHandle != 0) {
      return nativeGetProperties(nativeObjectHandle);
    }
    return null;
  }

  private native RecordingEngineProperties nativeGetProperties(long nativeHandle);

  /** This method sets the background image of a specified user.
   *
   * When the user is online but does not send any video stream, the background image is displayed.
   *
   * @note The background image is not displayed for users using the Agora Web SDK.
   *
   * @param nativeHandle The recording engine.
   * @param uid The UID of the user for whom the background image to be set.
   * @param image_path The path of the image file. Only supports local images in JPEG format.
   *
   * @return
   * <ul>
   *   <li>0: Success.</li>
   *   <li>< 0: Failure.</li>
   * </ul>
   */
  public int setUserBackground(long nativeHandle, int uid, String image_path) {
    if (nativeObjectHandle != 0) {
      return nativeSetUserBackground(nativeObjectHandle, uid, image_path);
    }
    return -1;
  }

  private native int nativeSetUserBackground(long nativeHandle, int uid, String image_path);

  /** This method sets the log level.
   *
   * Only log levels preceding the selected level are generated. The default value of the log level is 5.
   *
   * @param nativeHandle The recording engine.
   * @param level The intended level.
   *
   * @return
   * <ul>
   *   <li>0: Success.</li>
   *   <li>< 0: Failure.</li>
   * </ul>
   */
  public void setLogLevel(long nativeHandle, int level) {
    if (nativeObjectHandle != 0) {
      nativeSetLogLevel(nativeObjectHandle, level);
    }
  }

  private native void nativeSetLogLevel(long nativeHandle, int level);


  private native void enableLogModule(long nativeHandle, int module, int enable);


  private void nativeObjectRef(long nativeHandle) {
    this.nativeObjectHandle = nativeHandle;
    for (RecordingEventHandler oberserver : recordingEventHandlers) {
      oberserver.nativeObjectRef(nativeHandle);
    }
  }


	private void onLeaveChannel(int reason) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onLeaveChannel(reason);
		}
	}


	private void onError(int error, int stat_code) {
    nativeObjectHandle = 0;
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onError(error, stat_code);
		}
	}


	private void onWarning(int warn) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onWarning(warn);
		}
	}


	private void onUserOffline(long uid, int reason) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onUserOffline(uid, reason);
		}
	}


	private void onUserJoined(long uid, String recordingDir) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onUserJoined(uid, recordingDir);
		}
	}


	private void audioFrameReceived(long uid, AudioFrame frame) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.audioFrameReceived(uid, frame);
		}
	}


    private void onActiveSpeaker(long uid) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onActiveSpeaker(uid);
        }
    }

    /*
     * Brief: Callback to indicate audio volume
     * @param AudioVolumeInfo[]  audio info arrary
     */
    private void onAudioVolumeIndication(AudioVolumeInfo[] infos) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onAudioVolumeIndication(infos);
        }
    }

    /*
     * Brief: Callback to indicate receiving stream status changed
     * @param receivingAudio  receiving audio stream status
     * @param receivingVideo  receiving video stream status
     */
    private void onReceivingStreamStatusChanged(boolean receivingAudio, boolean receivingVideo) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onReceivingStreamStatusChanged(receivingAudio, receivingVideo);
        }
    }

    /*
    * when the network can not worked well, the function will be called
    */
    private void onConnectionLost() {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onConnectionLost();
        }
    }

    /*
    * when local user disconnected by accident, the function will be called(then SDK will try to reconnect itself)
    */
    private void onConnectionInterrupted() {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onConnectionInterrupted();
        }
    }
    /*
    * Brief: when the first remote video frame decoded, the function will be called
    *
    * @param uid the UID of the remote user
    *
    * @param width the width of the video frame
    *
    * @param height the height of the video frame
    *
    * @param elapsed the time elapsed from channel joined in ms
    */
    private void onFirstRemoteVideoDecoded(long uid, int width, int height, int elapsed) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }
    }

    /*
    * Brief: when the first remote audio frame arrived, the function will be called
    *
    * @param uid the UID of the remote user
    *
    * @param elapsed the time elapsed from channel joined in ms
    */
    private void onFirstRemoteAudioFrame(long uid, int elapsed) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
            oberserver.onFirstRemoteAudioFrame(uid, elapsed);
        }
    }

    /*
     * Brief: Callback when received a video frame
	 *
	 * @param uid user ID
	 *
	 * @param type type of video frame, please refer to the define of VideoFrame
	 *
	 * @param frame reference of received video frame
	 *
	 * @param rotation rotation of video
	 */
	private void videoFrameReceived(long uid, int type, VideoFrame frame, int rotation) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.videoFrameReceived(uid, type, frame, rotation);
		}
	}


	private void stopCallBack() {
    nativeObjectHandle = 0;
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.stopCallBack();
		}
	}


	private void recordingPathCallBack(String path) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.recordingPathCallBack(path);
		}
	}


	 private void onJoinChannelSuccess(String channelId, long uid) {
		for (RecordingEventHandler oberserver : recordingEventHandlers) {
			oberserver.onJoinChannelSuccess(channelId, uid);
		}
	}


}
