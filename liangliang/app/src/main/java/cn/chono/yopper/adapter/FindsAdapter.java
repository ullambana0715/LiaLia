package cn.chono.yopper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cc.carousel.ConvenientBanner;
import cc.carousel.holder.ViewHolderCreator;
import cc.carousel.listener.OnItemClickListener;
import cc.carousel.transformer.NonPageTransformer;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DiscoverInfos.BannersEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.CampaignsEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindAstrologysEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindLuckEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindStoresEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.FindTarotEntity;
import cn.chono.yopper.Service.Http.DiscoverInfos.TodayArticles;
import cn.chono.yopper.data.FindsAdapterData;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.convenientbanner.FindBannerHoderView;

/**
 * Created by cc on 16/6/12.
 */
public class FindsAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {


    private static int Banner = 0;

    private static int Formulated = 1;

    private static int Tarot = 2;

    private static int Recommend = 3;

    BannersEntity banners;


    List<FindsAdapterData> initAdapterData = new ArrayList<>();


    Context mContext;


    public FindsAdapter(Context context) {
        mContext = context;
        RxBus.get().register(this);
    }

    public void setInitAdapterData(List<FindsAdapterData> initAdapterData) {
        this.initAdapterData = initAdapterData;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {


        if (Banner == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_header_layout, parent, false);

            return new BannerViewHolder(view);
        }

        if (Formulated == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_formulated, parent, false);

            return new FormulatedViewHolder(view);

        }

        if (Tarot == viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finds_tarotastrologys, parent, false);

            return new TarotViewHolder(view);

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_recommend, parent, false);

        return new RecommendViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, boolean isItem) {

        LogUtils.e("onBindViewHolder");

        if (holder instanceof BannerViewHolder) {


            (((BannerViewHolder) holder).findHeaderLayout_cb).setPages(new ViewHolderCreator<FindBannerHoderView>() {
                @Override
                public FindBannerHoderView createHolder() {
                    return new FindBannerHoderView();
                }
            }, initAdapterData.get(position).campaigns);


            (((BannerViewHolder) holder).findHeaderLayout_cb).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {

                    ViewsUtils.preventViewMultipleClick( (((BannerViewHolder) holder).findHeaderLayout_cb),1000);


                    CampaignsEntity campaignsEntity = initAdapterData.get(position).campaigns.get(pos);


                    String type = campaignsEntity.type;

                    MobclickAgent.onEvent(mContext, "btn_find_event_banner");


                    if (!TextUtils.isEmpty(type) && TextUtils.equals("ActivityDetail", type)) {

                        RxBus.get().post("ActivityDetail", campaignsEntity);


                    } else if (!TextUtils.isEmpty(type) && TextUtils.equals("Default", type)) {

                        RxBus.get().post("Default", campaignsEntity);


                    } else if (!TextUtils.isEmpty(type) && TextUtils.equals("Article", type)) {

                        RxBus.get().post("Article", campaignsEntity);

                    } else {

                        RxBus.get().post("campaigns_dialog", campaignsEntity);
                    }


                }
            });


            return;
        }


        if (holder instanceof FormulatedViewHolder) {


            ((FormulatedViewHolder) holder).findsFormulatedAdapter.setSubBanners(initAdapterData.get(position).banners);


            return;
        }

        if (holder instanceof TarotViewHolder) {


            ((TarotViewHolder) holder).itemFindsTarot_tv_tarot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RxBus.get().post("Tarot", "");
                    return;

                }
            });


            ((TarotViewHolder) holder).itemFindsTarot_tv_astrologys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    RxBus.get().post("Astrologys", "");
                    return;

                }
            });


            FindTarotEntity findTarotEntity = initAdapterData.get(position).findTarotAstrologysEntity.findTarotEntity;


            if (findTarotEntity != null) {

                ((TarotViewHolder) holder).itemFindsTarot_tv_name.setText(findTarotEntity.name);

                ((TarotViewHolder) holder).itemFindsTarot_tv_content.setText(findTarotEntity.description);

                ((TarotViewHolder) holder).itemFindsTarot_iv_icon.setImageResource(findTarotEntity.iconUrl);

//                Glide.with(mContext).load(findTarotEntity.iconUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(((TarotViewHolder) holder).itemFindsTarot_iv_icon);


                ((TarotViewHolder) holder).itemFindsTarot_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RxBus.get().post("TarotList", "");
                        return;
                    }
                });
                ((TarotViewHolder) holder).itemFindsTarot_iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.get().post("TarotList", "");
                        return;
                    }
                });


            }

            FindAstrologysEntity findAstrologysEntity = initAdapterData.get(position).findTarotAstrologysEntity.findAstrologysEntity;


            if (findAstrologysEntity != null) {

                ((TarotViewHolder) holder).itemFindsAstrologys_tv_name.setText(findAstrologysEntity.name);

                ((TarotViewHolder) holder).itemFindsAstrologys_tv_content.setText(findAstrologysEntity.description);

                ((TarotViewHolder) holder).itemFindsAstrologys_iv_icon.setImageResource(findAstrologysEntity.iconUrl);

//                Glide.with(mContext).load(findAstrologysEntity.iconUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(((TarotViewHolder) holder).itemFindsAstrologys_iv_icon);

                ((TarotViewHolder) holder).itemFindsAstrologys_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RxBus.get().post("AstrologysList", "");
                        return;
                    }
                });

                ((TarotViewHolder) holder).itemFindsAstrologys_iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.get().post("AstrologysList", "");
                        return;
                    }
                });

            }


            FindLuckEntity findLuckEntity = initAdapterData.get(position).findTarotAstrologysEntity.findLuckEntity;


            if (findLuckEntity != null) {

                ((TarotViewHolder) holder).itemFindsLuck_tv_name.setText(findLuckEntity.name);

                ((TarotViewHolder) holder).itemFindsLuck_tv_content.setText(findLuckEntity.description);

                ((TarotViewHolder) holder).itemFindsLuck_iv_icon.setImageResource(findLuckEntity.iconUrl);

//                Glide.with(mContext).load(findLuckEntity.iconUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(((TarotViewHolder) holder).itemFindsLuck_iv_icon);


                ((TarotViewHolder) holder).itemFindsLuck_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        RxBus.get().post("Luck", "");
                        return;

                    }
                });
                ((TarotViewHolder) holder).itemFindsLuck_iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        RxBus.get().post("Luck", "");
                        return;

                    }
                });

            }


            FindStoresEntity findStoresEntity = initAdapterData.get(position).findTarotAstrologysEntity.findStoresEntity;


            if (findStoresEntity != null) {

                ((TarotViewHolder) holder).itemFindsStores_tv_name.setText(findStoresEntity.name);

                ((TarotViewHolder) holder).itemFindsStores_tv_content.setText(findStoresEntity.description);

                ((TarotViewHolder) holder).itemFindsStores_iv_icon.setImageResource(findStoresEntity.iconUrl);

//                Glide.with(mContext).load(findStoresEntity.iconUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(((TarotViewHolder) holder).itemFindsStores_iv_icon);


                ((TarotViewHolder) holder).itemFindsStores_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RxBus.get().post("TarotShop", "");

                        return;

                    }
                });
                ((TarotViewHolder) holder).itemFindsStores_iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RxBus.get().post("TarotShop", "");

                        return;

                    }
                });

            }


            return;
        }


        if (holder instanceof RecommendViewHolder) {


            ((RecommendViewHolder) holder).itemFindsRecommend_rl_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RxBus.get().post("ArticleList", banners);

                }
            });


            for (int i = 0; i < initAdapterData.get(position).todayArticles.size(); i++) {

                TodayArticles todayArticles = initAdapterData.get(position).todayArticles.get(i);

                if (todayArticles == null) {
                    return;
                }


                View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_recommend_content, null);


                TextView itemFindsRecommend_tv_title = (TextView) view.findViewById(R.id.itemFindsRecommend_tv_title);

                LinearLayout itemFindsRecommend_ll_img = (LinearLayout) view.findViewById(R.id.itemFindsRecommend_ll_img);

                TextView itemFindsRecommend_tv_watch_number = (TextView) view.findViewById(R.id.itemFindsRecommend_tv_watch_number);

                itemFindsRecommend_tv_title.setMaxLines(2);

                itemFindsRecommend_tv_title.setEllipsize(TextUtils.TruncateAt.END);

                itemFindsRecommend_tv_title.setText(todayArticles.title == null ? "" : todayArticles.title);

                itemFindsRecommend_tv_watch_number.setText(todayArticles.readCount + "");

                if (todayArticles.imageUrls != null && todayArticles.imageUrls.size() > 0) {


                    for (int j = 0; j < todayArticles.imageUrls.size(); j++) {


                        if (TextUtils.isEmpty(todayArticles.imageUrls.get(j))) {

                            continue;
                        }

                        final int v_w = UnitUtil.getScreenWidthPixels(mContext) - (UnitUtil.dip2px(20, mContext));


                        final ImageView imageView = new ImageView(mContext);


                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v_w, (v_w * 3 / 5));

                        imageView.setLayoutParams(params);

                        String imgurl= ImgUtils.DealImageUrl(todayArticles.imageUrls.get(j),640,384);
                        Glide.with(mContext).load(imgurl).into(imageView);

                        itemFindsRecommend_ll_img.addView(imageView);


                        break;


                    }


                }




                view.setTag(todayArticles.id);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Id = (String) v.getTag();


                        RxBus.get().post("RecommendDetail", Id);


                        return;
                    }
                });

                ((RecommendViewHolder) holder).itemFindsRecommend_ll_content.addView(view);


            }


            return;
        }

    }

    @Override
    public int getAdapterItemCount() {


        return initAdapterData == null ? 0 : initAdapterData.size();
    }


    @Override
    public int getAdapterItemViewType(int position) {


        if (initAdapterData != null && initAdapterData.size() > 0) {

            if (Banner == initAdapterData.get(position).type)
                return Banner;
            else if (Formulated == initAdapterData.get(position).type)
                return Formulated;
            else if (Tarot == initAdapterData.get(position).type)
                return Tarot;
            else if (Recommend == initAdapterData.get(position).type)
                return Recommend;
        }


        return super.getAdapterItemViewType(position);
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder {

        ConvenientBanner findHeaderLayout_cb;

        public BannerViewHolder(View itemView) {
            super(itemView);

            findHeaderLayout_cb = (ConvenientBanner) itemView.findViewById(R.id.findHeaderLayout_cb);

            findHeaderLayout_cb.getViewPager().setPageMargin(18);

            findHeaderLayout_cb.getViewPager().setOffscreenPageLimit(3);

            findHeaderLayout_cb.startTurning(6000);

            findHeaderLayout_cb.setPageTransformer(new NonPageTransformer());

        }
    }

    public class FormulatedViewHolder extends RecyclerView.ViewHolder {

        GridView itemFind_gv_formulated;

        FindsFormulatedAdapter findsFormulatedAdapter;

        public FormulatedViewHolder(View itemView) {

            super(itemView);


            itemFind_gv_formulated = (GridView) itemView.findViewById(R.id.itemFind_gv_formulated);

            findsFormulatedAdapter = new FindsFormulatedAdapter(mContext);

            itemFind_gv_formulated.setAdapter(findsFormulatedAdapter);

        }


    }


    public class TarotViewHolder extends RecyclerView.ViewHolder {

        TextView itemFindsTarot_tv_name;
        TextView itemFindsTarot_tv_content;
        ImageView itemFindsTarot_iv_icon;

        TextView itemFindsTarot_tv_tarot;
        TextView itemFindsTarot_tv_astrologys;

        TextView itemFindsLuck_tv_name;
        TextView itemFindsLuck_tv_content;
        ImageView itemFindsLuck_iv_icon;

        TextView itemFindsAstrologys_tv_name;

        TextView itemFindsAstrologys_tv_content;

        ImageView itemFindsAstrologys_iv_icon;


        TextView itemFindsStores_tv_name;
        TextView itemFindsStores_tv_content;
        ImageView itemFindsStores_iv_icon;


        LinearLayout itemFindsTarot_ll;

        LinearLayout itemFindsLuck_ll;
        LinearLayout itemFindsAstrologys_ll;
        LinearLayout itemFindsStores_ll;


        public TarotViewHolder(View itemView) {
            super(itemView);

            itemFindsTarot_tv_name = (TextView) itemView.findViewById(R.id.itemFindsTarot_tv_name);
            itemFindsTarot_tv_content = (TextView) itemView.findViewById(R.id.itemFindsTarot_tv_content);
            itemFindsTarot_iv_icon = (ImageView) itemView.findViewById(R.id.itemFindsTarot_iv_icon);


            itemFindsTarot_tv_tarot = (TextView) itemView.findViewById(R.id.itemFindsTarot_tv_tarot);
            itemFindsTarot_tv_astrologys = (TextView) itemView.findViewById(R.id.itemFindsTarot_tv_astrologys);


            itemFindsLuck_tv_name = (TextView) itemView.findViewById(R.id.itemFindsLuck_tv_name);
            itemFindsLuck_tv_content = (TextView) itemView.findViewById(R.id.itemFindsLuck_tv_content);
            itemFindsLuck_iv_icon = (ImageView) itemView.findViewById(R.id.itemFindsLuck_iv_icon);


            itemFindsAstrologys_tv_name = (TextView) itemView.findViewById(R.id.itemFindsAstrologys_tv_name);
            itemFindsAstrologys_tv_content = (TextView) itemView.findViewById(R.id.itemFindsAstrologys_tv_content);
            itemFindsAstrologys_iv_icon = (ImageView) itemView.findViewById(R.id.itemFindsAstrologys_iv_icon);


            itemFindsStores_tv_name = (TextView) itemView.findViewById(R.id.itemFindsStores_tv_name);
            itemFindsStores_tv_content = (TextView) itemView.findViewById(R.id.itemFindsStores_tv_content);
            itemFindsStores_iv_icon = (ImageView) itemView.findViewById(R.id.itemFindsStores_iv_icon);


            itemFindsTarot_ll = (LinearLayout) itemView.findViewById(R.id.itemFindsTarot_ll);

            itemFindsLuck_ll = (LinearLayout) itemView.findViewById(R.id.itemFindsLuck_ll);

            itemFindsAstrologys_ll = (LinearLayout) itemView.findViewById(R.id.itemFindsAstrologys_ll);

            itemFindsStores_ll = (LinearLayout) itemView.findViewById(R.id.itemFindsStores_ll);

        }
    }


    public class RecommendViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemFindsRecommend_rl_title;

        LinearLayout itemFindsRecommend_ll_content;


        public RecommendViewHolder(View itemView) {
            super(itemView);

            itemFindsRecommend_rl_title = (LinearLayout) itemView.findViewById(R.id.itemFindsRecommend_ll_title);
            itemFindsRecommend_ll_content = (LinearLayout) itemView.findViewById(R.id.itemFindsRecommend_ll_content);


        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("bannersEvent")

            }

    )
    public void bannersEvent(BannersEntity banners) {

        this.banners = banners;

    }

    public void unregister() {
        RxBus.get().unregister(this);
    }


}
