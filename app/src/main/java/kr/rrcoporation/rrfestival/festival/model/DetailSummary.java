package kr.rrcoporation.rrfestival.festival.model;

/**
 * Created by fimtrus on 2017. 5. 17..
 */

public class DetailSummary {

    private Integer contentid;
    private Integer contenttypeid;
    private Integer fldgubun;
    private String infoname;

    public Integer getContentid() {
        return contentid;
    }

    public void setContentid(Integer contentid) {
        this.contentid = contentid;
    }

    public Integer getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(Integer contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public Integer getFldgubun() {
        return fldgubun;
    }

    public void setFldgubun(Integer fldgubun) {
        this.fldgubun = fldgubun;
    }

    public String getInfoname() {
        return infoname;
    }

    public void setInfoname(String infoname) {
        this.infoname = infoname;
    }

    public String getInfotext() {
        return infotext;
    }

    public void setInfotext(String infotext) {
        this.infotext = infotext;
    }

    public Integer getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(Integer serialnum) {
        this.serialnum = serialnum;
    }

    private String  infotext;
    private Integer serialnum;
}
