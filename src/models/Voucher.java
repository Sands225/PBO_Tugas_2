package models;

public class Voucher {
    private int id;
    private String code;
    private String description;
    private double discount;
    private String start_date;
    private String end_date;

    public Voucher(int id, String code, String description, double discount, String start_date, String end_date) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}