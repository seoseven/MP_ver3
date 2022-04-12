package com.nwu.ypf.mp_ver3.db;

import android.net.Uri;

import androidx.room.TypeConverter;

/**
 * 把音乐uri地址数据解析成数据库可以识别的String类型
 */
public class Converters {
    @TypeConverter
    public static Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(Uri uri) {
        return uri == null ? null : uri.toString();
    }
}
