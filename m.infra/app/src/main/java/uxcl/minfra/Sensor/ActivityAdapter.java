package uxcl.minfra.Sensor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import uxcl.minfra.Model.Result;
import uxcl.minfra.R;

public class ActivityAdapter extends ArrayAdapter{

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ActivityAdapter(Context ctx, int resourceId, List<Result> results) {

        super( ctx, resourceId, results );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {

        /* create a new view of my layout and inflate it in the row */
        convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        /* Extract the city's object to show */
        Result city = (Result) getItem( position );

        /* Take the TextView from layout and set the city's name */
        TextView txtName = (TextView) convertView.findViewById(R.id.temp);
        txtName.setText(city.getTemp());

        /* Take the TextView from layout and set the city's wiki link */
        TextView txtWiki = (TextView) convertView.findViewById(R.id.location);
        txtWiki.setText(city.getLat()+' '+city.getLng());

        /* Take the ImageView from layout and set the city's image */
        ImageView imageCity = (ImageView) convertView.findViewById(R.id.icon);
        String uri = "@mipmap/ic_share";

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        imageCity.setImageDrawable(image);
        return convertView;
    }
}
