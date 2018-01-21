package senokt16.gmail.com.virtualbusinesscard.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class DeepLinkUtils  {

    public static Intent newFacebookIntent(PackageManager pm, String user) {
        String url = "https://www.facebook.com/" + user;
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newSnapchatIntent(PackageManager pm, String user) {
        String url = "https://www.snapchat.com/add/" + user;
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newInstagramIntent(PackageManager pm, String user) {
        String url = "https://www.instagram.com/_u/" + user;
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newYoutubeIntent(PackageManager pm, String user) {
        String url = "https://www.youtube.com/user/" + user;
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newTwitterIntent(PackageManager pm, String user) {
        String url = "https://www.twitter.com/" + user;
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newLinkIntent(PackageManager pm, String link){
        String url = "https://" + link;
        Uri uri = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

}
