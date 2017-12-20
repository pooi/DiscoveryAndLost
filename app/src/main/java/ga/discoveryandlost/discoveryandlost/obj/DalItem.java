package ga.discoveryandlost.discoveryandlost.obj;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.R;

/**
 * Created by tw on 2017. 12. 19..
 */

public class DalItem implements Serializable {

    String category, color, brand, building, room, tags, photos, description;

    String tempImageName;

    ArrayList<HashMap<String, String>> querys;

    public DalItem(){
        querys = new ArrayList<>();
    }

    public View getQueryView(LayoutInflater inflater, int pos){

        if(inflater != null) {
            View v = inflater.inflate(R.layout.question_and_answer_custom_item, null, false);

            TextView tv_question = (TextView) v.findViewById(R.id.tv_question);
            MaterialEditText edit_answer = (MaterialEditText) v.findViewById(R.id.edit_answer);

            switch (pos) {
                case 0: {
                    tv_question.setText("카테고리는 무엇입니까?");
                    edit_answer.setText(category);
                    edit_answer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            category = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                }
                case 1: {
                    tv_question.setText("무슨 색입니까?");
                    edit_answer.setText(color);
                    edit_answer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            color = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                }
                case 2: {
                    tv_question.setText("브랜드는 무엇입니까?");
                    edit_answer.setText(brand);
                    edit_answer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            brand = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                }
                case 3: {
                    tv_question.setText("어디서 발견했습니까?");
                    edit_answer.setText(building);
                    edit_answer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            building = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                }
                case 4: {
                    tv_question.setText("기타 특징을 입력해주세요.(콤마로 구분)");
                    edit_answer.setText(tags);
                    edit_answer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            tags = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                }


            }
            return v;
        }

        return null;

    }

    public void updateContent(View v, int pos){

        MaterialEditText edit_answer = (MaterialEditText)v.findViewById(R.id.edit_answer);

        switch (pos){
            case 0:{
                category = edit_answer.getText().toString();
                break;
            }
            case 1:{
                color = edit_answer.getText().toString();
                break;
            }
            case 2:{
                brand = edit_answer.getText().toString();
                break;
            }
            case 3:{
                building = edit_answer.getText().toString();
                break;
            }
            case 4:{
                tags = edit_answer.getText().toString();
                break;
            }


        }

    }

    public static int getSize(){
        return 5;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        switch (category){
            case "mouse":
                category = "마우스";
                break;
            case "tumbler":
                category = "텀블러";
                break;
            case "laptop":
                category = "노트북";
                break;
            case "wallet":
                category = "지갑";
                break;
            case "watch":
                category = "손목시계";
                break;
        }
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

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getTempImageName() {
        return tempImageName;
    }

    public void setTempImageName(String tempImageName) {
        this.tempImageName = tempImageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
