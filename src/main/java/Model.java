import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.JsonNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * The Model holds all information retrieved from the API response. For now, a Request is passed
 * into the model and then a call to the getResponse method sends this request to the API.
 */
public class Model {
  private Request APIRequest;
  private ArrayList<Option> callList;
  private ArrayList<Option> putList;
  private API api;
  private HttpResponse<JsonNode> rawJSON;
  private TickerList tickerList;

  /**
   * A Request is passed into the model and the model stores data once a response from the API is
   * received.
   *
   * @param userRequest a Request to be sent to the API
   * @throws IllegalArgumentException if the Request object has a bad ticker according to the
   * models internal tickerList
   */
  public Model(Request userRequest) throws Exception,IllegalArgumentException{
    tickerList = new TickerList();
    callList = new ArrayList<>(150);
    putList = new ArrayList<>(150);
    api = new API();
    if (tickerList.contains(userRequest.getTicker())) {
      APIRequest = userRequest;
    } else
        throw new IllegalArgumentException ("Ticker passed in request was not valid!");
    getResponse();

  }
    /**
   * getResponse makes the call to the Yahoo Finance API, using the Request object passed to the
   * constructor. The status is reported and if a good response is received, status code 200 is
   * outputted to the console. The raw JSON data is stored in the rawJSON field of this model.
   *
   * @return a httpResponse<JsonNode> response object from the API
   * @throws Exception if an error is encountered in making a request to the Yahoo Finance API
   */
  public HttpResponse<JsonNode> getResponse() throws Exception{

  HttpResponse<JsonNode> response = Unirest.get(api.getUrl() + api.getOptionEndpoint() + "?" +
          APIRequest.getTicker() + "&" + APIRequest.getEpochTimeStamp() + "&" + api.getRegion())
          .header("x-rapidapi-key", api.getKey())
          .header("x-rapidapi-host", api.getHost())
          .asJson();
  rawJSON = response;

    //Status code 200 means a good response was received
    System.out.println("Status: " + response.getStatus());
    parseResponse();
    return response;

  }

  /**
   * parseResponse wrangles the raw JSON data response from getResponse() and extracts the desirable
   * information from within int. New Option objects are created for every contract found within the
   * root JSON object. These are then stored in the callList and putList as according to OptionType.
   */
  public void parseResponse(){
    //Create parser instance and assign initial JsonObject to root
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jp = new JsonParser();
    JsonObject root = jp.parse(rawJSON.getBody().toString()).getAsJsonObject();
    JsonObject contracts = root.getAsJsonObject("contracts");
    //Extracting the call and put JsonArrays out
    JsonArray callArray = contracts.getAsJsonArray("calls");
    JsonArray putArray = contracts.getAsJsonArray("puts");

    //Now we extract the data we need from the option arrays and make Option objects with them to
    //store in the callList and putList
    for (JsonElement option: callArray){
      JsonObject call = option.getAsJsonObject();
      BigDecimal strike = call.get("strike").getAsJsonObject().get("raw").getAsBigDecimal();
      BigDecimal premium = call.get("ask").getAsJsonObject().get("raw").getAsBigDecimal();
      String name = call.get("contractSymbol").getAsString();
      BigDecimal impliedVol = call.get("impliedVolatility").getAsJsonObject().get("raw").getAsBigDecimal();

      Option newCall = new Option(strike,premium,APIRequest.getThirdFriday(),OptionType.CALL,name,impliedVol);
      callList.add(newCall);
    }
    for (JsonElement option: putArray){
      JsonObject put = option.getAsJsonObject();
      BigDecimal strike = put.get("strike").getAsJsonObject().get("raw").getAsBigDecimal();
      BigDecimal premium = put.get("ask").getAsJsonObject().get("raw").getAsBigDecimal();
      String name = put.get("contractSymbol").getAsString();
      BigDecimal impliedVol = put.get("impliedVolatility").getAsJsonObject().get("raw").getAsBigDecimal();

      Option newCall = new Option(strike,premium,APIRequest.getThirdFriday(),OptionType.PUT,name,impliedVol);
      putList.add(newCall);
    }
  }
  public HttpResponse<JsonNode> getRawJSON(){
    return rawJSON;
  }

  /**
   * printOptions displays the retrieved information neatly, and in a human-readable way.
   */
  public void printOptions() {
    int callSum = 0;
    int putSum = 0;
    System.out.println("Call Options:\n");
    for (Option call : callList) {
      System.out.println("Call Symbol:" + call.getOptionName());
      System.out.println("Strike :" + call.getStrike());
      System.out.println("Premium :" + call.getPremium());
      System.out.println("Expiration :" + call.getExpiration());
      System.out.println("IV :" + call.getImpliedVol() + "\n\n");
      callSum++;
    }
    System.out.println("Put Options\n");
    for (Option put : putList) {
      System.out.println("Put Symbol:" + put.getOptionName());
      System.out.println("Strike :" + put.getStrike());
      System.out.println("Premium :" + put.getPremium());
      System.out.println("Expiration :" + put.getExpiration());
      System.out.println("IV :" + put.getImpliedVol() + "\n\n");
      putSum++;
    }
    System.out.println("Total Calls Found: " + callSum);
    System.out.println("Total Puts Found: " + putSum);
  }

}
