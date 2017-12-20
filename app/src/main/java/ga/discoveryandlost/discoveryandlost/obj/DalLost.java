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

    // lost_id, price, rgt_user, buy_user, registered_date, bought_date

    String id, category, color, brand, building, room, tags, description, photos;

    User rgtUser, rcvUser;

    Double match;

    Long registeredDate, receivedDate;

    public DalLost(){
        rgtUser = new User();
        rcvUser = new User();
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

            rgtUser.convertRgt(temp);
            rcvUser.convertRcv(temp);

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
            if (keySet.contains("registered_date")) {
                String date = (String) temp.get("registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
//                registeredDate = Long.parseLong((String) temp.get("registered_date"));
            }
            if (keySet.contains("received_date")) {
                String date = (String) temp.get("received_date");
                if(date == null || date.isEmpty()){
                    receivedDate = null;
                }else {
                    receivedDate = Long.parseLong(date);
                }
//                registeredDate = Long.parseLong((String) temp.get("received_date"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void convertLost(JSONObject temp){
        ArrayList<String> keySet = AdditionalFunc.getKeySet(temp.keys());

        try {

            rgtUser.convertRgt(temp);
            rcvUser.convertRcv(temp);

            if (keySet.contains("lost_id")) {
                id = (String) temp.get("lost_id");
            }
            if (keySet.contains("lost_category")) {
                category = (String) temp.get("lost_category");
            }
            if (keySet.contains("lost_color")) {
                color = (String) temp.get("lost_color");
            }
            if (keySet.contains("lost_brand")) {
                brand = (String) temp.get("lost_brand");
            }
            if (keySet.contains("lost_building")) {
                building = (String) temp.get("lost_building");
            }
            if (keySet.contains("lost_room")) {
                room = (String) temp.get("lost_room");
            }
            if (keySet.contains("lost_tags")) {
                tags = (String) temp.get("lost_tags");
            }
            if (keySet.contains("lost_description")) {
                description = (String) temp.get("lost_description");
            }
            if (keySet.contains("lost_photos")) {
                photos = (String) temp.get("lost_photos");
            }
            if (keySet.contains("lost_registered_date")) {
                String date = (String) temp.get("lost_registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
            }
            if (keySet.contains("lost_received_date")) {
                String date = (String) temp.get("lost_received_date");
                if(date == null || date.isEmpty()){
                    receivedDate = null;
                }else {
                    receivedDate = Long.parseLong(date);
                }
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

    public User getRgtUser() {
        return rgtUser;
    }

    public void setRgtUser(User rgtUser) {
        this.rgtUser = rgtUser;
    }

    public User getRcvUser() {
        return rcvUser;
    }

    public void setRcvUser(User rcvUser) {
        this.rcvUser = rcvUser;
    }
}
