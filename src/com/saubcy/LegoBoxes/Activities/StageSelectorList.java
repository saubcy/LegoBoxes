package com.saubcy.LegoBoxes.Activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saubcy.LegoBoxes.Animation.AnimationFactory;
import com.saubcy.LegoBoxes.Interface.SelectListener;

public class StageSelectorList extends BaseListActivity {

	private Animation mButtonFlickerAnimation;
	private ArrayList<LevelMetaData> mLevelData;
	private boolean mLevelSelected;
	
	private int row_layout;
	private int text_id;

	private SelectListener listener = null;

	public StageSelectorList() {

	}
	
	public void setRowLayoutId(int id) {
		this.row_layout = id;
	}
	
	public void setTextId(int id) {
		this.text_id = id;
	}

	public RelativeLayout getRoot() {
		return Root;
	}

	public void setListener(SelectListener sl) {
		this.listener = sl;
	}

	public void setLevelData(ArrayList<LevelMetaData> levels) {
		mLevelData = levels;
		DisableItemArrayAdapter<LevelMetaData> adapter = 
				new DisableItemArrayAdapter<LevelMetaData>(
						this, row_layout, text_id, mLevelData);
		setListAdapter(adapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadViews();
	}

	@Override
	protected void initBefore() {
		mLevelSelected = false;
		mButtonFlickerAnimation = 
				AnimationFactory.getButtonFlicker(this);
	}

	private void loadViews() {
		list.setDividerHeight(0);
		list.setFooterDividersEnabled(false);
		list.setHeaderDividersEnabled(false);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (!mLevelSelected) {
			super.onListItemClick(l, v, position, id);
			LevelMetaData selectedLevel = mLevelData.get(position);
			mLevelSelected = true;

			TextView text = (TextView)v.findViewById(text_id);
			if (text != null) {
				if ( null != listener ) {
					listener.notifyQuick();
				}
				text.startAnimation(mButtonFlickerAnimation);
				mButtonFlickerAnimation.setAnimationListener(
						new EndActivityAfterAnimation(selectedLevel.code));
			} 
		}
	}
	
	protected class EndActivityAfterAnimation implements Animation.AnimationListener {
		private int code;
        
        public EndActivityAfterAnimation(int code) {
        	this.code = code;
        }
            

        public void onAnimationEnd(Animation animation) {
        	if ( null != listener ) {
				listener.notifySelect(code);
			}
            finish();
        }

        public void onAnimationRepeat(Animation animation) {
            
        }

        public void onAnimationStart(Animation animation) {
            
        }
        
    }

	public class LevelMetaData {
		public String name;
		public int code;
		
		public LevelMetaData(String name, int code) {
			this.name = name;
			this.code = code;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	protected class DisableItemArrayAdapter<T> extends ArrayAdapter<T> {
		protected static final int TYPE_ENABLED = 0;
		protected static final int TYPE_COUNT = 3;

		private int mRowResource;
		private Context mContext;
		private int mTextViewResource;

		public DisableItemArrayAdapter(Context context, int resource,
				int textViewResourceId, List<T> objects) {
			super(context, resource, textViewResourceId, objects);
			mRowResource = resource;
			mContext = context;
			mTextViewResource = textViewResourceId;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public int getItemViewType(int position) {
			int type = TYPE_ENABLED;
			return type;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isEmpty() {
			return mLevelData.size() > 0;
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent) {
			View sourceView = null;
			if (convertView != null && convertView.getId() == mRowResource) {
				sourceView = convertView;
			} else {
				sourceView = LayoutInflater.from(mContext).inflate(
						mRowResource, parent, false);
			}

			TextView view = (TextView)sourceView.findViewById(mTextViewResource);
			if (view != null) {
				view.setText(mLevelData.get(position).name);
			}

			return sourceView;
		}

	}
}
