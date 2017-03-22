package kr.rrcoporation.rrfestival.festival.model;

public class FestivalData {

    private FestivalDataHeader header;
    private FestivalDataBody body;

    public FestivalDataHeader getHeader() {
        return header;
    }

    public void setHeader(FestivalDataHeader header) {
        this.header = header;
    }

    public FestivalDataBody getBody() {
        return body;
    }

    public void setBody(FestivalDataBody body) {
        this.body = body;
    }
}
