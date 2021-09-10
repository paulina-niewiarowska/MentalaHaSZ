package com.pniew.mentalahasz.module_periodization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.Things;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class ListOfThingsAdapter extends ListAdapter<Things, ListOfThingsAdapter.ThingHolder> {

    private OnThingClickListener listener;

    protected ListOfThingsAdapter() {
        super(new DiffUtil.ItemCallback<Things>() {
            @Override
            public boolean areItemsTheSame(@NonNull Things oldItem, @NonNull Things newItem) {
                return (oldItem.getId() == newItem.getId() && oldItem.getObjectType() == newItem.getObjectType());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Things oldItem, @NonNull Things newItem) {
                return oldItem.equals(newItem);
            }
        }
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ThingHolder holder, int position) {
        Things currentThing = (Things) getItem(position);
        setBasicView(holder, currentThing);

        if(getItem(position).getObjectType().equals(ART_PERIOD_STRING)){
            setArtPeriodView(holder, currentThing);

        } else if(getItem(position).getObjectType().equals(MOVEMENT_STRING)){
            setMovementView(holder, currentThing);
        }
    }

    @NonNull
    @Override
    public ThingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thing, parent, false);
        return new ListOfThingsAdapter.ThingHolder(itemView);
    }

    class ThingHolder extends RecyclerView.ViewHolder {
        private final TextView thingType;
        private final TextView thingName;
        private final TextView thingDating;
        private final TextView thingLocation;
        private final TextView thingParentPeriod;

        public ThingHolder(@NonNull View itemView) {
            super(itemView);
            thingType = itemView.findViewById(R.id.thing_type);
            thingName = itemView.findViewById(R.id.thing_name);
            thingDating = itemView.findViewById(R.id.thing_dating);
            thingLocation = itemView.findViewById(R.id.thing_location);
            thingParentPeriod = itemView.findViewById(R.id.thing_parent_period);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onThingClick(getItem(position));
                    }
                }
            });
        }
    }

    public Object getThingAt(int position){
        return getItem(position);
    }

    public interface OnThingClickListener {
        void onThingClick(Things thing);
    }

    public void setOnThingClickListener(OnThingClickListener listener) {
        this.listener = listener;
    }



    private void setBasicView(@NonNull ThingHolder holder, Things currentThing) {
        String stringType = "Item type: <b>" + currentThing.getObjectType() + "<b>";
        holder.thingType.setText(HtmlCompat.fromHtml(stringType, HtmlCompat.FROM_HTML_MODE_COMPACT));

        String stringName = "Item name: <b>" + currentThing.getName() + "<b>";
        holder.thingName.setText(HtmlCompat.fromHtml(stringName, HtmlCompat.FROM_HTML_MODE_COMPACT));

        holder.thingDating.setVisibility(View.GONE);
        holder.thingLocation.setVisibility(View.GONE);
        holder.thingParentPeriod.setVisibility(View.GONE);
    }

    private void setArtPeriodView(@NonNull ThingHolder holder, Things currentThing) {
        String stringDating = "Item dating: <b>" + currentThing.getDating() + "<b>";
        holder.thingDating.setText(HtmlCompat.fromHtml(stringDating, HtmlCompat.FROM_HTML_MODE_COMPACT));
        holder.thingDating.setVisibility(View.VISIBLE);

        holder.thingLocation.setVisibility(View.GONE);
        holder.thingParentPeriod.setVisibility(View.GONE);
    }

    private void setMovementView(@NonNull ThingHolder holder, Things currentThing) {
        String stringDating = "Item dating: <b>" + currentThing.getDating() + "<b>";
        holder.thingDating.setText(HtmlCompat.fromHtml(stringDating, HtmlCompat.FROM_HTML_MODE_COMPACT));
        String stringInPeriod = "Item in Period: <b>" + currentThing.getInPeriod() + "<b>";
        holder.thingParentPeriod.setText(HtmlCompat.fromHtml(stringInPeriod, HtmlCompat.FROM_HTML_MODE_COMPACT));
        String stringLocation = "Item location: <b>" + currentThing.getLocation() + "<b>";
        holder.thingLocation.setText(HtmlCompat.fromHtml(stringLocation, HtmlCompat.FROM_HTML_MODE_COMPACT));

        holder.thingLocation.setVisibility(View.VISIBLE);
        holder.thingParentPeriod.setVisibility(View.VISIBLE);
        holder.thingDating.setVisibility(View.VISIBLE);
    }

}
