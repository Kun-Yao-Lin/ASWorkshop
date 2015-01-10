package tw.com.akdg.helloandroidstudio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.EncodeHintType;

import java.util.HashMap;


public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    private DisplayMetrics metrics;
    private ImageView QR_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metrics = this.getResources().getDisplayMetrics();
        QR_code = (ImageView) this.findViewById(R.id.qr_code);
        try {
            createQRcode("http://goo.gl/2SJIkk");
        } catch (Exception e) {
            Log.e(TAG, "createQRcode failed", e);
        }
    }

    private void createQRcode(final String content) throws Exception {
        try {
            final Bitmap bitmap = encodeAsBitmap(content, com.google.zxing.BarcodeFormat.QR_CODE, metrics.widthPixels, metrics.widthPixels);
            if (null != bitmap) {
                QR_code.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }

    private Bitmap encodeAsBitmap(String contents, com.google.zxing.BarcodeFormat format,
                                  int desiredWidth, int desiredHeight) throws com.google.zxing.WriterException {
        final int WHITE = this.getResources().getColor(android.R.color.transparent);
        final int BLACK = 0xFF000000;

        HashMap<EncodeHintType, String> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new HashMap<EncodeHintType, String>(2);
            hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET, encoding);
        }
        com.google.zxing.MultiFormatWriter writer = new com.google.zxing.MultiFormatWriter();
        com.google.zxing.common.BitMatrix result = writer.encode(contents, format, desiredWidth,
                desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
