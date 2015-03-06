package com.sifiso.codetribe.summarylib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sifiso.codetribe.summarylib.R;
import com.sifiso.codetribe.summarylib.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DrawerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private int[] drawables;


    public DrawerAdapter(Context context, int textViewResourceId,
                         List<String> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // drawables = new int[]{R.drawable.ic_launcher, R.drawable.ic_launcher};

    }

    View view;


    static class ViewHolderItem {
        TextView DR_titles;
        ImageView DR_icon_drawer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.DR_titles = (TextView) convertView
                    .findViewById(R.id.DR_titles);
            item.DR_icon_drawer = (ImageView) convertView
                    .findViewById(R.id.DR_icon_drawer);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        String p = mList.get(position);
        if(p.equals("")){
        }else{
            item.DR_titles.setText(p);
        }

       // item.DR_icon_drawer.setImageDrawable(convertView.getResources().getDrawable(drawables[position]));

        Statics.setRobotoFontLight(ctx, item.DR_titles);

        animateView(convertView);
        return (convertView);
    }

    public void animateView(final View view) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.grow_fade_in_center);
        a.setDuration(500);
        if (view == null)
            return;
        view.startAnimation(a);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
