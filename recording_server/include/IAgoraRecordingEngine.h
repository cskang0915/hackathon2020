#ifndef _IAGORA_RECORDINGENGINE_H_
#define _IAGORA_RECORDINGENGINE_H_
#include "IAgoraLinuxSdkCommon.h"

namespace agora {
namespace recording {
/**
 The IRecordingEngineEventHandler class enables callbacks to your application.
 */
class IRecordingEngineEventHandler {

public:
    virtual ~IRecordingEngineEventHandler() {}
    
    /** Occurs when an error occurs during SDK runtime.
     
     The SDK cannot fix the issue or resume running. It requires intervention from the application and informs the user on the issue.
     
     @param error \ref agora::linuxsdk::ERROR_CODE_TYPE "Error codes".
     @param stat_code \ref agora::linuxsdk::STAT_CODE_TYPE "State codes".
     
     */
    virtual void onError(int error, agora::linuxsdk::STAT_CODE_TYPE stat_code) = 0;

    /** Occurs when a warning occurs during SDK runtime.
     
     In most cases, the application can ignore the warnings reported by the SDK because the SDK can usually fix the issue and resume running.
     
     @param warn \ref agora::linuxsdk::WARN_CODE_TYPE "Warning codes".
     
     */
    virtual void onWarning(int warn) = 0;
   
    /** Occurs when the recording application joins the channel.
     
     @param channelId Channel ID assigned based on the channel name specified in \ref IRecordingEngine::joinChannel "joinChannel".
     @param uid The user ID.

     */
    virtual void onJoinChannelSuccess(const char * channelId, uid_t uid) = 0;
   
    /** Occurs when the recording application leaves the channel.
     
     @param code The reasons why the recording application leaves the channel. See the \ref agora::linuxsdk::LEAVE_PATH_CODE "LEAVE_PATH_CODE".

     */
    virtual void onLeaveChannel(agora::linuxsdk::LEAVE_PATH_CODE code) = 0;

    /** Occurs when a user joins the channel and returns the UID of the new user.
     
     - Communication profile: This callback notifies the recording application that a remote user joins the channel and reports the user's UID and information.
     - Live broadcast profile: This callback notifies the recording application that a host joins the channel and reports the user's UID and information.
     
     If there are users/hosts in the channel before the recording application joins the channel, the SDK also reports on the UIDs and information of the existing users/hosts. This callback is triggered as many times as the number of the users/hosts in the channel.
     
     @param uid The UID of the remote user/host joining the channel.
     @param infos \ref agora::linuxsdk::UserJoinInfos "User information".
     
     */
    virtual void onUserJoined(uid_t uid, agora::linuxsdk::UserJoinInfos &infos) = 0;
   
    /** Occurs when a user leaves the channel or goes offline.
     
     When no data package of a user is received for a certain period of time (15 seconds), the SDK assumes that the user has goes offline. Weak network connections may lead to misinformation, so Agora recommends using the signaling system for offline event detection.
     
     @param uid The user ID.
     @param reason The \ref agora::linuxsdk::USER_OFFLINE_REASON_TYPE "reasons" why the user leaves the channel or goes offline.
     
     */
    virtual void onUserOffline(uid_t uid, agora::linuxsdk::USER_OFFLINE_REASON_TYPE reason) = 0;

    /** Occurs when the raw audio data is received.
     
     @param uid The user ID.
     @param frame Received raw audio data in PCM or AAC format. For more information, see \ref agora::linuxsdk::AudioFrame "AudioFrame".

     */
    virtual void audioFrameReceived(unsigned int uid, const agora::linuxsdk::AudioFrame *frame) const = 0;

    /** Occurs when the raw video data is received.
     
     This callback is triggered for every received raw video frame and can be used to detect sexually explicit content, if necessary. Agora recommends capturing the I frame only and neglecting the others.
     
     @param uid The user ID.
     @param frame Received raw audio data in YUV, H.264 or JPG format. For more information, see \ref agora::linuxsdk::VideoFrame "VideoFrame".
     
     */
    virtual void videoFrameReceived(unsigned int uid, const agora::linuxsdk::VideoFrame *frame) const = 0;

    /** Reports the user who speaks loudest.
     
     If you set the \ref agora::recording::RecordingConfig::audioIndicationInterval "audioIndicationInterval" parameter in RecordingConfig to be greater than 0, this callback returns the `uid` of the user with the highest volume over a certain time period.
      
     @param uid The UID of the user with the highest volume over a certain time period.
     */
    virtual void onActiveSpeaker(uid_t uid)  = 0;

    /** Reports the list of users who are speaking and their volumes.
     
     This callback works only when the \ref agora::recording::RecordingConfig::audioIndicationInterval "audioIndicationInterval" parameter in RecordingConfig is set to be greater than 0.
     
     @param speakers    An array containing the user ID and volume information for each speaker. For more information, see \ref agora::linuxsdk::AudioVolumeInfo "AudioVolumeInfo".
     @param speakerNum  The total number of users who are speaking.
     */
    virtual void onAudioVolumeIndication(const agora::linuxsdk::AudioVolumeInfo* speakers, unsigned int speakerNum)  = 0;

    /** Occurs when the first remote video frame is decoded.
     
     This callback is triggered when the first frame of the remote video is received and decoded.
     
     @param uid     The user ID.
     @param width   The width of the video frame.
     @param height  The height of the video frame.
     @param elapsed Time elapsed (ms) from the local user calling \ref IRecordingEngine::joinChannel "joinChannel" until this callback is triggered.
     */
    virtual void onFirstRemoteVideoDecoded(uid_t uid, int width, int height, int elapsed) = 0;

    /** Occurs when the first remote audio frame is received.
     
     @param uid      The user ID.
     @param elapsed  Time elapsed (ms) from the local user calling \ref IRecordingEngine::joinChannel "joinChannel" until this callback is triggered.
     */
    virtual void onFirstRemoteAudioFrame(uid_t uid, int elapsed) = 0;

    /** Occurs when the status of receiving the audio or video stream changes.
     
     @param receivingAudio  Whether or not the recording application is receiving the audio stream.
     @param receivingVideo  Whether or not the recording application is receiving the video stream.
     */
    virtual void onReceivingStreamStatusChanged(bool receivingAudio, bool receivingVideo) = 0;

    /** Occurs when the SDK cannot reconnect to Agora's edge server 10 seconds after its connection to the server is interrupted.
     
     The SDK triggers this callback when it cannot connect to the server 10 seconds after calling \ref IRecordingEngine::joinChannel "joinChannel", regardless of whether it is in the channel or not.

     This callback is different from \ref IRecordingEngineEventHandler::onConnectionInterrupted "onConnectionInterrupted":
     
     - The SDK triggers the \ref IRecordingEngineEventHandler::onConnectionInterrupted "onConnectionInterrupted" callback when the SDK loses connection with the server for more than 4 seconds after it joins the channel.
     - The SDK triggers the \ref IRecordingEngineEventHandler::onConnectionLost "onConnectionLost" callback when the SDK loses connection with the server for more than 10 seconds, regardless of whether it joins the channel or not.
     
     For both callbacks, the SDK tries to reconnect to the server until the app calls \ref IRecordingEngine::leaveChannel "leaveChannel".
    */
    virtual void onConnectionLost() = 0;

    /** Occurs when the connection between the SDK and the server is interrupted.
     
     The SDK triggers this callback when it loses connection to the server for more than 4 seconds after the connection is established. After triggering this callback, the SDK tries to reconnect to the server. You can use this callback to implement pop-up reminders. 

     This callback is different from \ref IRecordingEngineEventHandler::onConnectionInterrupted "onConnectionInterrupted":
     
     - The SDK triggers the \ref IRecordingEngineEventHandler::onConnectionInterrupted "onConnectionInterrupted" callback when the SDK loses connection with the server for more than 4 seconds after it joins the channel.
     - The SDK triggers the \ref IRecordingEngineEventHandler::onConnectionLost "onConnectionLost" callback when the SDK loses connection with the server for more than 10 seconds, regardless of whether it joins the channel or not.
     
     For both callbacks, the SDK tries to reconnect to the server until the app calls \ref IRecordingEngine::leaveChannel "leaveChannel".
    */
    virtual void onConnectionInterrupted() = 0;
};

/** The recording configuration. */
typedef struct RecordingConfig {
    /** Sets whether or not to record audio only.
     - true: Enables audio recording and disables video recording.
     - false: (Default) Enables both audio and video recording.
     
     Used together with #isVideoOnly:
     - If #isAudioOnly is true and #isVideoOnly is false, only records audio;
     - If #isAudioOnly is false and #isVideoOnly is true, only records video;
     - If #isAudioOnly is false and #isVideoOnly is false, records both audio and video;
     - #isAudioOnly and #isVideoOnly can not be set as true at the same time.
     
     */
    bool isAudioOnly;
    /** Sets whether or not to record video only.
     - true: Enables video recording and disables audio recording.
     - false: (Default) Enables both audio and video recording.
     
     Used together with #isAudioOnly:
     - If #isAudioOnly is true and #isVideoOnly is false, only records audio;
     - If #isAudioOnly is false and #isVideoOnly is true, only records video;
     - If #isAudioOnly is false and #isVideoOnly is false, records both audio and video;
     - #isAudioOnly and #isVideoOnly cannot be set as true at the same time.
     
     */
    bool isVideoOnly;
    /** Sets whether or not to enable composite recording mode.
     
     - true: Enables composite recording mode, which means the audio of all uids is mixed in an audio file and the video of all uids is mixed in a video file. You can set the audio profile of the recording file by the #audioProfile parameter and set the video profile by the #mixResolution parameter.
     - false: (Default) Enables individual recording mode, which means one audio or video file for each uid. The sampling rate of the recording file is 48 kHz, and the bitrate and audio channel number of the recording file are the same as those of the original audio stream. The video profile of the recording file is the same as that of the original video profile.
     */
    bool isMixingEnabled;
    /**  If you set #isMixingEnabled as true and enable composite recording mode, #mixedVideoAudio allows you to mix the audio and video in an MP4 file in real time. For more information, see \ref agora::linuxsdk::MIXED_AV_CODEC_TYPE "MIXED_AV_CODEC_TYPE". 
     */
    agora::linuxsdk::MIXED_AV_CODEC_TYPE mixedVideoAudio;
    /** If you set #isMixingEnabled as true and enable composite recording mode, #mixResolution allows you to set the video profile, including the width, height, frame rate, and bitrate. The default setting is 360 x 640, 15 fps, 500 Kbps.
    
     @note Agora supports the following frame rates: 1 fps, 7 fps, 10 fps, 15 fps, 24 fps, 30 fps, and 60 fps. The default value is 15 fps. If you set other frame rates, the SDK uses the default value.
     
     See the [Resolution, Frame Rate and Bitrate Table](https://docs.agora.io/en/faq/recording_video_profile).
     */
    char * mixResolution;
    /** When the whole channel is encrypted, the recording SDK uses #decryptionMode to enable the built-in decryption function. The default value is NULL.
    
     The following decryption methods are supported:
     - "aes-128-xts": AES-128, XTS mode.
     - "aes-128-ecb" AES-128, ECB mode.
     - "aes-256-xts": AES-256, XTS mode.

     @note The decryption method of the recording application must be the same as that of the Native/Web SDK.
     */
    char * decryptionMode;
    /** Sets the decryption password when the #decryptionMode is enabled. The default value is NULL.  */
    char * secret;
    /** Sets the path of AgoraCoreService. 
     
     The default path of AgoraCoreService is Agora_Recording_SDK_for_Linux_FULL/bin/.  */
    char * appliteDir;
    /** Sets the path of the recorded files. The default value is NULL.
     
     After setting `recordFileRootDir`, the subdirectory will be automatically generated according to the date of the recording.
     */
    char * recordFileRootDir;
    /** Sets the path of the configuration file. The default value is NULL. 
     
     In the configuration file, you can set the absolute directory of the output, but the subdirectory will not be generated automatically. The content in the configuration file must be in JSON format, such as {"Recording_Dir" : "<recording path>"}. You cannot change "Recording_Dir".
     */
    char * cfgFilePath;
    /** Sets the video decoding format. For more information, see \ref agora::linuxsdk::VIDEO_FORMAT_TYPE "VIDEO_FORMAT_TYPE".
     
     @note When \ref agora::linuxsdk::VIDEO_FORMAT_TYPE "VIDEO_FORMAT_TYPE" = 1, 2, 3 or 4, \ref agora::recording::RecordingConfig#isMixingEnabled "isMixingEnabled" cannot be set as true.
     */
    agora::linuxsdk::VIDEO_FORMAT_TYPE decodeVideo;
    /** Sets the audio decoding format. For more information, see \ref agora::linuxsdk::AUDIO_FORMAT_TYPE "AUDIO_FORMAT_TYPE".
     
     @note When \ref agora::linuxsdk::AUDIO_FORMAT_TYPE "AUDIO_FORMAT_TYPE" = 1, 2 or 3, \ref agora::recording::RecordingConfig#isMixingEnabled "isMixingEnabled" cannot be set as true.
     */
    agora::linuxsdk::AUDIO_FORMAT_TYPE decodeAudio;
    /** Sets the lowest UDP port.
    
     Ensure that the value of highUdpPort - lowUdpPort is &ge; 6. The default value is 0.
     */
    int lowUdpPort;
    /** Sets the highest UDP port.
    
     Ensure that the value of highUdpPort - lowUdpPort is &ge; 6. The default value is 0.
     */
    int highUdpPort;
    /** Sets a time period. The value must be &ge; 3 seconds. The default value is 300 seconds.
     
     When the Agora Recording SDK is recording, if there is no user in the channel after a time period of `idleLimitSec`, it automatically stops recording and leaves the channel.
     
     @note We charge you this time period.
     */
    int idleLimitSec;
    /** Sets the interval of the screen capture.
     
     The interval must be longer than 1 second and the default value is 5 seconds. 
     
     @note #captureInterval is only valid when #decodeVideo is set as 3, 4, or 5.
     */
    int captureInterval;
    /** Sets whether or not to detect the users who speak. 
     
     - &le; 0: (Default) Do not detect the users who speak.
     - > 0: Sets the interval (ms) of detecting the users who speak. Agora recommends setting the interval to be longer than 200 ms. When the SDK detects the users who speak, the SDK returns the UID of the user who speaks loudest in the \ref agora::recording::IRecordingEngineEventHandler::onActiveSpeaker "onActiveSpeaker" callback and returns the UIDs of all users who speak and their voice volumes in the \ref agora::recording::IRecordingEngineEventHandler::onAudioVolumeIndication "onAudioVolumeIndication" callback.
     */
    int audioIndicationInterval;
    /** Sets the channel mode. For more information, see \ref agora::linuxsdk::CHANNEL_PROFILE_TYPE "CHANNEL_PROFILE_TYPE". */
    agora::linuxsdk::CHANNEL_PROFILE_TYPE channelProfile;
    /** Takes effect only when the Agora Native SDK enables the dual-stream mode (high stream by default). For more information, see \ref agora::linuxsdk::REMOTE_VIDEO_STREAM_TYPE "REMOTE_VIDEO_STREAM_TYPE".
     */
    agora::linuxsdk::REMOTE_VIDEO_STREAM_TYPE streamType;
    /** Sets whether to record automatically or manually. For more information, see \ref agora::linuxsdk::TRIGGER_MODE_TYPE "TRIGGER_MODE_TYPE". */
    agora::linuxsdk::TRIGGER_MODE_TYPE triggerMode;
    /** Sets the programming language. For more information, see \ref agora::linuxsdk::LANGUAGE_TYPE "LANGUAGE_TYPE". */
    agora::linuxsdk::LANGUAGE_TYPE lang;
    /** Sets the proxy server, which allows recording with the Intranet server. For more information, please contact sales@agora.io. */
    char * proxyServer;
    /** If you set #isMixingEnabled as true and enable composite recording mode, #audioProfile allows you to set the audio profile of the recording file. For more information, see \ref agora::linuxsdk::AUDIO_PROFILE_TYPE "AUDIO_PROFILE_TYPE". */
    agora::linuxsdk::AUDIO_PROFILE_TYPE audioProfile;
    /** Sets the directory of the default background image of the canvas in composite recording mode. 
     
     If #defaultVideoBg is not set, the canvas displays the background color.
     
     @note Only supports local images in JPEG format. */
    char * defaultVideoBg;
    /** Sets the directory of the default background image of users in composite recording mode.
     
     The background image is displayed when a user is online and does not send any video stream. If #defaultUserBg is not set, the user region displays the background color.
     
     @note
     - Only supports local images in JPEG format.
     - The background image is not displayed for users using the Agora Web SDK.
     */
    char * defaultUserBg;


    RecordingConfig(): channelProfile(agora::linuxsdk::CHANNEL_PROFILE_COMMUNICATION),
        isAudioOnly(false),
        isVideoOnly(false),
        isMixingEnabled(false),
        mixResolution(NULL),
        decryptionMode(NULL),
        secret(NULL),
        idleLimitSec(300),
        appliteDir(NULL),
        recordFileRootDir(NULL),
        cfgFilePath(NULL),
        lowUdpPort(0),
        highUdpPort(0),
        captureInterval(5),
        decodeAudio(agora::linuxsdk::AUDIO_FORMAT_DEFAULT_TYPE),
        decodeVideo(agora::linuxsdk::VIDEO_FORMAT_DEFAULT_TYPE),
        mixedVideoAudio(agora::linuxsdk::MIXED_AV_DEFAULT),
        streamType(agora::linuxsdk::REMOTE_VIDEO_STREAM_HIGH),
        triggerMode(agora::linuxsdk::AUTOMATICALLY_MODE),
        lang(agora::linuxsdk::CPP_LANG),
        proxyServer(NULL),
        audioProfile(agora::linuxsdk::AUDIO_PROFILE_DEFAULT),
        defaultVideoBg(NULL),
        defaultUserBg(NULL)
    {}

    virtual ~RecordingConfig() {}
} RecordingConfig;

/** The recording properties. */
typedef struct RecordingEngineProperties {
    /** The relative path of the recorded files and recording log. */
    char* storageDir;
    RecordingEngineProperties(): storageDir(NULL)
                          {}
}RecordingEngineProperties;

/** The IRecordingEngine class provides the main methods that can be invoked by your application. */
class IRecordingEngine {
public:

    /** This method creates an \ref agora::recording::IRecordingEngine "IRecordingEngine" instance.
     
     @param appId The App ID used in the communications to be recorded. For more information, see [Get an App ID](https://docs.agora.io/en/Recording/token?platform=All%20Platforms#app-id).
     @param eventHandler The Agora Recording SDK notifies the application of the triggered events by callbacks in the \ref agora::recording::IRecordingEngineEventHandler "IRecordingEngineEventHandler".

     @return An \ref agora::recording::IRecordingEngine "IRecordingEngine" instance.
     */
    static IRecordingEngine* createAgoraRecordingEngine(const char * appId, IRecordingEngineEventHandler *eventHandler);

    virtual ~IRecordingEngine() {}

    /** This method allows the recording application to join a channel and start recording.
     
     @note
     - In the Recording SDK, `requestToken` and `renewToken` are private methods. Make sure that you set [expireTimestamp](https://docs.agora.io/en/Recording/token?platform=All%20Platforms&_ga=2.205808721.316116830.1544409452-764614247.1539586349#Generate_Token) as 0 when generating a token, which means that the privilege, once generated, never expires.
     - A channel does not accept duplicate uids. Otherwise, there will be unpredictable behaviors.

     @param channelKey The `channelKey` for authentication. Set `channelKey` of the recording application the same as that of the Native/Web SDK. If the Native/Web SDK uses a token, `channelKey` must be set as the token. For more information, see [Use Security Keys](https://docs.agora.io/en/Voice/token?platform=All%20Platforms#app-id-native).
     @param channelId  The name of the channel to be recorded.
     @param uid        The unique identifier of the recording application. 
     - If you set `uid` as 0, the SDK randomly assigns a uid and returns it in the \ref IRecordingEngineEventHandler::onJoinChannelSuccess "onJoinChannelSuccess" callback.
     - If you set your own `uid`, it should be a 32-bit unsigned integer ranging from 1 to (2<sup>32</sup>-1).
     @param config     Detailed recording configuration. See \ref agora::recording::RecordingConfig "RecordingConfig".
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int joinChannel(const char * channelKey, const char *channelId, uid_t uid, const RecordingConfig &config) = 0;

    /** This method sets the video layout in composite recording mode.
     
     @note If you record video in composite recording mode, you must call this method to set the video layout.
     
     @param layout Layout setting. See \ref agora::linuxsdk::VideoMixingLayout "VideoMixingLayout".

     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int setVideoMixingLayout(const agora::linuxsdk::VideoMixingLayout &layout) = 0;
   
    /** This method stops \ref agora::recording::IRecordingEngineEventHandler::onError "onError" handler. 
    
     It is called in \ref agora::recording::IRecordingEngineEventHandler::onError "onError" handler when meeting an error which cannot be handled.
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int stoppedOnError() = 0;

    /** This method allows the recording application to leave the channel and release the thread resources.
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int leaveChannel() = 0;

    /** This method destroys the \ref agora::recording::IRecordingEngine "IRecordingEngine" object.
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int release() = 0;

    /** TThis method allows you to retrieve the recording properties. 
     
     @note
     - Call this method after joining the channel.
     - The recording properties only include the information of the path where the recording files and log are stored.
     - Both the `getProperties` method and the \ref agora::recording::IRecordingEngineEventHandler::onUserJoined "onUserJoined" callback report the relative path of the recorded files and recording log. The difference between these two functions is that the \ref agora::recording::IRecordingEngineEventHandler::onUserJoined "onUserJoined" callback is only triggered when a remote user joins the channel.

     @return \ref agora::recording::RecordingEngineProperties "RecordingEngineProperties".
     */
    virtual const RecordingEngineProperties* getProperties() = 0;

    /** This method manually starts recording.
     
     This method is valid only when you set \ref agora::recording::RecordingConfig::triggerMode "triggerMode" in RecordingConfig as 1 (manually). 
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int startService() = 0;

    /** This method manually stops recording.
     
     This method is valid only when you set \ref agora::recording::RecordingConfig::triggerMode "triggerMode" in RecordingConfig as 1 (manually). 
     
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int stopService() = 0;
     
    /** This method sets the background image of a specified user. When the user is online but does not send any video stream, the background image is displayed.
     
     @note The background image is not displayed for users using the Agora Web SDK.

     @param uid The UID of the user for whom the background image to be set. 
     @param img_path The path of the image file. Only supports local images in JPEG format.

     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int setUserBackground(uid_t uid, const char* img_path) = 0;

    /** This method sets the log output level.
     
     Only log levels preceding the selected level are generated. The default value of the log level is 5.
     
     @param level See \ref agora::linuxsdk::agora_log_level "agora_log_level".
     @return
     - 0: Success.
     - < 0: Failure.
     */
    virtual int setLogLevel(agora::linuxsdk::agora_log_level level) = 0;

};

}
}

#endif
