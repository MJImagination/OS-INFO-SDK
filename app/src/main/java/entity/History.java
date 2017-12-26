package entity;

import org.litepal.crud.DataSupport;

/**
 * Created by ancun on 2017/12/18.
 */

public class History extends DataSupport{
    private long id;
    private String date;
    private String status;
    private long osInfoID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOsInfoID() {
        return osInfoID;
    }

    public void setOsInfoID(long osInfoID) {
        this.osInfoID = osInfoID;
    }


    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", osInfoID=" + osInfoID +
                '}';
    }
}
