package io.agora.recording.common;


import io.agora.recording.common.Common.AUDIO_FORMAT_TYPE;
import io.agora.recording.common.Common.CHANNEL_PROFILE_TYPE;
import io.agora.recording.common.Common.REMOTE_VIDEO_STREAM_TYPE;
import io.agora.recording.common.Common.VIDEO_FORMAT_TYPE;
import io.agora.recording.common.Common.MIXED_AV_CODEC_TYPE;

public class RecordingConfig {
  public RecordingConfig() {
    isAudioOnly = false;
    isVideoOnly = false;
    isMixingEnabled = false;
    mixedVideoAudio = MIXED_AV_CODEC_TYPE.MIXED_AV_DEFAULT;

    mixResolution = "";
    decryptionMode = "";
    secret = "";
    appliteDir = "";
    recordFileRootDir = "";
    cfgFilePath = "";
    proxyServer = "";
    defaultVideoBgPath = "";
    defaultUserBgPath = "";

    lowUdpPort = 0;//40000;
    highUdpPort = 0;//40004;
    idleLimitSec = 300;
    captureInterval = 5;
    triggerMode = 0;
    audioIndicationInterval = 0;
    audioProfile = 0;

    decodeVideo = VIDEO_FORMAT_TYPE.VIDEO_FORMAT_DEFAULT_TYPE;
    decodeAudio = AUDIO_FORMAT_TYPE.AUDIO_FORMAT_DEFAULT_TYPE;
    channelProfile = CHANNEL_PROFILE_TYPE.CHANNEL_PROFILE_COMMUNICATION;
    streamType = REMOTE_VIDEO_STREAM_TYPE.REMOTE_VIDEO_STREAM_HIGH;
  }

  /** Sets whether or not to record audio only:
   * <ul>
   *  <li>true: Enables audio recording and disables video recording.</li>
   *  <li>false: (Default) Enables both audio and video recording.</li>
   * </ul>
   *
   * Used together with isVideoOnly:
   * <ul>
   *   <li>If isAudioOnly is true and isVideoOnly is false, only records audio.</li>
   *   <li>If isAudioOnly is false and isVideoOnly is true, only records video.</li>
   *   <li>If isAudioOnly is false and isVideoOnly is false, records both audio and video.</li>
   *   <li>isAudioOnly and isVideoOnly can not be set as true at the same time.</li>
   * </ul>
   */
  public boolean isAudioOnly;

  /** Sets whether or not to record video only:
   * <ul>
   *   <li>true: Enables video recording and disable audio recording.</li>
   *   <li>false: (Default) Enables both audio and video recording.</li>
   * </ul>
   *
   * Used together with isAudioOnly:
   * <ul>
   *   <li>If isAudioOnly is true and isVideoOnly is false, only records audio.</li>
   *   <li>If isAudioOnly is false and isVideoOnly is true, only records video.</li>
   *   <li>If isAudioOnly is false and isVideoOnly is false, records both audio and video.</li>
   *   <li>isAudioOnly and isVideoOnly can not be set as true at the same time.</li>
   * </ul>
   */
  public boolean isVideoOnly;

  /** Sets whether or not to enable the composite recording mode.
   * 
   * <ul>
   *   <li>true: Enables composite recording mode, which means the audio of all uids is mixed in an audio file and the video of all uids is mixed in a video file. You can set the audio profile of the recording file by the {@link io::agora::recording::common::RecordingConfig#audioProfile audioProfile} parameter and set the video profile by the {@link io::agora::recording::common::RecordingConfig#mixResolution mixResolution} parameter.</li>
   *   <li>false: (Default) Enables individual recording mode, which means one audio or video file for each uid. The sampling rate of the recording file is 48 kHz, and the bitrate and audio channel number of the recording file are the same as those of the original audio stream.</li>
   * </ul>
   */
  public boolean isMixingEnabled;

  /** If you set {@link io.agora.recording.common.RecordingConfig#isMixingEnabled isMixingEnabled} as true, {@link io.agora.recording.common.RecordingConfig#mixedVideoAudio mixedVideoAudio} allows you to mix the audio and video in an MP4 file in real time. For more information, see {@link io.agora.recording.common.Common#MIXED_AV_CODEC_TYPE MIXED_AV_CODEC_TYPE}.
   */
  public MIXED_AV_CODEC_TYPE mixedVideoAudio;

  /** If you set {@link io.agora.recording.common.RecordingConfig#isMixingEnabled isMixingEnabled} as true, {@link io.agora.recording.common.RecordingConfig#mixResolution mixResolution} allows you to set the video profile, including the width, height, frame rate, and bitrate. The default setting is 360 x 640, 15 fps, 500 Kbps.
   *
   * @note Agora only supports the following frame rates: 1 fps, 7 fps, 10 fps, 15 fps, 24 fps, 30 fps and 60 fps. The default value is 15 fps. If you set the frame rate as other values, the SDK uses the default value.
   *
   * See the <a href="https://docs.agora.io/en/faq/recording_video_profile"> Resolution, Frame Rate and Bitrate Table</a>.
   *
   */
  public String mixResolution;

  /** When the whole channel is encrypted, the recording SDK uses decryptionMode to enable the built-in decryption function:
   * <ul>
   *   <li>"aes-128-xts": AES-128, XTS mode</li>
   *   <li>"aes-128-ecb": AES-128, ECB mode</li>
   *   <li>"aes-256-xts": AES-256, XTS mode</li>
   * </ul>
   * 
   * The default value is NULL.
   *
   * @note The decryption method of the recording application must be the same as that of the Native/Web SDK.
   */
  public String decryptionMode;

  /** The decryption password when decryption mode is enabled. The default value is NULL. */
  public String secret;

  /** Sets the path of AgoraCoreService.
   * The default path of AgoraCoreService is Agora_Recording_SDK_for_Linux_FULL/bin/.
   */
  public String appliteDir;

  /** Sets the path of the recorded files. The default value is NULL.
   *
   * After setting `recordFileRootDir`, the subdirectory will be automatically generated according to the date of the recording.
   */
  public String recordFileRootDir;

  /** Sets the path of the configuration file. The default value is NULL.
   *
   * In the configuration file, you can set the absolute directory of the output, but the subdirectory will not be generated automatically. The content in the configuration file must be in JSON format, such as {"Recording_Dir" : "<recording path>"}. You cannot change "Recording_Dir".
   */
  public String cfgFilePath;

  //decodeVideo: default 0 (0:save as file, 1:h.264, 2:yuv, 3:jpg buffer, 4:jpg file, 5:jpg file and video file)
  /** Sets the video decoding format. See {@link io.agora.recording.common.Common#VIDEO_FORMAT_TYPE VIDEO_FORMAT_TYPE}.
   * 
   * @note When {@link io.agora.recording.common.Common#VIDEO_FORMAT_TYPE VIDEO_FORMAT_TYPE} = 1, 2, 3 or 4, {@link io.agora.recording.common.RecordingConfig#isMixingEnabled isMixingEnabled} cannot be set as true.
   */
  public VIDEO_FORMAT_TYPE decodeVideo;

  //decodeAudio:  (default 0 (0:save as file, 1:aac frame, 2:pcm frame, 3:mixed pcm frame) (Can't combine with isMixingEnabled) /option)
  /** Sets the audio decoding format. See {@link io.agora.recording.common.Common#AUDIO_FORMAT_TYPE AUDIO_FORMAT_TYPE}.
   *
   * @note When {@link io.agora.recording.common.Common#AUDIO_FORMAT_TYPE AUDIO_FORMAT_TYPE} = 1, 2 or 3, {@link io.agora.recording.common.RecordingConfig#isMixingEnabled isMixingEnabled} cannot be set as true.
   */
  public AUDIO_FORMAT_TYPE decodeAudio;

  /** Sets the lowest UDP port. The default value is 0. Ensure that the value of highUdpPort - lowUdpPort &ge; 6. */
  public int lowUdpPort;

  /** Sets the highest UDP port. The default value is 0. Ensure that the value of highUdpPort - lowUdpPort &ge; 6. */
  public int highUdpPort;

  /** Sets a time period. The value must be &ge; 3 seconds. The default value is 300 seconds.
   *
   * When the Agora Recording SDK is recording, if there is no user in the channel after a time period of `idleLimitSec`, it automatically stops recording and leaves the channel.
   *
   * @note We charge you this time period.
   */
  public int idleLimitSec;

  /** Sets the interval of the screen capture. The interval must be longer than 1 second and the default value is 5 seconds.
   *
   * @note  `captureInterval` is only valid when {@link io.agora.recording.common.RecordingConfig#decodeVideo decodeVideo} is set as 3, 4 or 5.
   */
  public int captureInterval;

  /** Sets whether or not to detect the users who speak.
   *
   * <ul>
   *   <li>&le; 0: (Default) Do not detect the users who speak.</li>
   *   <li>> 0: Sets the interval (ms) of detecting the users who speak. Agora recommends setting the interval to be longer than 200 ms. When the SDK detects the users who speak, the SDK returns the UID of the user who speaks loudest in the {@link RecordingEventHandler#onActiveSpeaker onActiveSpeaker} callback and returns the UIDs of all users who speak and their voice volumes in the {@link io::agora::recording::RecordingEventHandler#onAudioVolumeIndication onAudioVolumeIndication} callback.</li>
   * </ul>
   */
  public int audioIndicationInterval;

  //channelProfile:0 communicate, 1:braodacast; default is 0
  /** Sets the channel mode. See {@link io.agora.recording.common.Common#CHANNEL_PROFILE_TYPE CHANNEL_PROFILE_TYPE}. */
  public CHANNEL_PROFILE_TYPE channelProfile;

  //streamType:0:get high stream 1:get low stream; default is 0
  /** `streamType` takes effect only when the Agora Native SDK/Web SDK enables the dual-stream
   * mode (high stream by default). See {@link io.agora.recording.common.Common#REMOTE_VIDEO_STREAM_TYPE REMOTE_VIDEO_STREAM_TYPE}.
   */
  public REMOTE_VIDEO_STREAM_TYPE streamType;

  /** Sets whether to start the recording automatically or manually:
   * <ul>
   *   <li>0: (Default) Automatically</li>
   *   <li>1: Manually</li>
   * </ul>
   *
   * If you wish to call {@link RecordingSDK#startService() startService} and {@link RecordingSDK#stopService() stopService}, then choose Manually.
   */
  public int triggerMode;

  /** Deploys the Enterprise Proxy. It allows you to record the content with the Intranet server.
   * For details, please contact <a href="mailto:ssles@agora.io">support@agora.io</a>.
   */
  public String proxyServer; //format ipv4:port

  /** If you set {@link io.agora.recording.common.RecordingConfig#isMixingEnabled isMixingEnabled} as true, {@link io.agora.recording.common.RecordingConfig#mixResolution mixResolution} allows you to set the audio profile of the recording file:
   * 
   * <ul>
   *   <li>AUDIO_PROFILE_DEFAULT = 0: (Default) Sampling rate of 48 kHz, communication encoding, mono, and a bitrate of up to 48 Kbps.</li>
   *   <li>AUDIO_PROFILE_MUSIC_HIGH_QUALITY = 1: Sampling rate of 48 kHz, music encoding, mono, and a bitrate of up to 128 Kbps.</li>
   *   <li>AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO = 2: Sampling rate of 48 kHz, music encoding, stereo, and a bitrate of up to 192 Kbps.</li>
   * </ul>
   */
  public int audioProfile;

  /** Sets the path of the default background image of the canvas in composite recording mode. If `defaultVideoBgPath` is not set, the cavas displays the background color.
   *
   * @note Only supports local images in JPEG format.
   */
  public String defaultVideoBgPath;

  /** Sets the path of the default background image of users in composite recording mode.
   *
   * The background image is displayed when a user is online and does not send any video stream. If `defaultUserBgPath` is not set, the the user region displays the background color.
   *
   * @note
   * <ul>
   *   <li>Only supports local images in JPEG format.</li>
   *   <li>The background image is not displayed for users using the Agora Web SDK.</li>
   * </ul>
   */
  public String defaultUserBgPath;
}
