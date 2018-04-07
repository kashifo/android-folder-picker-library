package lib.folderpicker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderAdapter extends ArrayAdapter<FilePojo> {

	Activity context;
	ArrayList<FilePojo> dataList;

	public FolderAdapter(Activity context, ArrayList<FilePojo> dataList) {

		super(context, R.layout.fp_filerow, dataList);
		this.context = context;
		this.dataList = dataList;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(R.layout.fp_filerow, parent, false);

		ImageView imageView = (ImageView) convertView.findViewById(R.id.fp_iv_icon);
		TextView name = (TextView) convertView.findViewById(R.id.fp_tv_name);
		
		if( dataList.get(position).isFolder() )
		{
			imageView.setImageResource( R.drawable.fp_folder);
		}
		else
		{
			imageView.setImageResource( R.drawable.fp_file);
		}

		name.setText( dataList.get(position).getName() );

		return convertView;
	}

}