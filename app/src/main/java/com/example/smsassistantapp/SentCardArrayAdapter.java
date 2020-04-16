package com.example.smsassistantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentCardArrayAdapter extends ArrayAdapter<SentCard> implements Filterable {

    private static final String TAG = "CardArrayAdapter";
    private List<SentCard> cardList = new ArrayList<SentCard>();
    public List<SentCard> orig;
    static class CardViewHolder {
        TextView line1;
        TextView label;
        TextView line2;
        Button billbtn;
    }
    public SentCardArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(SentCard object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public SentCard getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardArrayAdapter.CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardArrayAdapter.CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.label = (TextView) row.findViewById(R.id.label);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            viewHolder.billbtn = (Button)row.findViewById(R.id.billbtn);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardArrayAdapter.CardViewHolder)row.getTag();
        }


        SentCard card = getItem(position);
        viewHolder.line1.setText(card.getLine1());
        viewHolder.label.setText(card.getLabel());
        viewHolder.line2.setText(card.getLine2());

        String paybtnvisibility="";
        paybtnvisibility = card.getLabel();
        Pattern bill = Pattern.compile("\\bBill|bill\\b");
        Matcher matcher = bill.matcher(paybtnvisibility);
        if(matcher.find()){
            viewHolder.billbtn.setVisibility(View.VISIBLE);
            Log.i("check if loop","label present");
        }
        else{
            viewHolder.billbtn.setVisibility(View.INVISIBLE);
        }

        return row;
    }
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<SentCard> results = new ArrayList<SentCard>();
                if (orig == null)
                    orig = cardList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final SentCard g : orig) {
                            if (g.getLine2().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cardList = (ArrayList<SentCard>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
