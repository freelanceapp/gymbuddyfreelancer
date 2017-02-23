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
import com.backbencherslab.gymbuddy.model.Friend;


public class FriendsListAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private List<Friend> itemsList;

	ImageLoader imageLoader = App.getInstance().getImageLoader();

	public FriendsListAdapter(Activity activity, List<Friend> itemsList) {

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

		public TextView mLocation;
		public TextView mOnline;
        public TextView profileUsername;
		public TextView profileFullname;
		public CircularImageView profilePhoto;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.friend_list_row, null);
			
			Friend friend = itemsList.get(position);
			
			viewHolder = new ViewHolder();
			
			viewHolder.profilePhoto = (CircularImageView) convertView.findViewById(R.id.personPhoto);
			viewHolder.profileFullname = (TextView) convertView.findViewById(R.id.personFullName);
            viewHolder.profileUsername = (TextView) convertView.findViewById(R.id.personUsername);
			viewHolder.mLocation = (TextView) convertView.findViewById(R.id.timeAgo);
            viewHolder.mOnline = (TextView) convertView.findViewById(R.id.online);

            convertView.setTag(viewHolder);

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.profilePhoto.setTag(position);
        viewHolder.mLocation.setTag(position);
        viewHolder.mOnline.setTag(position);
		
		final Friend friend = itemsList.get(position);

        viewHolder.profileFullname.setText(friend.getFriendUserFullname());

        viewHolder.profileUsername.setText("@" + friend.getFriendUserUsername());

		if (friend.getFriendLocation().length() > 0) {

			viewHolder.mLocation.setText(friend.getFriendLocation());
			viewHolder.mLocation.setVisibility(View.VISIBLE);

		} else {

			viewHolder.mLocation.setVisibility(View.GONE);
		}

        if (friend.isOnline()) {

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

        if (friend.getFriendUserPhotoUrl().length() > 0) {

            imageLoader.get(friend.getFriendUserPhotoUrl(), ImageLoader.getImageListener(viewHolder.profilePhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.profilePhoto.setImageResource(R.drawable.profile_default_photo);
        }

		return convertView;
	}
}