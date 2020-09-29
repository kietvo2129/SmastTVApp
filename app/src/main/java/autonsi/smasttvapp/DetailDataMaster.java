package autonsi.smasttvapp;

public class DetailDataMaster {

    String ldt,work_ymd
    ,work_time,start_time1,start_time,end_time,end_time1,stt;
    int prod_qty,done_qty,refer_qty,defect_qty,efficiency;

    public DetailDataMaster(String ldt, String work_ymd, String work_time, String start_time1, String start_time, String end_time, String end_time1, String stt, int prod_qty, int done_qty, int refer_qty, int defect_qty, int efficiency) {
        this.ldt = ldt;
        this.work_ymd = work_ymd;
        this.work_time = work_time;
        this.start_time1 = start_time1;
        this.start_time = start_time;
        this.end_time = end_time;
        this.end_time1 = end_time1;
        this.stt = stt;
        this.prod_qty = prod_qty;
        this.done_qty = done_qty;
        this.refer_qty = refer_qty;
        this.defect_qty = defect_qty;
        this.efficiency = efficiency;
    }

    public String getLdt() {
        return ldt;
    }

    public void setLdt(String ldt) {
        this.ldt = ldt;
    }

    public String getWork_ymd() {
        return work_ymd;
    }

    public void setWork_ymd(String work_ymd) {
        this.work_ymd = work_ymd;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getStart_time1() {
        return start_time1;
    }

    public void setStart_time1(String start_time1) {
        this.start_time1 = start_time1;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time1() {
        return end_time1;
    }

    public void setEnd_time1(String end_time1) {
        this.end_time1 = end_time1;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public int getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(int prod_qty) {
        this.prod_qty = prod_qty;
    }

    public int getDone_qty() {
        return done_qty;
    }

    public void setDone_qty(int done_qty) {
        this.done_qty = done_qty;
    }

    public int getRefer_qty() {
        return refer_qty;
    }

    public void setRefer_qty(int refer_qty) {
        this.refer_qty = refer_qty;
    }

    public int getDefect_qty() {
        return defect_qty;
    }

    public void setDefect_qty(int defect_qty) {
        this.defect_qty = defect_qty;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }
}
