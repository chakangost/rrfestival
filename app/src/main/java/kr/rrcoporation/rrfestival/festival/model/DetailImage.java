package kr.rrcoporation.rrfestival.festival.model;

/**
 * Created by fimtrus on 2017. 5. 17..
 */

public class DetailImage {

    private Integer contentid;
    private String originimgurl;
    private String serialnum;
    private String smallimageurl;

    public Integer getContentid() {
        return contentid;
    }

    public void setContentid(Integer contentid) {
        this.contentid = contentid;
    }

    public String getOriginimgurl() {
        return originimgurl;
    }

    public void setOriginimgurl(String originimgurl) {
        this.originimgurl = originimgurl;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public String getSmallimageurl() {
        return smallimageurl;
    }

    public void setSmallimageurl(String smallimageurl) {
        this.smallimageurl = smallimageurl;
    }
}
