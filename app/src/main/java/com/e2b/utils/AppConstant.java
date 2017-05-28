package com.e2b.utils;

import android.Manifest;

import com.amazonaws.services.s3.model.CreateBucketRequest;

public class AppConstant {
    public static final String HOCKEY_APPID = "45dc7bbdd7654730971624bbde5b4da4";
    public static final String GPLUS_ID = "901679639956-kqhq4q7rdrcruscn71cd8pbt95psbdpt.apps.googleusercontent.com";
    public static final int PASSWARD_LENGTH = 5;
    public static final String TYPE = "tab_host_frag_type";
    public static final int INBOX = 1;
    public static final int FRIENDS = 0;

    public static final String DEVICE_OS = "1";
    public static final int TAKE_PICTURE = 0;
    public static final int CHOOSE_PICTURE = 1;
    public static final String MY_ACCESS_KEY_ID = "AKIAJPDFYMZSYSARHJWQ" ;
    public static final String MY_SECRET_KEY = "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";
    public static final String MY_PICTURE_BUCKET = "s3e2bapp";


    public static String FragmentVal = "fragmentVal";

    public interface PreferenceKeeperNames {
        String LOGIN = "login";
        String ACCESS_TOKEN = "access_token";
        String USER = "profile";
        String FCM_TOKEN = "fcm_token";
        String USER_ID = "user_id";
    }

    public interface PERMISSION_REQ_CODE {
        int WRITE_EXTERNAL = 0;
        int GET_ACCOUNT = 2;
        int READ_CONTACTS = 1;
    }

    public interface BUNDLE_KEY {
        String PRIVACY_URL = "privacy_url";
        String IS_FROM_CAMERA = "from_camera";
    }

    public interface FOOTER_INDEX {
        int HOME = 0;
        int ORDER = 1;
        int PROFILE = 2;
    }

    public interface STATUS_STATE {
        int DEFAULT = 0;
        int IMAGE = 1;
        int VIDEO = 2;
    }

    public interface NOTIFICATION_ACTION{
        int NEW_FRIEND_REQUEST= 1;
        int APPROVED_FRIEND_REQUEST= 2;
        int NEW_PRIVATE_MESSAGE= 3;
        int NEW_STREAM_POST= 4;
        int NEW_REPLIES_FOR_STREAM_POST= 5;
        int NEW_PROFILE_LIKE = 6;
        int NEW_COMMENT_STREAM_POST= 7;
        int NEW_STREAM_POST_LIKE=8;
        int NEW_STREAM_VIDEO=9;
        int NEW_STREAM_IMAGE=0;

//        1. New friends requests :neha send you a friend Request
//        2. Approved friend requests :You are now friends with avni mehta.
//        3. New private messages : neha sent you a Private message.
//                4. New stream posts: neha posted in the Plate group.
//                5. New replies for stream posts : neha replied to your stream comment
//        6. New profile likes: avni mehta liked your profile.
//        7. New comments on stream posts : neha has made a comment on your stream.
//                8. New stream posts likes : avni mehta liked your post.

    }

    public interface PERMISSION {
        String CAMERA = Manifest.permission.CAMERA;
        String WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE;
    }

    public interface REQUEST_CODE {
        int CAPTURE_IMAGE = 0;
        int GALLARY_IMAGE = 1;
        int CAMERA_PERMISSION = 2;
        int WRITE_EXTERNAL_PERMISSION = 3;
    }

    public interface REQ {
        int IMAGE_CAMERA = 100;
        int IMAGE_GALLERY = 200;
        int IMAGE_AUDIO = 300;
    }

    public static String FILE_PATH_IMAGE = "filePathImage";
    public static String FILE_PATH_AUDIO = "filePathAudio";


}
