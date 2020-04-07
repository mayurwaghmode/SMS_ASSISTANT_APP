package com.example.smsassistantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentCardArrayAdapter extends ArrayAdapter<SentCard>{

    private static final String TAG = "CardArrayAdapter";
    private List<SentCard> cardList = new ArrayList<SentCard>();

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

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
