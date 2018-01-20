package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import static android.support.v4.content.ContextCompat.startActivity;
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

        return null;
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        Pair<String,String> currPair = allList.get(position+2);
        final Intent currIntent;


        switch(currPair.first){
            case "EM": currIntent = null;
            break;
            case "AD": currIntent = null;
            break;
            case "PH": currIntent = null;
            break;
            case "FB": currIntent = newFacebookIntent(pm, currPair.second);
            break;
            case "TW": currIntent = newTwitterIntent(pm, currPair.second);
            break;
            case "SC": currIntent = newSnapchatIntent(pm, currPair.second);
            break;
            case "IG": currIntent = newInstagramIntent(pm, currPair.second);
            break;
            case "YT": currIntent = newYoutubeIntent(pm, currPair.second);
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
