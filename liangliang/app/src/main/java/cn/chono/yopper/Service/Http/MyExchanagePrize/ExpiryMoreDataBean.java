package cn.chono.yopper.Service.Http.MyExchanagePrize;

import cn.chono.yopper.Service.Http.ParameterBean;

public class ExpiryMoreDataBean
  extends ParameterBean
{
  private String nextQuery;
  
  public String getNextQuery()
  {
    return this.nextQuery;
  }
  
  public void setNextQuery(String paramString)
  {
    this.nextQuery = paramString;
  }
}
