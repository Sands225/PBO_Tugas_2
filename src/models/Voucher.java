package models;

public class Voucher {
    private int id;
    private String code;
    private String description;
    private double discount;
    private String start_date;
    private String end_date;

    public Voucher() {}

    public Voucher(int id, String code, String description, double discount, String start_date, String end_date) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() { return id; }
    public void setId(int voucherId) { this.id = voucherId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public String getStart_date() { return start_date; }
    public void setStart_date(String start_date) { this.start_date = start_date; }

    public String getEnd_date() { return end_date; }
    public void setEnd_date(String end_date) { this.end_date = end_date; }
}