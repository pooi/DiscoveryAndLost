package ga.discoveryandlost.discoveryandlost.obj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;


/**
 * Created by ewon on 2017-12-24.
 */

public class User implements Serializable {
    String id, studentId, email, fn, ln, phone;
    Long registeredDate;

    public User(){

    }

    public User(String data){
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
            if (keySet.contains("student_id")) {
                studentId = (String) temp.get("student_id");
            }
            if (keySet.contains("email")) {
                email = (String) temp.get("email");
            }
            if (keySet.contains("first_name")) {
                fn = (String) temp.get("first_name");
            }
            if (keySet.contains("last_name")) {
                ln = (String) temp.get("last_name");
            }
            if (keySet.contains("phone")) {
                phone = (String) temp.get("phone");
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void convertRgt(JSONObject temp){
        ArrayList<String> keySet = AdditionalFunc.getKeySet(temp.keys());

        try {

            if (keySet.contains("rgt_id")) {
                id = (String) temp.get("rgt_id");
            }
            if (keySet.contains("rgt_student_id")) {
                studentId = (String) temp.get("rgt_student_id");
            }
            if (keySet.contains("rgt_email")) {
                email = (String) temp.get("rgt_email");
            }
            if (keySet.contains("rgt_first_name")) {
                fn = (String) temp.get("rgt_first_name");
            }
            if (keySet.contains("rgt_last_name")) {
                ln = (String) temp.get("rgt_last_name");
            }
            if (keySet.contains("rgt_phone")) {
                phone = (String) temp.get("rgt_phone");
            }
            if (keySet.contains("rgt_registered_date")) {
                String date = (String) temp.get("rgt_registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
//                registeredDate = Long.parseLong((String) temp.get("rgt_registered_date"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void convertRcv(JSONObject temp){
        ArrayList<String> keySet = AdditionalFunc.getKeySet(temp.keys());

        try {

            if (keySet.contains("rcv_id")) {
                id = (String) temp.get("rcv_id");
            }
            if (keySet.contains("rcv_student_id")) {
                studentId = (String) temp.get("rcv_student_id");
            }
            if (keySet.contains("rcv_email")) {
                email = (String) temp.get("rcv_email");
            }
            if (keySet.contains("rcv_first_name")) {
                fn = (String) temp.get("rcv_first_name");
            }
            if (keySet.contains("rcv_last_name")) {
                ln = (String) temp.get("rcv_last_name");
            }
            if (keySet.contains("rcv_phone")) {
                phone = (String) temp.get("rcv_phone");
            }
            if (keySet.contains("rcv_registered_date")) {
                String date = (String) temp.get("rcv_registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
//                registeredDate = Long.parseLong((String) temp.get("rcv_registered_date"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void convertBuy(JSONObject temp){
        ArrayList<String> keySet = AdditionalFunc.getKeySet(temp.keys());

        try {

            if (keySet.contains("buy_id")) {
                id = (String) temp.get("buy_id");
            }
            if (keySet.contains("buy_student_id")) {
                studentId = (String) temp.get("buy_student_id");
            }
            if (keySet.contains("buy_email")) {
                email = (String) temp.get("buy_email");
            }
            if (keySet.contains("buy_first_name")) {
                fn = (String) temp.get("buy_first_name");
            }
            if (keySet.contains("buy_last_name")) {
                ln = (String) temp.get("buy_last_name");
            }
            if (keySet.contains("buy_phone")) {
                phone = (String) temp.get("buy_phone");
            }
            if (keySet.contains("buy_registered_date")) {
                String date = (String) temp.get("buy_registered_date");
                if(date == null || date.isEmpty()){
                    registeredDate = null;
                }else {
                    registeredDate = Long.parseLong(date);
                }
//                registeredDate = Long.parseLong((String) temp.get("buy_registered_date"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<User> getUserList(String data){

        ArrayList<User> list = new ArrayList<>();

        try {
            JSONObject jObject = new JSONObject(data);
            JSONArray results = jObject.getJSONArray("result");
            String countTemp = (String)jObject.get("num_result");
            int count = Integer.parseInt(countTemp);

            for ( int i = 0; i < count; ++i ) {
                JSONObject temp = results.getJSONObject(i);

                User user = new User();
                user.convert(temp);

                list.add(user);

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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getName(){
        return ln+fn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Long registeredDate) {
        this.registeredDate = registeredDate;
    }
}
