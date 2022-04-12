package com.nwu.ypf.mp_ver3.repository;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.MusicInfo;
import com.nwu.ypf.mp_ver3.bean.Singer;
import com.nwu.ypf.mp_ver3.bean.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MusicRepo {

    private static final String TAG = MusicRepo.class.getSimpleName();

    private static MusicRepo INSTANCE;

    public static MusicRepo getInstance() {
        if (INSTANCE == null) {
            synchronized (MusicRepo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MusicRepo();
                }
            }
        }
        return INSTANCE;
    }

    private MusicRepo() {
    }
// 扫描回调
    public interface ScanLocalMusicCallback {
        @WorkerThread
        void onFinish(@Nullable List<IMusicInfo> music);
    }

    private static String[] musicProjection = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE};

    /**
     * 异步将音乐按歌手聚类。
     */
    @MainThread
    public LiveData<Source<List<Singer>>> groupMusicWithSinger(@NonNull final List<IMusicInfo> music) {
        final MutableLiveData<Source<List<Singer>>> resultLiveData =
                new MutableLiveData<>(Source.<List<Singer>>loading());
        final long s = System.currentTimeMillis();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                List<Singer> result = groupWithSinger(music);
                Log.d(TAG, String.format(Locale.getDefault(), "按歌手聚类消耗%dms，共%d个歌手。",
                        (System.currentTimeMillis() - s), result.size()));
                resultLiveData.postValue(Source.success(result));
            }
        };
        new Thread(run).start();
        return resultLiveData;
    }

    /**
     * 异步将音乐按专辑聚类。
     */
    @MainThread
    public LiveData<Source<List<Album>>> groupMusicWithAlbum(@NonNull final List<IMusicInfo> music) {
        final MutableLiveData<Source<List<Album>>> resultLiveData =
                new MutableLiveData<>(Source.<List<Album>>loading());
        final long s = System.currentTimeMillis();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                List<Album> result = groupWithAlbum(music);
                Log.d(TAG, String.format(Locale.getDefault(), "按专辑聚类消耗%dms，共%d个专辑。",
                        (System.currentTimeMillis() - s), result.size()));
                resultLiveData.postValue(Source.success(result));
            }
        };
        new Thread(run).start();
        return resultLiveData;
    }

    @NonNull
    private List<Singer> groupWithSinger(@NonNull List<IMusicInfo> music) {
        Map<Long, Singer> singers = new HashMap<>();
        for (IMusicInfo item : music) {
            long id = item.getArtistId();
            if (singers.containsKey(id)) {
                Objects.requireNonNull(singers.get(id)).getMusic().add(item);
            } else {
                Singer tmp = new Singer(item);
                singers.put(item.getArtistId(), tmp);
            }
        }
        // Map 转 List
        List<Singer> result = new ArrayList<>();
        for (Map.Entry<Long, Singer> entry : singers.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    @NonNull
    private List<Album> groupWithAlbum(@NonNull List<IMusicInfo> music) {
        Map<Integer, Album> albums = new HashMap<>();
        for (IMusicInfo item : music) {
            int id = item.getAlbumId();
            if (albums.containsKey(id)) {
                Objects.requireNonNull(albums.get(id)).getMusic().add(item);
            } else {
                Album tmp = new Album(item);
                albums.put(item.getAlbumId(), tmp);
            }
        }
        // Map 转 List
        List<Album> result = new ArrayList<>();
        for (Map.Entry<Integer, Album> entry : albums.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }


    /**
     * 异步查询本地音乐。
     *
     * @param minSize     最低音乐文件大小。
     * @param minDuration 最低音乐时长。
     * @param query       歌曲模糊查询。null 标识查找所有歌曲。
     * @param callback    查询回调。
     */
    @MainThread
    public void scanLocalMusic(final Context context, final Long minSize, final Long minDuration,
                               @Nullable final String query, final ScanLocalMusicCallback callback) {
        final long s = System.currentTimeMillis();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                List<IMusicInfo> result = scanLocalMusicInternal(context, minSize, minDuration,
                        query);
                Log.d(TAG, String.format(Locale.getDefault(), "查询音乐消耗%dms，共%d首。",
                        (System.currentTimeMillis() - s), result == null ? 0 : result.size()));
                callback.onFinish(result);
            }
        };
        new Thread(run).start();
    }

    /**
     * @param query 歌曲模糊查询。null 标识查找所有歌曲。
     */
    @Nullable
    private List<IMusicInfo> scanLocalMusicInternal(Context context, Long minSize,
                                                    Long minDuration, @Nullable String query) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + " = ? AND " +
                MediaStore.Audio.AudioColumns.TITLE + " != ? AND " +
                MediaStore.Audio.AudioColumns.SIZE + " > ? AND " +
                MediaStore.Audio.Media.DURATION + " > ?";
        List<String> args = new ArrayList<>();
        args.add("1");
        args.add("");
        args.add(String.valueOf(minSize));
        args.add(String.valueOf(minDuration));
        if (!TextUtils.isEmpty(query)) {
            selection = selection + " AND ( " +
                    MediaStore.Audio.AudioColumns.TITLE + " LIKE ?  OR " +
                    MediaStore.Audio.AudioColumns.ARTIST + " LIKE ? OR " +
                    MediaStore.Audio.AudioColumns.ALBUM + " LIKE ? ) ";
            args.add("%" + query + "%");
            args.add("%" + query + "%");
            args.add("%" + query + "%");
        }

        String[] tmp = new String[args.size()];
        args.toArray(tmp);
        Cursor cursor = cr.query(uri, musicProjection, selection, tmp, null);
        return parseMusicList(context, cursor);
    }

    private @Nullable
    ArrayList<IMusicInfo> parseMusicList(@NonNull Context context, @Nullable Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<IMusicInfo> musicList = new ArrayList<>();

        int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int artistIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
        int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
        int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int sizeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);

        while (cursor.moveToNext()) {
            String musicName = cursor.getString(titleIndex);
            int id = cursor.getInt(idIndex);
            Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

            IMusicInfo music = new MusicInfo(musicName, uri);
            music.setId(id);
            music.setArtist(cursor.getString(artistIndex));
            music.setArtistId(cursor.getLong(artistIdIndex));
            music.setAlbumId(cursor.getInt(albumIdIndex));
            music.setAlbumName(cursor.getString(albumIndex));
            music.setDuration(cursor.getInt(durationIndex));
            music.setFile(cursor.getString(dataIndex));
            music.setSize(cursor.getInt(sizeIndex));
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    /**
     * 通过艺术家 id 查询封面图。
     *
     * @param albumId 艺术家id，
     * @return 封面图路径。
     */
    public static String getAlbumArt(ContentResolver cr, long albumId) {
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Audio.Albums.ALBUM_ART};
        Cursor cursor = cr.query(uri, projection, MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)}, null);
        String albumArt = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                albumArt = cursor.getString(0);
            }
            cursor.close();
        }
        return albumArt;
    }

}
