package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import static senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol.*;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import static android.support.v4.content.ContextCompat.startActivity;
import static senokt16.gmail.com.virtualbusinesscard.util.ContactUtils.newContactIntent;
import static senokt16.gmail.com.virtualbusinesscard.util.DeepLinkUtils.*;

/**
 * Created by Ian_Tai on 2018-01-20.
 */

public class ProfileAdapter extends Adapter<ProfileAdapter.ViewHolder> {
    private InformationCard iC;
    private ArrayList<Pair<String,String>> allList;
    private Context context;
    private PackageManager pm;
    public ProfileAdapter(InformationCard infoC, Context incContext) {
        iC = infoC;
        allList = iC.getAll();
        context = incContext;
        pm = context.getPackageManager();
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        Pair<String,String> currPair = allList.get(position+2);
        final Intent currIntent;
        Drawable icon;
        Log.v("Check", "arrivedAtViewHolder");
        switch(currPair.first){
            case EMAIL_PREFIX: currIntent = newContactIntent(iC);
            icon = context.getResources().getDrawable(R.drawable.ic_email_black_24dp);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Email: " + currPair.second);
            break;
            case ADDRESS_PREFIX: currIntent = newContactIntent(iC);
            icon = context.getResources().getDrawable(R.drawable.ic_map_marker);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Address: " + currPair.second);
            break;
            case PHONE_PREFIX: currIntent = newContactIntent(iC);
            icon = context.getResources().getDrawable(R.drawable.ic_phone_black_24dp);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Phone: " + currPair.second);
            break;
            case FACEBOOK_PREFIX: currIntent = newFacebookIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_facebook_box);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Facebook: " + currPair.second);
            break;
            case TWITTER_PREFIX: currIntent = newTwitterIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_twitter_box);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Twitter: " + currPair.second);
            break;
            case SNAPCHAT_PREFIX: currIntent = newSnapchatIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_snapchat);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Snapchat: " + currPair.second);
            break;
            case INSTAGRAM_PREFIX: currIntent = newInstagramIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_instagram);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Instagram: " + currPair.second);
            break;
            case YOUTUBE_PREFIX: currIntent = newYoutubeIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_youtube_play);
            holder.button.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
            holder.button.setText("Youtube: " + currPair.second);
            break;
            default: currIntent = null;
            break;
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currIntent == null){
                    return;
                }
                context.startActivity(currIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v("items", allList.toString());
        return allList.size() - 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_link);
        }
    }
}
