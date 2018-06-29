package com.metropolitan.sistemzarezervacijukarata;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List items = new ArrayList();

    public FilmAdapter(Context context, List items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Film film = (Film)items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_layout, null);
            holder = new ViewHolder();

            holder.name = (TextView)convertView.findViewById(R.id.naslov);
            holder.pic = (ImageView)convertView.findViewById(R.id.slika);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(film.getNaslov());
        if (film.getSlika() != null) {
            holder.pic.setImageBitmap(film.getSlika());
        } else {
            // MY DEFAULT IMAGE
            holder.pic.setImageResource(R.drawable.ic_launcher_background);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView name;

        ImageView pic;
    }
}
