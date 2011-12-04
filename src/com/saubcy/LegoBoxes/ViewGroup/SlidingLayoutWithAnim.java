package com.saubcy.LegoBoxes.ViewGroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlidingLayoutWithAnim extends ViewGroup {
	// ~--- static fields ------------------------------------------------------
	
	private SelectListener listenner = null;
	
	// Number of internal View
	public int MAX_CHILDREN;
	// Number of view behind the visible view
	public int NUMBER_CHILDREN_UNDER;
	// Image names
	public String[] IMAGE_NAMES;
	// Image name text view
	public TextView IMAGE_NAME_TEXT_VIEW;
	// Number of pixel between the top of two Views
	public int SPACE_BETWEEN_VIEW = 80;

	// Scale ratio for each "layer" of children
	private static final float SCALE_RATIO = 0.8f;

	private static final int DECALAGE = 50;

	private static final int ALPHA_START = 255;
	private static final int ALPHA_DECREMENT = 55;

	private static final boolean SHIFT_IMAGES = false;
	private static final int ROTATION = 10;

	// Gesture
	private static final int MAJOR_MOVE = 120;

	// ~--- fields -------------------------------------------------------------

	// Animation
	private long mCurTime;
	private long mStartTime;
	private float mGap;
	private int mCenterViewIndextoReach = 0;
	private static final int DURATION = 300;
	private Runnable animationTask = new Runnable() {
		public void run() {
			mCurTime = SystemClock.uptimeMillis();

			long totalTime = mCurTime - mStartTime;

			// Animation end
			if (totalTime > DURATION) {
				mCenterViewIndex = mCenterViewIndextoReach;
				mGap = 0;
				removeCallbacks(animationTask);
			} else {
				float perCent = ((float) totalTime) / DURATION;

				mGap = (mCenterViewIndex - mCenterViewIndextoReach) * perCent;
				// We can slow down this with ?postDelayed to prevent useless power consumption
				post(this);
			}
			invalidate();
		}
	};

	// Index of the child to show by default
	int mCenterViewIndex = 0;
	int mCenterViewLeft = 0;
	int mCenterViewWidth = 0;
	private GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			int dx = (int) (e2.getX() - e1.getX());

			if ((Math.abs(dx) > MAJOR_MOVE) && (Math.abs(velocityX) > Math.abs(velocityY))) {
				if (velocityX > 0) {

					// left-right mouvement
					if (mCenterViewIndextoReach > 0) {
						mCenterViewIndextoReach = mCenterViewIndex - 1;
						mStartTime = SystemClock.uptimeMillis();
						post(animationTask);
					}
				} else {

					// right-left mouvement
					if (mCenterViewIndextoReach < MAX_CHILDREN - 1) {
						mCenterViewIndextoReach = mCenterViewIndex + 1;
						mStartTime = SystemClock.uptimeMillis();
						post(animationTask);
					}
				}

				return true;
			} else {
				return false;
			}
		}
 
		@Override
		public void onLongPress(MotionEvent e) {
			// Only for maps
			if (IMAGE_NAMES != null && IMAGE_NAMES.length > 0) {
				if (getChildAt(mCenterViewIndex) instanceof GalleryLayout) {
					// Get the selected Map Code
					GalleryLayout selectedView = (GalleryLayout) getChildAt(mCenterViewIndex);
					// Start the game settings activity
					if ( null != listenner ) {
						listenner.notifySelect(selectedView.getImgCode());
					}
				}
			}
		}

	});

	// ~--- constructors -------------------------------------------------------

	public SlidingLayoutWithAnim(Context context) {
		super(context);
		initSlidingAnimation();
	}

	public SlidingLayoutWithAnim(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSlidingAnimation();
	}

	public SlidingLayoutWithAnim(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
	}

	// ~--- methods ------------------------------------------------------------

	private void initSlidingAnimation() {
		setWillNotDraw(false);
		// Set the touch listener to select the image
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int count = getChildCount();

		final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);

		int maxChildWidth = 0;
		int maxChildHeight = 0;
		float childWidth = 0;
		float childHeight = 0;

		// Measure all children
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			// Measure Child
			measureChild(child, widthMeasureSpec, heightMeasureSpec);

			// Process Layout dimension
			childWidth = child.getMeasuredWidth();
			if (childWidth > maxChildWidth) {
				maxChildWidth = (int) childWidth;
			}

			childHeight = child.getMeasuredHeight();
			if (childHeight > maxChildHeight) {
				maxChildHeight = (int) childHeight;
			}

			// Enable cache for animation
			child.setDrawingCacheEnabled(true);
		}

		// Store Child Top and Height
		mCenterViewLeft = (specWidthSize / 2) - (maxChildWidth / 2);
		mCenterViewWidth = maxChildWidth;

		setMeasuredDimension(specWidthSize, specHeightSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		final int count = getChildCount();
		final int heightCenter = getHeight() / 2;
		final Paint p = new Paint();

		// Hide all view but not the center view
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			// If no GAP we will display the Center view
			if ((i == mCenterViewIndex) && (mGap == 0)) {
				continue;
			}
			child.setVisibility(View.INVISIBLE);
		}

		// Draw all views before the center view, and 1 view before that one
		for (int i = NUMBER_CHILDREN_UNDER; i > 1; i--) {
			final int childIndex = mCenterViewIndex - i;
			final float distance = i - mGap;

			if (childIndex >= 0) {
				final View child = getChildAt(childIndex);

				drawBitmapBeforeCenter(child, canvas, heightCenter, p, distance);
			}
		}

		// Draw all views after the center view, and 1 view before that one
		int numberChildrenUnder = NUMBER_CHILDREN_UNDER;
		if (NUMBER_CHILDREN_UNDER % 2 == 0) {
			numberChildrenUnder = NUMBER_CHILDREN_UNDER - 1;
		}
		for (int i = numberChildrenUnder; i > 1; i--) {
			final int childIndex = mCenterViewIndex + i;
			final float distance = i + mGap;

			if (childIndex < MAX_CHILDREN) {
				final View child = getChildAt(childIndex);

				drawBitmapAfterCenter(child, canvas, heightCenter, p, distance);
			}
		}

		// Display the visible view
		final View centerView = getChildAt(mCenterViewIndex);

		if (centerView != null) {

			// If no GAP centerView is set to visisble
			if (mGap == 0) {
				final View beforeCenterView = getChildAt(mCenterViewIndex - 1);

				drawBitmapBeforeCenter(beforeCenterView, canvas, heightCenter, p, 1);

				final View afterCenterView = getChildAt(mCenterViewIndex + 1);

				drawBitmapAfterCenter(afterCenterView, canvas, heightCenter, p, 1);

				centerView.setVisibility(View.VISIBLE);

				if (IMAGE_NAMES != null && IMAGE_NAMES.length > 0) {
					IMAGE_NAME_TEXT_VIEW.setText(IMAGE_NAMES[mCenterViewIndex]);
				}
			} else {

				// top to bottom movement
				if (mGap > 0.0f) {
					final View childAfter = getChildAt(mCenterViewIndex + 1);

					drawBitmapAfterCenter(childAfter, canvas, heightCenter, p, 1 + mGap);

					// Find the correct overlap
					if (mGap > 0.5f) {
						drawBitmapAfterCenter(centerView, canvas, heightCenter, p, mGap);

						final View childBefore = getChildAt(mCenterViewIndex - 1);

						drawBitmapBeforeCenter(childBefore, canvas, heightCenter, p, 1 - mGap);
					} else {
						final View childBefore = getChildAt(mCenterViewIndex - 1);

						drawBitmapBeforeCenter(childBefore, canvas, heightCenter, p, 1 - mGap);

						drawBitmapAfterCenter(centerView, canvas, heightCenter, p, mGap);
					}

					// bottom to top movement
				} else {
					final View childBefore = getChildAt(mCenterViewIndex - 1);

					drawBitmapBeforeCenter(childBefore, canvas, heightCenter, p, 1 - mGap);

					// Find the correct overlap
					if (mGap < -0.5f) {
						drawBitmapBeforeCenter(centerView, canvas, heightCenter, p, -mGap);

						final View childAfter = getChildAt(mCenterViewIndex + 1);

						drawBitmapAfterCenter(childAfter, canvas, heightCenter, p, 1 + mGap);
					} else {
						final View childAfter = getChildAt(mCenterViewIndex + 1);

						drawBitmapAfterCenter(childAfter, canvas, heightCenter, p, 1 + mGap);

						drawBitmapBeforeCenter(centerView, canvas, heightCenter, p, -mGap);
					}
				}
			}
		}
	}

	// Draw the bitmap of a view at the right position
	// goUp to true if before the center view, otherwise false
	private void drawBitmapBeforeCenter(View child, Canvas canvas, int heightCenter, Paint p, float coef) {
		if (child != null) {
			final int childHeight = child.getMeasuredHeight();

			final Bitmap cache = child.getDrawingCache();

			if (cache != null) {
				final int childHeightCenter = (childHeight / 2);
				float scale = (float) Math.pow(SCALE_RATIO, Math.abs(coef));

				p.setAlpha((int) (ALPHA_START - ALPHA_DECREMENT * coef));
				canvas.save();
				canvas.translate(mCenterViewLeft - (SPACE_BETWEEN_VIEW * coef), heightCenter);
				canvas.scale(scale, scale);
				if (SHIFT_IMAGES) {
					canvas.rotate(coef * ROTATION);
					canvas.drawBitmap(cache, 0, -childHeightCenter - DECALAGE * coef, p);
				} else {
					canvas.drawBitmap(cache, 0, -childHeightCenter, p);
				}
				canvas.restore();
			}
		}
	}

	private void drawBitmapAfterCenter(View child, Canvas canvas, int heightCenter, Paint p, float coef) {
		if (child != null) {
			final int childHeight = child.getMeasuredHeight();

			final Bitmap cache = child.getDrawingCache();

			if (cache != null) {
				final int childHeightCenter = (childHeight / 2);
				float scale = (float) Math.pow(SCALE_RATIO, Math.abs(coef));

				p.setAlpha((int) (ALPHA_START - ALPHA_DECREMENT * coef));
				canvas.save();
				canvas.translate(mCenterViewLeft + mCenterViewWidth + (SPACE_BETWEEN_VIEW * coef), heightCenter);
				canvas.scale(scale, scale);

				if (SHIFT_IMAGES) {
					canvas.rotate(-coef * ROTATION);
					canvas.drawBitmap(cache, -child.getWidth(), -childHeightCenter - DECALAGE * coef, p);
				} else {
					canvas.drawBitmap(cache, -child.getWidth(), -childHeightCenter, p);
				}
				canvas.restore();
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		final int count = getChildCount();
		final int widthCenter = getWidth() / 2;
		final int heightCenter = getHeight() / 2;

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			final int childWidth = child.getMeasuredWidth();
			final int childHeight = child.getMeasuredHeight();
			final int childLeft = widthCenter - (childWidth / 2);
			final int childTop = heightCenter - (childHeight / 2);

			setChildFrame(child, childLeft, childTop, childWidth, childHeight);
		}
	}

	// ~--- set methods --------------------------------------------------------

	private void setChildFrame(View child, int left, int top, int width, int height) {
		child.layout(left, top, left + width, top + height);
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	public String getCurrentImgCode() {
		if (getChildAt(mCenterViewIndex) instanceof GalleryLayout) {
			return ((GalleryLayout) getChildAt(mCenterViewIndex)).getImgCode();
		}
		return null;
	}
	
	public static class GalleryLayout extends LinearLayout {

		private String Code;

		public GalleryLayout(String code, Context context) {
			super(context);
			this.Code = code;
		}

		public String getImgCode() {
			return Code;
		}

	}
	
	public void setListener(SelectListener sl) {
		this.listenner = sl;
	}
	
	public interface SelectListener {
		public void notifySelect(String code);
	}
	
}
