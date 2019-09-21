package com.recycle.laundrymanagement;

import java.util.HashMap;
import java.util.Map;

public class WashingMachine {
    private int drawable_id;
    private String drawable_label;
    private int washing_machine_id;
    private String user_email;
    private int status_id;
    private long start_time;
    private long end_time;

    public WashingMachine() {

    }

    public WashingMachine(String drawable_label, int drawable_id) {
        this.drawable_id = drawable_id;
        this.drawable_label = drawable_label;
    }
    public int getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }

    public String getDrawable_label() {
        return drawable_label;
    }

    public void setDrawable_label(String drawable_label) {
        this.drawable_label = drawable_label;
    }

    public int getWashing_machine_id() {
        return washing_machine_id;
    }

    public void setWashing_machine_id(int washing_machine_id) {
        this.washing_machine_id = washing_machine_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("drawable_id", drawable_id);
        result.put("drawable_label", drawable_label);
        result.put("washing_machine_id", washing_machine_id);
        result.put("user_email", user_email);
        result.put("status_id", status_id);
        result.put("start_time", start_time);
        result.put("end_time", end_time);
        return result;
    }

}
