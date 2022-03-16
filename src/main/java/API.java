import java.net.http.HttpResponse;
import java.net.URLEncoder;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class API {

  private String key;
  private String host;
  private String url;
  private String charset;
  private String optionEndpoint;
  private String region;

  public API(){
    key = "b19ebf861cmshddb2b3acd10bdb3p1df82ajsn9d64cd7646a4";
    host = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/";
    charset = "UTF-8";
    optionEndpoint = "stock/v2/get-options";
    region = "region=US";
  }
  public String getKey(){
    return key;
  }
  public String getHost(){
    return host;
  }
  public String getUrl(){
    return url;
  }
  public String getCharset(){
    return charset;
  }
  public String getOptionEndpoint(){
    return optionEndpoint;
  }
  public String getRegion(){
    return region;
  }

}
