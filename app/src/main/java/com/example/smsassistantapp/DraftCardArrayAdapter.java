package com.example.smsassistantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DraftCardArrayAdapter extends ArrayAdapter <DraftCard>{

    private static final String TAG = "CardArrayAdapter";
    private List<DraftCard> cardList = new ArrayList<DraftCard>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }
    public DraftCardArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    @Override
    public void add(DraftCard object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public DraftCard getItem(int index) {
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
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardArrayAdapter.CardViewHolder)row.getTag();
        }
        DraftCard card = getItem(position);
        viewHolder.line1.setText(card.getLine1());
        viewHolder.line2.setText(card.getLine2());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
