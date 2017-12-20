package ga.discoveryandlost.discoveryandlost.obj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;

/**
 * Created by tw on 2017. 12. 20..
 */

public class DalSell implements Serializable {

    // lost_id, price, rgt_user, buy_user, registered_date, bought_date
    DalLost lost;
    User rgtUser, buyUser;
    Integer price;
    Long registeredDate, boughtDate;

    public DalSell(){
        lost = new DalLost();
        rgtUser = new User();
        buyUser = new User();
        price = null;
        registeredDate = null;
        boughtDate = null;
    }

    public DalSell(String data){
        this();
        build(data);
    }

    public void build(String data){

        try {
            // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
            JSONObject jObject = new JSONObject(data);
            // results라는 key는 JSON배열로 되어있다.
            JSONArray results = jObject.getJSONArray("result");
            String countTemp = (String)jObject.get("num_result");
            int count = Integer.parseInt(countTemp);

            for ( int i = 0; i < count; ++i ) {
                JSONObject temp = results.getJSONObject(i);
                convert(temp);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void convert(JSONObject temp){
        ArrayList<String> keySet = AdditionalFunc.getKeySet(temp.keys());

        try {

            lost.convertLost(temp);
            rgtUser.convertRgt(temp);
            buyUser.convertBuy(temp);

            if (keySet.contains("price")) {
                price = Integer.parseInt((String) temp.get("price"));
            }
            if (keySet.contains("registered_date")) {
                String date = (String) temp.get("registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
            }
            if (keySet.contains("bought_date")) {
                String date = (String) temp.get("bought_date");
                if(date == null || date.isEmpty()){
                    boughtDate = null;
                }else {
                    boughtDate = Long.parseLong(date);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<DalSell> getSellList(String data){

        ArrayList<DalSell> list = new ArrayList<>();

        try {
            JSONObject jObject = new JSONObject(data);
            JSONArray results = jObject.getJSONArray("result");
            String countTemp = (String)jObject.get("num_result");
            int count = Integer.parseInt(countTemp);

            for ( int i = 0; i < count; ++i ) {
                JSONObject temp = results.getJSONObject(i);

                DalSell sell = new DalSell();
                sell.convert(temp);

                list.add(sell);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }

    public DalLost getLost() {
        return lost;
    }

    public void setLost(DalLost lost) {
        this.lost = lost;
    }

    public User getRgtUser() {
        return rgtUser;
    }

    public void setRgtUser(User rgtUser) {
        this.rgtUser = rgtUser;
    }

    public User getBuyUser() {
        return buyUser;
    }

    public void setBuyUser(User buyUser) {
        this.buyUser = buyUser;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Long registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Long getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Long boughtDate) {
        this.boughtDate = boughtDate;
    }
}
