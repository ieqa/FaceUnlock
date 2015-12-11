In order to make face detection work in portrait mode, I modify the *CameraBridgeViewBase.java* in OpenCV Library.
In the **deliverAndDrawFrame** method I add the following code:
```Java
canvas.rotate(90,0,0);
mScale = canvas.getWidth() / (float)mCacheBitmap.getHeight();
float scale2 = canvas.getHeight() / (float)mCacheBitmap.getWidth();
if(scale2 > mScale){
mScale = scale2;
}
if (mScale != 0) {
     canvas.scale(mScale, mScale,0,0);
}
canvas.drawBitmap(mCacheBitmap, 0, -mCacheBitmap.getHeight(), null);
```
And delete:
```Java
if (mScale != 0) {
     canvas.drawBitmap(mCacheBitmap, new Rect(0,0,mCacheBitmap.getWidth(), mCacheBitmap.getHeight()),
     new Rect((int)((canvas.getWidth() - mScale*mCacheBitmap.getWidth()) / 2),
     (int)((canvas.getHeight() - mScale*mCacheBitmap.getHeight()) / 2),
     (int)((canvas.getWidth() - mScale*mCacheBitmap.getWidth()) / 2 + mScale*mCacheBitmap.getWidth()),
     (int)((canvas.getHeight() - mScale*mCacheBitmap.getHeight()) / 2 + mScale*mCacheBitmap.getHeight())), null);
} else {
     canvas.drawBitmap(mCacheBitmap, new Rect(0,0,mCacheBitmap.getWidth(), mCacheBitmap.getHeight()),
     new Rect((canvas.getWidth() - mCacheBitmap.getWidth()) / 2,
     (canvas.getHeight() - mCacheBitmap.getHeight()) / 2,
     (canvas.getWidth() - mCacheBitmap.getWidth()) / 2 + mCacheBitmap.getWidth(),
     (canvas.getHeight() - mCacheBitmap.getHeight()) / 2 + mCacheBitmap.getHeight()), null);
}
```
