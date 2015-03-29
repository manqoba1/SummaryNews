package com.sifiso.codetribe.summarylib.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.sifiso.codetribe.summarylib.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Util {

    public interface UtilAnimationListener {
        public void onAnimationEnded();
    }

    public interface UtilPopupListener {
        public void onItemSelected(int index);
    }


    public static void flashInfinite(final View view, final long duration) {
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        an.setRepeatMode(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flashInfinite(view, duration);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();

    }


    public static void shakeX(final View v, int duration, int max, final UtilAnimationListener listener) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + 20f);
        an.setDuration(duration);
        an.setRepeatMode(ObjectAnimator.REVERSE);
        an.setRepeatCount(max);
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();
    }


    public static void shrink(View view, long duration, final UtilAnimationListener listener) {
        ObjectAnimator anx = ObjectAnimator.ofFloat(view, "scaleX", 1, 0);
        ObjectAnimator any = ObjectAnimator.ofFloat(view, "scaleY", 1, 0);

        anx.setDuration(duration);
        any.setDuration(duration);
        anx.setInterpolator(new AccelerateInterpolator());
        any.setInterpolator(new AccelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>();
        animatorList.add((Animator) anx);
        animatorList.add((Animator) any);
        set.playTogether(animatorList);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    public static void explode(View view, long duration, final UtilAnimationListener listener) {
        ObjectAnimator anx = ObjectAnimator.ofFloat(view, "scaleX", 0, 1);
        ObjectAnimator any = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);

        anx.setDuration(duration);
        any.setDuration(duration);
        anx.setInterpolator(new AccelerateInterpolator());
        any.setInterpolator(new AccelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>();
        animatorList.add((Animator) anx);
        animatorList.add((Animator) any);
        set.playTogether(animatorList);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }


    public static Location getLocationFromExif(String filePath) {
        String sLat = "", sLatR = "", sLon = "", sLonR = "";
        try {
            ExifInterface ef = new ExifInterface(filePath);
            sLat = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            sLon = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            sLatR = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            sLonR = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        } catch (IOException e) {
            return null;
        }

        double lat = DMSToDouble(sLat);
        if (lat > 180.0) return null;
        double lon = DMSToDouble(sLon);
        if (lon > 180.0) return null;

        lat = sLatR.contains("S") ? -lat : lat;
        lon = sLonR.contains("W") ? -lon : lon;

        Location loc = new Location("exif");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        Log.i(LOG, "----> File Exif lat: " + loc.getLatitude() + " lng: " + loc.getLongitude());
        return loc;
    }

    static String LOG = Util.class.getSimpleName();

    //-------------------------------------------------------------------------
    private static double DMSToDouble(String sDMS) {
        double dRV = 999.0;
        try {
            String[] DMSs = sDMS.split(",", 3);
            String s[] = DMSs[0].split("/", 2);
            dRV = (new Double(s[0]) / new Double(s[1]));
            s = DMSs[1].split("/", 2);
            dRV += ((new Double(s[0]) / new Double(s[1])) / 60);
            s = DMSs[2].split("/", 2);
            dRV += ((new Double(s[0]) / new Double(s[1])) / 3600);
        } catch (Exception e) {
        }
        return dRV;
    }

    public static final long HOUR = 60 * 60 * 1000;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final long WEEKS = 2 * WEEK;
    public static final long MONTH = 30 * DAY;


    public static String getTruncated(double num) {
        String x = "" + num;
        int idx = x.indexOf(".");
        String xy = x.substring(idx + 1);
        if (xy.length() > 2) {
            String y = x.substring(0, idx + 2);
            return y;
        } else {
            return x;
        }
    }

    public static Intent getMailIntent(Context ctx, String email, String message, String subject,
                                       File file) {

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        if (email == null) {
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"",
                    "aubrey.malabie@gmail.com"});
        } else {
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        }

        if (subject == null) {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                    subject);
        } else {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        sendIntent.setType("application/pdf");

        return sendIntent;
    }

    public static double getPercentage(int totalMarks, int attained) {
        BigDecimal total = new BigDecimal(totalMarks);
        BigDecimal totStu = new BigDecimal(attained);
        double perc = totStu.divide(total, 3, BigDecimal.ROUND_UP).doubleValue();
        perc = perc * 100;
        return perc;
    }

    public static String formatCellphone(String cellphone) {
        StringBuilder sb = new StringBuilder();
        String suff = cellphone.substring(0, 3);
        String p1 = cellphone.substring(3, 6);
        String p2 = cellphone.substring(6);
        sb.append(suff).append(" ");
        sb.append(p1).append(" ");
        sb.append(p2);
        return sb.toString();
    }

    public static void hide(View view, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 0);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(scaleX, scaleY);
        animSetXY.setInterpolator(new AccelerateInterpolator());
        animSetXY.setDuration(duration);
        if (duration == 0) {
            animSetXY.setDuration(200);
        }
        animSetXY.start();
    }

    public static void show(View view, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(scaleX, scaleY);
        animSetXY.setInterpolator(new OvershootInterpolator());
        animSetXY.setDuration(duration);
        if (duration == 0) {
            animSetXY.setDuration(300);
        }
        animSetXY.start();
    }

    public static void animateScaleY(View txt, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(txt, "scaleY", 0);
        an.setRepeatCount(1);
        an.setDuration(duration);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }

    public static void animateRotationY(View view, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(view, "rotation", 0.0f, 360f);
        //an.setRepeatCount(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    public static void animateRollup(View view, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.0f);
        //an.setRepeatCount(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    public static void animateSlideRight(View view, long duration) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(
                view, "translate", 0, 100, 0, 100);
        //an.setRepeatCount(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    public static void animateFlipFade(Context ctx, View v) {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(ctx,
                R.animator.flip_fade);
        set.setTarget(v);
        set.start();
    }

    public static ArrayList<String> getRecurStrings(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String day = getDayOfWeek(dayOfWeek);
        boolean isWeekDay = false;
        if (dayOfWeek > 1 && dayOfWeek < 7) {
            isWeekDay = true;
        }
        Log.d("Util", "#########");
        Log.d("Util", "dayOfWeek: " + dayOfWeek + " day: " + day
                + " isWeekDay: " + isWeekDay);
        // which week?
        int week = 0;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth < 8) {
            week = 1;
        }
        if (dayOfMonth > 7 && dayOfMonth < 15) {
            week = 2;
        }
        if (dayOfMonth > 14 && dayOfMonth < 22) {
            week = 3;
        }
        if (dayOfMonth > 21) {
            week = 4;
        }
        Log.d("Util", "dayOfMonth: " + dayOfMonth + " week in month: "
                + getWeekOfMonth(week));

        ArrayList<String> list = new ArrayList<String>();
        list.add("One time event");
        list.add("Daily Event");

        list.add("Every Week day(Mon-Fri)");
        list.add("Weekly on " + getDayOfWeek(dayOfWeek));
        list.add("Monthly (every " + getWeekOfMonth(week) + " "
                + getDayOfWeek(dayOfWeek));
        list.add("Monthly on day " + dayOfMonth);
        String month = getMonth(cal.get(Calendar.MONTH));
        list.add("Yearly on " + dayOfMonth + " " + month);
        return list;
    }

    public static String getMonth(int mth) {
        switch (mth) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return null;
    }

    public static String getWeekOfMonth(int i) {
        switch (i) {
            case 1:
                return FIRST_WEEK;
            case 2:
                return SECOND_WEEK;
            case 4:
                return FOURTH_WEEK;
            case 3:
                return THIRD_WEEK;

        }
        return MONDAY;
    }

    public static String getDayOfWeek(int i) {
        switch (i) {
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
            case 6:
                return FRIDAY;
            case 7:
                return SATURDAY;
            case 1:
                return SUNDAY;

        }
        return MONDAY;
    }


    public static final String MONDAY = "Monday";
    public static final String TUESDAY = "Tuesday";
    public static final String WEDNESDAY = "Wednesday";
    public static final String THURSDAY = "Thursday";
    public static final String FRIDAY = "Friday";
    public static final String SATURDAY = "Saturday";
    public static final String SUNDAY = "Sunday";

    public static final String FIRST_WEEK = "First";
    public static final String SECOND_WEEK = "Second";
    public static final String THIRD_WEEK = "Third";
    public static final String FOURTH_WEEK = "Fourth";

    public static int[] getDateParts(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        int[] ints = {cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),
                cal.get(Calendar.YEAR)};
        return ints;
    }

    public static File getDirectory(String dir) {
        File sd = Environment.getExternalStorageDirectory();
        File appDir = new File(sd, dir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        return appDir;

    }

    public static String getLongTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(date);
    }

    public static String getShortTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

    public static int[] getTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hr = df.format(date);
        df = new SimpleDateFormat("mm");
        String min = df.format(date);
        int[] time = {Integer.parseInt(hr), Integer.parseInt(min)};
        return time;
    }

    public static long getSimpleDate(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getSimpleDate(int day, int month, int year) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        return getSimpleDate(cal.getTime());
    }

    public static String getLongerDate(int day, int month, int year) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        Date d = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
        return df.format(d);
    }

    public static String getLongDate(int day, int month, int year) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        Date d = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        return df.format(d);
    }

    public static String getLongestDate(int day, int month, int year) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month);
        cal.set(GregorianCalendar.DAY_OF_MONTH, day);
        Date d = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
        return df.format(d);
    }

    public static String getLongDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
        return df.format(date);
    }

    public static String getLongerDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return df.format(date);
    }

    public static String getLongDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(
                "EEEE, dd MMMM yyyy HH:mm:ss");
        return df.format(date);
    }

    public static Calendar getLongDateTimeNoSeconds(Calendar cal) {

        int year = cal.get(Calendar.YEAR);
        int mth = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, mth);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Log.d("Util", "Reset date: " + getLongDateTimeNoSeconds(c.getTime()));
        return c;
    }

    public static String getLongDateTimeNoSeconds(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm");
        return df.format(date);
    }

    public static String getLongDateForPDF(long date) {
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
        Date d = new Date(date);
        return df.format(d);
    }

    public static String getShortDateForPDF(long startDate, long endDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Date dtStart = new Date(startDate);
        Date dtEnd = new Date(endDate);

        return df.format(dtStart) + " to " + df.format(dtEnd);
    }

    public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /* if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation. */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        if (type == null) {
            type = "image/jpg";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        if (cursor == null) {
            return 1;
        }
        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private static int MAX_IMAGE_DIMENSION = 720;

    static public boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();
        Log.w("Util", "--------- disk storage state is: " + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                Log.i("Util", "************ storage is writable: " + writable);
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }
}