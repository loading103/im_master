package com.android.im.imutils;

import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;

import com.android.im.IMSManager;
import com.android.im.R;
import com.android.nettylibrary.IMSConfig;
import com.android.nettylibrary.bean.IMMessageBean;
import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMPreferenceUtil;

/**
 * Created by Administrator on 2019/12/18.
 * Describe:
 */
public class SoundUtils {


    private Uri notification;

    private Vibrator vibrator;

    private long lastVoicetime;

    public  static SoundUtils instance;

    public static SoundUtils getInstance() {
        if (instance == null) {
            instance = new SoundUtils();
        }
        return instance;
    }
    /**
     * 提示音和震动(2秒一次)
     */
    public  void VoidandShakeSetting(IMMessageBean message) {
        if (message.getType() == MessageProtobuf.ImMessage.TypeEnum.USERMSG ) {
            if ((System.currentTimeMillis() - lastVoicetime) >1000) {
                HandlerTheSetting(message);
                lastVoicetime = System.currentTimeMillis();
            }
        }
    }

    private void HandlerTheSetting(IMMessageBean message) {
        if ( message==null || message.getData()==null) {
            return;
        }
        String groupId = message.getGroupId();
        String senderId = message.getSenderId();
        if (!TextUtils.isEmpty(groupId)) {   //是群的情况
            boolean hasnonotice = IMPreferenceUtil.getPreference_Boolean(groupId + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
            if (IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_VOICE, true)) {//开启声音
                if (!hasnonotice) {
                    playVoice();
                }
            }
            if (IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_SHOKE, false)) {//开启震动
                if (!hasnonotice) {
                    playShake();
                }
            }

        } else {
            boolean hasnonotice = IMPreferenceUtil.getPreference_Boolean(senderId + IMSConfig.IM_CONVERSATION_NO_NOTICE, false);
            if (IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_VOICE, true)) {//开启声音
                if (!hasnonotice) {
                    playVoice();
                }
            }
            if (IMPreferenceUtil.getPreference_Boolean(IMSConfig.IM_SETTING_SHOKE, false)) {//开启震动
                if (!hasnonotice) {
                    playShake();
                }
            }

        }
    }
    /**
     * 系统声音
     */
    public void playVoice() {
        if (notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone r = RingtoneManager.getRingtone(IMSManager.getInstance().getContext(), notification);
        r.play();

    }
    /**
     * 震动
     */
    public void playShake() {
        if (vibrator == null) {
            vibrator = (Vibrator) IMSManager.getInstance().getContext().getSystemService(Service.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(700);  //设置手机振动
    }
    /**
     * 播放声音
     * @param resId
     */
    public static void playSong(Context context,int resId) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(1);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        final int voiceId = soundPool.load(context,resId , 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0) {
                        soundPool.play(voiceId, 1, 1, 1, 0, 1);
                    }
                }
            });
        }
    }

}
