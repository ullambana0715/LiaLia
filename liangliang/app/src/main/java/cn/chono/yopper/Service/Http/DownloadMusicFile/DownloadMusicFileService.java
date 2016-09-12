package cn.chono.yopper.Service.Http.DownloadMusicFile;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpListener;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/23.
 */
public class DownloadMusicFileService extends HttpService {
    public DownloadMusicFileService(Context context) {
        super(context);
    }

    private DownloadMusicFileBean fileBean;

    private OKHttpListener mokHttpListener;

    @Override
    public void enqueue() {
        String url=fileBean.getUrl();
        callWrap= OKHttpUtils.download(context, url, mokHttpListener);

    }

    public void setOKHttpListener( OKHttpListener okHttpListener){
        this.mokHttpListener=okHttpListener;

    }

    @Override
    public void parameter(ParameterBean iBean) {
        fileBean= (DownloadMusicFileBean) iBean;
    }
}
