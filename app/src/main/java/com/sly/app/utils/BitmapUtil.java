package com.sly.app.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 处理图片的工具类
 * @date 2016-1-15 下午2:14:02
 */
public class BitmapUtil {
	/**
	 * 把Bitmap转换成Bytes
	 * @param bm
	 * @return byte[]
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 把Bitmap转换成Bytes
	 * @param imagePath
	 * @return Bitmap
	 */
	public static Bitmap Path2Bitmap(String  imagePath) {
		Bitmap img = BitmapFactory.decodeFile(imagePath,getBitmapOption(2));
		return img;
	}
	private static BitmapFactory.Options getBitmapOption(int inSampleSize)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inSampleSize = inSampleSize;
		return options;
	}

	/**
	 * 把Bytes转换成Bimap
	 * @param b
	 * @return Bitmap
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 100*100圆形头像用
	 * @param bmp
	 * @return
	 */
	public static Bitmap getCropped2Bitmap(Bitmap bmp) {
		int radius = 40;
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
			scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter, false);
		else
			scaledSrcBmp = bmp;
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

		return output;
	}

	/**
	 * 图片转成string
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 40, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/**
	 * string转成bitmap
	 * @param str
	 */
	public static Bitmap convertStringToBitmap(String str) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(str, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 自动计算Bitmap缩放比率
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
     * @return
     */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {



		int initialSize = computeInitialSampleSize(options, minSideLength,

				maxNumOfPixels);


		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}


		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,

												int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;


		int lowerBound = (maxNumOfPixels == -1) ? 1 :

				(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 :

				(int) Math.min(Math.floor(w / minSideLength),

						Math.floor(h / minSideLength));


		if (upperBound < lowerBound) {

			// return the larger one when there is no overlapping zone.

			return lowerBound;

		}


		if ((maxNumOfPixels == -1) &&

				(minSideLength == -1)) {

			return 1;

		} else if (minSideLength == -1) {

			return lowerBound;

		} else {

			return upperBound;

		}

	}
	/**
	 * 测量并把bitmap加载到内存中避免内存溢出
	 */
	public static Bitmap getBitmap(String s1) {
		Bitmap bitmap;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先设置为TRUE不加载到内存中，但可以得到宽和高
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(s1, options); // 此时返回bm为空
		// 计算缩放比
		int be = BitmapUtil.computeSampleSize(options, -1, 480*720);
		//设置缩放比
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		// 这样就不会内存溢出了
		bitmap = BitmapFactory.decodeFile(s1, options);
		return bitmap;
	}

	/**
	 * 通过uri获取图片并进行压缩
	 *
	 * @param uri
	 */
	public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws IOException {
		InputStream input = ac.getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;//optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		int originalWidth = onlyBoundsOptions.outWidth;
		int originalHeight = onlyBoundsOptions.outHeight;
		if ((originalWidth == -1) || (originalHeight == -1))
			return null;
		//图片分辨率以480x800为标准
		float hh = 100;//这里设置高度为800f
		float ww = 100;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (originalWidth / ww);
		} else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (originalHeight / hh);
		}
		if (be <= 0)
			be = 1;
		//比例压缩
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = be;//设置缩放比例
		bitmapOptions.inDither = true;//optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		input = ac.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();

		return compressImage(bitmap);//再进行质量压缩
	}

	/**
	 * 质量压缩方法
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			//第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
			image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 5;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 通过Uri获取文件
	 * @param ac
	 * @param uri
	 * @return
	 */
	public static File getFileFromMediaUri(Context ac, Uri uri) {
		if(uri.getScheme().toString().compareTo("content") == 0){
			ContentResolver cr = ac.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
			if (cursor != null) {
				cursor.moveToFirst();
				String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
				cursor.close();
				if (filePath != null) {
					return new File(filePath);
				}
			}
		}else if(uri.getScheme().toString().compareTo("file") == 0){
			return new File(uri.toString().replace("file://",""));
		}
		return null;
	}

	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path 图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm     需要旋转的图片
	 * @param degree 旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 *
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			bitmap = null;
		} catch (IOException e) {
			e.printStackTrace();
			bitmap = null;
		}
		return bitmap;
	}

	/**
	 * 绘制水印图片
	 * @param src 原图
	 * @param watermark 水印
	 * @param paddingLeft
	 * @param paddingTop
	 * @return
	 */
	public static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
											   int paddingLeft, int paddingTop) {
		if (src == null) {
			return null;
		}
		int width = src.getWidth();
		int height = src.getHeight();
		//创建一个bitmap
		Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		//将该图片作为画布
		Canvas canvas = new Canvas(newb);
		//在画布 0，0坐标上开始绘制原始图片
		canvas.drawBitmap(src, 0, 0, null);
		//在画布上绘制水印图片
		canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
		Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(35);
		textPaint.setStrokeWidth(1);
		textPaint.setColor(Color.RED);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);
		String text = "鞋品会";
		int yPos = (int) ((width / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;//得到文本绘制的Y坐标
		canvas.drawText(text, yPos, width, textPaint);
		// 保存
//		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.save();
		// 存储
		canvas.restore();
		return newb;
	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}
}
