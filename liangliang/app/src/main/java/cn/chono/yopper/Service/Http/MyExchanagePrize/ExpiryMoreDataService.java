package cn.chono.yopper.Service.Http.MyExchanagePrize;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;


public class ExpiryMoreDataService extends HttpService
{
  private ExpiryMoreDataBean dataBean;

  public ExpiryMoreDataService(Context paramContext)
  {
    super(paramContext);
  }

  public void enqueue()
  {
    this.OutDataClass = ExpiryDataRespBean.class;
    String str = this.dataBean.getNextQuery();
    this.callWrap = OKHttpUtils.get(this.context, str, this.okHttpListener);
  }

  public void parameter(ParameterBean paramParameterBean)
  {
    this.dataBean = ((ExpiryMoreDataBean)paramParameterBean);
  }
}
