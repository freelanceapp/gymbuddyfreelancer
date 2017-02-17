package com.backbencherslab.gymbuddy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;

import java.util.List;

import com.backbencherslab.gymbuddy.R;
import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.model.Guest;


public class GuestListAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private List<Guest> itemsList;

	ImageLoader imageLoader = App.getInstance().getImageLoader();

	public GuestListAdapter(Activity activity, List<Guest> itemsList) {

		this.activity = activity;
		this.itemsList = itemsList;
	}

	@Override
	public int getCount() {

		return itemsList.size();
	}

	@Override
	public Object getItem(int location) {

		return itemsList.get(location);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
	static class ViewHolder {

		public TextView mTimeAgo;
        public TextView mOnline;
        public TextView profileUsername;
		public TextView profileFullname;
		public CircularImageView profilePhoto;
		public Boolean isMyRow = false;
	        
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.guest_list_row, null);
			
			Guest guest = itemsList.get(position);
			
			viewHolder = new ViewHolder();
			
			viewHolder.profilePhoto = (CircularImageView) convertView.findViewById(R.id.personPhoto);
			viewHolder.profileFullname = (TextView) convertView.findViewById(R.id.personFullName);
            viewHolder.profileUsername = (TextView) convertView.findViewById(R.id.personUsername);
			viewHolder.mTimeAgo = (TextView) convertView.findViewById(R.id.timeAgo);
            viewHolder.mOnline = (TextView) convertView.findViewById(R.id.online);

            convertView.setTag(viewHolder);

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.profilePhoto.setTag(position);
        viewHolder.mTimeAgo.setTag(position);
        viewHolder.mOnline.setTag(position);
		
		final Guest guest = itemsList.get(position);

        viewHolder.profileFullname.setText(guest.getGuestUserFullname());

        viewHolder.profileUsername.setText("@" + guest.getGuestUserUsername());

        viewHolder.mTimeAgo.setText(guest.getTimeAgo());

        if (guest.isOnline()) {

            viewHolder.mOnline.setVisibility(View.VISIBLE);
            viewHolder.mOnline.setText(activity.getString(R.string.label_online));

        } else {

            viewHolder.mOnline.setVisibility(View.GONE);
            viewHolder.mOnline.setText(activity.getString(R.string.label_offline));
        }
		
        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        if (!itemsList.get(position).isVerify()) {

            viewHolder.profileFullname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        } else {

            viewHolder.profileFullname.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.profile_verify_icon, 0);
        }

        if (guest.getGuestUserPhotoUrl().length() > 0) {

            imageLoader.get(guest.getGuestUserPhotoUrl(), ImageLoader.getImageListener(viewHolder.profilePhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.profilePhoto.setImageResource(R.drawable.profile_default_photo);
        }

		return convertView;
	}
}