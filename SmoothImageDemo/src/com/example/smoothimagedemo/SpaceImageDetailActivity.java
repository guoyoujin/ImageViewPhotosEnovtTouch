package com.example.smoothimagedemo;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import com.imageview.touch.PhotoViewAttacher;
import com.imageview.touch.PhotoViewAttacher.OnMatrixChangedListener;
import com.imageview.touch.PhotoViewAttacher.OnPhotoTapListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.roamer.ui.view.SmoothImageView;

public class SpaceImageDetailActivity extends Activity {

	private ArrayList<String> mDatas;
	private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	private PhotoViewAttacher mAttacher;
	SmoothImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
		mPosition = getIntent().getIntExtra("position", 0);
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		imageView = new SmoothImageView(this);
//		imageView.setImageResource(R.drawable.temp);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// scaleAnimation.setDuration(300);
		// scaleAnimation.setInterpolator(new AccelerateInterpolator());
		// imageView.startAnimation(scaleAnimation);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(imageView);
		ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
					@Override
					public void onTransformComplete(int mode) {
						if (mode == 2) {
							finish();
						}
					}
				});
				imageView.transformOut();
			}
		});


	}

	@Override
	public void onBackPressed() {
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}
	class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
			float xPercentage = x * 100f;
			float yPercentage = y * 100f;
//
//			if (null != mCurrentToast) {
//				mCurrentToast.cancel();
//			}
//
//			mCurrentToast = Toast.makeText(PhotosActivity.this,
//					String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage), Toast.LENGTH_SHORT);
//			mCurrentToast.show();
		}
	}

	class MatrixChangeListener implements OnMatrixChangedListener {
		@Override
		public void onMatrixChanged(RectF rect) {
//			mCurrMatrixTv.setText(rect.toString());
		}
	}


}
