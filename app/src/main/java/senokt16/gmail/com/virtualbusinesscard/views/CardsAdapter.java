package senokt16.gmail.com.virtualbusinesscard.views;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    List<InformationCard> data;

    public CardsAdapter(List<InformationCard> data) {
        this.data = data;
    }
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, int position) {
        InformationCard card = data.get(position);
        holder.title.setText(card.getAll().get(0).second);
        holder.description.setText(card.getAll().get(1).second);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        String[] names = card.getAll().get(0).second.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String s:names) {
            initials.append(s.charAt(0));
        }

        int color = generator.getColor(card.getAll().get(0).second);

        holder.thubmnail.setImageDrawable(TextDrawable.builder().buildRound(initials.toString(), color));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView thubmnail;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            thubmnail = view.findViewById(R.id.thumbnail);
        }
    }
    public List<InformationCard> getCards(){
        return data;
    }
    public InformationCard getSingleCard(int pos) {
        if(pos >= data.size()) {
            return null;
        }
        else{
            return data.get(pos);
        }
    }
}
