package cn.chono.yopper.Service.Http.MyExchanagePrize;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

public class ExpiryDataService
  extends HttpService
{
  private ExpiryDataBean dataBean;
  
  public ExpiryDataService(Context paramContext)
  {
    super(paramContext);
  }
  
  public void enqueue()
  {
    this.OutDataClass = ExpiryDataRespBean.class;
    HashMap localHashMap = new HashMap();
    localHashMap.put("start", Integer.valueOf(this.dataBean.getStart()));
    this.callWrap = OKHttpUtils.get(this.context, "/api/v2/prizes/exchange?", localHashMap, this.okHttpListener);
  }
  
  public void parameter(ParameterBean paramParameterBean)
  {
    this.dataBean = ((ExpiryDataBean)paramParameterBean);
  }
}
