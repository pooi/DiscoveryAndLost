package ga.discoveryandlost.discoveryandlost.obj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;

/**
 * Created by tw on 2017. 12. 19..
 */

public class DalLost implements Serializable {

    String id, category, color, brand, building, room, tags, description, photos, rgtUser, rcvUser;

    Double match;

    Long registeredDate, receivedDate;

    public DalLost(){
        match = 0.0;
    }

    public DalLost(String data){
        this();
        build(data);
    }

    public boolean isEmpty(){
        if(id == null){
            return true;
        }
        return "".equals(id);
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

            if (keySet.contains("id")) {
                id = (String) temp.get("id");
            }
            if (keySet.contains("category")) {
                category = (String) temp.get("category");
            }
            if (keySet.contains("color")) {
                color = (String) temp.get("color");
            }
            if (keySet.contains("brand")) {
                brand = (String) temp.get("brand");
            }
            if (keySet.contains("building")) {
                building = (String) temp.get("building");
            }
            if (keySet.contains("room")) {
                room = (String) temp.get("room");
            }
            if (keySet.contains("tags")) {
                tags = (String) temp.get("tags");
            }
            if (keySet.contains("description")) {
                description = (String) temp.get("description");
            }
            if (keySet.contains("photos")) {
                photos = (String) temp.get("photos");
            }
            if (keySet.contains("rgt_user")) {
                rgtUser = (String) temp.get("rgt_user");
            }
            if (keySet.contains("rcv_user")) {
                rcvUser = (String) temp.get("rcv_user");
            }
            if (keySet.contains("registered_date")) {
                registeredDate = Long.parseLong((String) temp.get("registered_date"));
            }
            if (keySet.contains("received_date")) {
                registeredDate = Long.parseLong((String) temp.get("received_date"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<DalLost> getLostList(String data){

        ArrayList<DalLost> list = new ArrayList<>();

        try {
            JSONObject jObject = new JSONObject(data);
            JSONArray results = jObject.getJSONArray("result");
            String countTemp = (String)jObject.get("num_result");
            int count = Integer.parseInt(countTemp);

            for ( int i = 0; i < count; ++i ) {
                JSONObject temp = results.getJSONObject(i);

                DalLost lost = new DalLost();
                lost.convert(temp);

                list.add(lost);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return list;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getRgtUser() {
        return rgtUser;
    }

    public void setRgtUser(String rgtUser) {
        this.rgtUser = rgtUser;
    }

    public String getRcvUser() {
        return rcvUser;
    }

    public void setRcvUser(String rcvUser) {
        this.rcvUser = rcvUser;
    }

    public Long getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Long registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Double getMatch() {
        return match;
    }

    public void setMatch(Double match) {
        this.match = match;
    }
}
