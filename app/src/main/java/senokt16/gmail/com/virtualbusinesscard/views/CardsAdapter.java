package senokt16.gmail.com.virtualbusinesscard.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import senokt16.gmail.com.virtualbusinesscard.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, int position) {
        holder.title.setText("Card #" + (position+1));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
        }
    }
}
