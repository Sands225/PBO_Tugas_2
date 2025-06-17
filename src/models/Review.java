package models;

public class Review {
    public int booking;
    public int star;
    public String title;
    public String content;

    public int getBooking() { return booking; }
    public void setBooking(int booking) { this.booking = booking; }

    public int getStar() { return star; }
    public void setStar(int star) { this.star = star; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
