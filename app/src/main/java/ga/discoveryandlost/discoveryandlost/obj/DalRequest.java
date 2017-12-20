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

public class DalRequest implements Serializable {

    String id, lostId, itemMatchingId;

    User requestUser;

    Long registeredDate;
    Double matchRatio;

    public DalRequest(){
        requestUser = new User();
        registeredDate = null;
    }

    public DalRequest(String data){
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

            requestUser.convertRgt(temp);

            if (keySet.contains("id")) {
                id = (String) temp.get("id");
            }
            if (keySet.contains("lost_id")) {
                lostId = (String) temp.get("lost_id");
            }
            if (keySet.contains("item_matching_id")) {
                itemMatchingId = (String) temp.get("item_matching_id");
            }
            if (keySet.contains("registered_date")) {
                String date = (String) temp.get("registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
            }
            if (keySet.contains("match_ratio")) {
                String date = (String) temp.get("match_ratio");
                if(date == null || date.isEmpty()){
                    matchRatio = null;
                }else {
                    matchRatio = Double.parseDouble(date);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<DalRequest> getRequestList(String data){

        ArrayList<DalRequest> list = new ArrayList<>();

        try {
            JSONObject jObject = new JSONObject(data);
            JSONArray results = jObject.getJSONArray("result");
            String countTemp = (String)jObject.get("num_result");
            int count = Integer.parseInt(countTemp);

            for ( int i = 0; i < count; ++i ) {
                JSONObject temp = results.getJSONObject(i);

                DalRequest request = new DalRequest();
                request.convert(temp);

                list.add(request);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLostId() {
        return lostId;
    }

    public void setLostId(String lostId) {
        this.lostId = lostId;
    }

    public String getItemMatchingId() {
        return itemMatchingId;
    }

    public void setItemMatchingId(String itemMatchingId) {
        this.itemMatchingId = itemMatchingId;
    }

    public User getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(User requestUser) {
        this.requestUser = requestUser;
    }

    public Long getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Long registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Double getMatchRatio() {
        return matchRatio;
    }

    public void setMatchRatio(Double matchRatio) {
        this.matchRatio = matchRatio;
    }
}
