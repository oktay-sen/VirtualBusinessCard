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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import static senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol.*;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;
import senokt16.gmail.com.virtualbusinesscard.util.Unit;

import static android.support.v4.content.ContextCompat.startActivity;
import static senokt16.gmail.com.virtualbusinesscard.util.ContactUtils.newContactIntent;
import static senokt16.gmail.com.virtualbusinesscard.util.DeepLinkUtils.*;


public class ProfileAdapter extends Adapter<ProfileAdapter.ViewHolder> {
    private InformationCard iC;
    private ArrayList<Pair<String,String>> allList;
    private Context context;
    private PackageManager pm;

    private boolean editMode = false;
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
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Email");
            holder.description.setText(currPair.second);
            break;
            case ADDRESS_PREFIX: currIntent = newContactIntent(iC);
            icon = context.getResources().getDrawable(R.drawable.ic_map_marker);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Address");
            holder.description.setText(currPair.second);
            break;
            case PHONE_PREFIX: currIntent = newContactIntent(iC);
            icon = context.getResources().getDrawable(R.drawable.ic_phone_black_24dp);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Phone");
            holder.description.setText(currPair.second);
            break;
            case FACEBOOK_PREFIX: currIntent = newFacebookIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_facebook_box);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Facebook");
            holder.description.setText(currPair.second);
            break;
            case TWITTER_PREFIX: currIntent = newTwitterIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_twitter_box);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Twitter");
            holder.description.setText(currPair.second);
            break;
            case SNAPCHAT_PREFIX: currIntent = newSnapchatIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_snapchat);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Snapchat");
            holder.description.setText(currPair.second);
            break;
            case INSTAGRAM_PREFIX: currIntent = newInstagramIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_instagram);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("Instagram");
            holder.description.setText(currPair.second);
            break;
            case YOUTUBE_PREFIX: currIntent = newYoutubeIntent(pm, currPair.second);
            icon = context.getResources().getDrawable(R.drawable.ic_youtube_play);
            holder.thumbnail.setImageDrawable(icon);
            holder.title.setText("YouTube");
            holder.description.setText(currPair.second);
            break;
            default: currIntent = null;
            break;
        }

        if (editMode) {
            //holder.edit.startAnimation(AnimationUtils.loadAnimation(context, R.anim.edit_reveal));
            holder.edit.setVisibility(View.VISIBLE);
        } else {
            //holder.edit.setTranslationX(Unit.dp(context, 48));
            holder.edit.setVisibility(View.GONE);
            holder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currIntent == null){
                        return;
                    }
                    context.startActivity(currIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.v("items", allList.toString());
        return allList.size() - 2;
    }

    public boolean toggleEdit() {
        editMode = !editMode;
        notifyDataSetChanged();
        return editMode;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, edit;
        TextView title, description;
        View underline;
        ViewGroup wrapper;
        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            edit = itemView.findViewById(R.id.action);
            underline = itemView.findViewById(R.id.underline);
            wrapper = itemView.findViewById(R.id.wrapper);
        }
    }

}
