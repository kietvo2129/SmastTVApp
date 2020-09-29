package autonsi.smasttvapp;

import java.util.ArrayList;

public class NoticeMaster {
    String id,code,name,content,waiting_time,line_no,line_name,
    product_code,product_name,work_date;
    ArrayList<String> image;

    public NoticeMaster(String id, String code, String name, String content, String waiting_time, String line_no, String line_name, String product_code, String product_name, String work_date, ArrayList<String> image) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.content = content;
        this.waiting_time = waiting_time;
        this.line_no = line_no;
        this.line_name = line_name;
        this.product_code = product_code;
        this.product_name = product_name;
        this.work_date = work_date;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(String waiting_time) {
        this.waiting_time = waiting_time;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }
}
