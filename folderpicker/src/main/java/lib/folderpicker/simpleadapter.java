package lib.folderpicker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class simpleadapter extends ArrayAdapter<String> {

	Activity context;
	ArrayList<String> namesList;
	ArrayList<String> typesList;

	public simpleadapter(Activity context, ArrayList<String> namesList, ArrayList<String> typesList) {
		super(context, R.layout.filerow, namesList);

		this.context = context;
		this.namesList = namesList;
		this.typesList = typesList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(R.layout.filerow, parent, false);

		ImageView imageView = (ImageView) convertView.findViewById(R.id.fp_iv_icon);
		TextView name = (TextView) convertView.findViewById(R.id.fp_tv_name);
		
		if( typesList.get(position).equals("folder") )
		{
			imageView.setImageResource( R.drawable.folder );
		}
		else
		{
			imageView.setImageResource( R.drawable.file );
		}

		name.setText( namesList.get(position) );

		return convertView;
	}

}