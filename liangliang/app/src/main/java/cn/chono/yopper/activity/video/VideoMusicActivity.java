package cn.chono.yopper.activity.video;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileBean;
import cn.chono.yopper.Service.Http.DownloadMusicFile.DownloadMusicFileService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.VideoMusicList.VideoMusicListBean;
import cn.chono.yopper.Service.Http.VideoMusicList.VideoMusicListRespBean;
import cn.chono.yopper.Service.Http.VideoMusicList.VideoMusicListService;
import cn.chono.yopper.Service.Http.VideoMusicMoreList.VideoMusicMoreListBean;
import cn.chono.yopper.Service.Http.VideoMusicMoreList.VideoMusicMoreListRespBean;
import cn.chono.yopper.Service.Http.VideoMusicMoreList.VideoMusicMoreListService;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.base.App;
import cn.chono.yopper.adapter.VideoMusicAdapter;
import cn.chono.yopper.data.VideoMusicList;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ThreadPool;
import cn.chono.yopper.utils.video.VideoFileUtils;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * 便多音乐
 *
 * @author zxb
 */

public class VideoMusicActivity extends MainFrameActivity implements VideoMusicAdapter.OnItmeClickListener, MediaPlayer.OnCompletionListener {


    private GridView recyclerView;

    private VideoMusicAdapter adapter;

    private XRefreshView video_xRefreshView;

    private int _start = 0;
    private String _nextQuery;

    private List<VideoMusicList.VideoMusic> vmList;

    private LinearLayout error_network_layout;// 网
    private TextView error_network_tv;

    private boolean isMuiscData = false;

    private MediaPlayer mp;


    private LinearLayout btnGoBack_container;
    private TextView main_frame_tvTitle;

    XRefreshViewHeaders mXRefreshViewHeaders;


    XRefreshViewFooters mXRefreshViewFooters;

    private Dialog loadingDiaog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.video_music_activity);
        initView();
        setXrefreshViewListener();


        loadingDiaog = DialogUtil.LoadingDialog(this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }
        video_xRefreshView.setVisibility(View.GONE);
        error_network_layout.setVisibility(View.GONE);

        _start = 0;
        onRefreshData(_start);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频拍摄添加更多音乐"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频拍摄添加更多音乐"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    @Override
    protected void onDestroy() {

        if (mp.isPlaying()) {
            mp.stop();  //停止播放视频
        }
        mp.release();   //释放资源

        super.onDestroy();

    }

    private void initView() {

        mp = new MediaPlayer();

        btnGoBack_container = (LinearLayout) findViewById(R.id.btnGoBack_container);
        main_frame_tvTitle = (TextView) findViewById(R.id.main_frame_tvTitle);


        main_frame_tvTitle.setText("更多音乐");


        btnGoBack_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);

        video_xRefreshView = (XRefreshView) findViewById(R.id.video_xRefreshView);

        recyclerView = (GridView) findViewById(R.id.gridView);


        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDiaog = DialogUtil.LoadingDialog(VideoMusicActivity.this, null);
                if (!isFinishing()) {
                    loadingDiaog.show();
                }
                video_xRefreshView.setVisibility(View.GONE);
                error_network_layout.setVisibility(View.GONE);

                _start = 0;
                onRefreshData(_start);
            }
        });

        adapter = new VideoMusicAdapter(this);
        adapter.setOnItmeClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    private void setXrefreshViewListener() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        video_xRefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooters = new XRefreshViewFooters(this);



        video_xRefreshView.setCustomFooterView(mXRefreshViewFooters);


        video_xRefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        video_xRefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        video_xRefreshView.setAutoLoadMore(true);


        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        onLoadMoreData(_start, _nextQuery);
                    }
                }, 1000);

            }
        });


        mXRefreshViewFooters.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        onLoadMoreData(_start, _nextQuery);
                    }
                }, 1000);
            }
        });


        video_xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        _start = 0;
                        onRefreshData(_start);


                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {


                        onLoadMoreData(_start, _nextQuery);

                    }
                }, 1000);

            }

        });

    }

    private void onRefreshData(int start) {


        VideoMusicListBean listBean = new VideoMusicListBean();
        listBean.setStart(start);

        VideoMusicListService listService = new VideoMusicListService(this);
        listService.parameter(listBean);
        listService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                VideoMusicListRespBean listRespBean = (VideoMusicListRespBean) respBean;
                VideoMusicList videoMusicList = listRespBean.getResp();
                loadingDiaog.dismiss();

                if (videoMusicList != null) {
                    isMuiscData = true;
                    _nextQuery = videoMusicList.getNextQuery();
                    vmList = new ArrayList<>();
                    vmList = videoMusicList.getList();
                    adapter.setData(vmList);
                    adapter.notifyDataSetChanged();

                    video_xRefreshView.setVisibility(View.VISIBLE);
                    error_network_layout.setVisibility(View.GONE);

                }


                video_xRefreshView.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                if (!isMuiscData) {
                    video_xRefreshView.setVisibility(View.GONE);
                    error_network_layout.setVisibility(View.VISIBLE);
                }

                // 提示连接失败
                DialogUtil.showDisCoverNetToast(VideoMusicActivity.this);

                mXRefreshViewHeaders.onRefreshFail();

                video_xRefreshView.stopRefresh();
            }
        });
        listService.enqueue();


    }

    private void onLoadMoreData(int start, String nextQuery) {


        if (TextUtils.isEmpty(_nextQuery)) {

            mXRefreshViewFooters.setLoadcomplete(true);
            video_xRefreshView.setLoadComplete(false);
            return;
        }


        VideoMusicMoreListBean listBean = new VideoMusicMoreListBean();
        listBean.setNextQuery(nextQuery);

        VideoMusicMoreListService listService = new VideoMusicMoreListService(this);
        listService.parameter(listBean);
        listService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                VideoMusicMoreListRespBean listRespBean = (VideoMusicMoreListRespBean) respBean;
                VideoMusicList videoMusicList = listRespBean.getResp();


                if (videoMusicList != null) {
                    isMuiscData = true;
                    _nextQuery = videoMusicList.getNextQuery();
                    if (vmList == null) {
                        vmList = new ArrayList<VideoMusicList.VideoMusic>();
                    }
                    vmList.addAll(videoMusicList.getList());
                    adapter.setData(vmList);
                    adapter.notifyDataSetChanged();

                }


                if (TextUtils.isEmpty(_nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                    video_xRefreshView.stopLoadMore(false);

                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
                    video_xRefreshView.stopLoadMore();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewFooters.setLoadcomplete(false);
                video_xRefreshView.stopLoadMore(false);


                // 提示连接失败
                DialogUtil.showDisCoverNetToast(VideoMusicActivity.this);


            }
        });
        listService.enqueue();


    }

    @Override
    public void onItmeOnClick(int position) {

        String name = vmList.get(position).getName();
        String musicUrl = vmList.get(position).getMusicUrl();
        int id = vmList.get(position).getId();

        getMusicFile(position, name, musicUrl, id);
    }

    @Override
    public void onItmeBitmapSave(Bitmap bitmap, String name, int id) {

        ThreadPool.getThreadPool().addTask(new BitmapThread(bitmap, name, id));
    }

    @Override
    public void onPlayNativeMusic(String musicPath) {

        if (musicPath == null) {
            return;
        }

        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("musicPath", musicPath);
        message.setData(bundle);
        _MuiscHandler.sendMessage(message);


    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();//释放音频资源
    }


    private class BitmapThread implements Runnable {

        private int id;
        private Bitmap bitmap;
        private String name;

        public BitmapThread(Bitmap bitmap, String name, int id) {
            this.id = id;
            this.bitmap = bitmap;
            this.name = name;
        }

        @Override
        public void run() {

            VideoFileUtils.saveImgFile(bitmap, name, id);


        }

    }


    private void getMusicFile(final int position, final String name, String filePaht, final int id) {


        DownloadMusicFileBean musicFileBean = new DownloadMusicFileBean();
        musicFileBean.setUrl(filePaht);

        DownloadMusicFileService fileService = new DownloadMusicFileService(this);
        fileService.parameter(musicFileBean);
        fileService.setOKHttpListener(new OKHttpListener() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);

                byte[] bytes = (byte[]) result;

                if (bytes != null && bytes.length > 0) {
                    VideoFileUtils.saveVideoMuiacFile(bytes, name, id);
                    if (VideoFileUtils.isVideoMuiacFile(name, id)) {
                        if (null != App.qupaiService) {
                            File file = VideoFileUtils.getVideoMuiacFilePath(name, id);
                            App.qupaiService.addMusic(id, name, "file://" + file.getPath());
                        }
                    }
                }
                vmList.get(position).setIsMuiscFilseDataPath(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object result) {
                super.onFail(result);
                vmList.get(position).setIsMuiscFilseDataPath(true);
                adapter.notifyDataSetChanged();
            }
        });

        fileService.enqueue();


    }

    private Handler _MuiscHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle bundle = msg.getData();
                String musicPath = bundle.getString("musicPath");
                try {
                    //重置MediaPlayer
                    mp.reset();
                    mp.setDataSource(musicPath);
                    //在播放音频资源之前，必须调用Prepare方法完成些准备工作
                    mp.prepare();
                    //开始播放音频
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    };

}
