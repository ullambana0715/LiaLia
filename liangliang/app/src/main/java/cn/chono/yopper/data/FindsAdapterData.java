package cn.chono.yopper.data;

import java.util.List;

import cn.chono.yopper.Service.Http.DiscoverInfos.BannersEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.CampaignsEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindTarotAstrologysEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.TodayArticles;

/**
 * Created by cc on 16/6/22.
 */
public class FindsAdapterData {

    public int type;

    public FindTarotAstrologysEntity findTarotAstrologysEntity;

    public List<BannersEntity> banners;

    public List<CampaignsEntity> campaigns;

    public List<TodayArticles> todayArticles;


}
