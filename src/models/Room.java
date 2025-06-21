package models;

public class RoomType {
    private int id;
    private int villa;
    private String name;
    private int quantity;
    private int capacity;
    private int price;
    private String bed_size;
    private int has_desk;
    private int has_ac;
    private int has_tv;
    private int has_wifi;
    private int has_shower;
    private int has_hotwater;
    private int has_fridge;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVilla() { return villa; }
    public void setVilla(int villa) { this.villa = villa; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getBed_size() { return bed_size; }
    public void setBed_size(String bed_size) { this.bed_size = bed_size; }

    public int getHas_desk() { return has_desk; }
    public void setHas_desk(int has_desk) { this.has_desk = has_desk; }

    public int getHas_ac() { return has_ac; }
    public void setHas_ac(int has_ac) { this.has_ac = has_ac; }

    public int getHas_tv() { return has_tv; }
    public void setHas_tv(int has_tv) { this.has_tv = has_tv; }

    public int getHas_wifi() { return has_wifi; }
    public void setHas_wifi(int has_wifi) { this.has_wifi = has_wifi; }

    public int getHas_shower() { return has_shower; }
    public void setHas_shower(int has_shower) { this.has_shower = has_shower; }

    public int getHas_hotwater() { return has_hotwater; }
    public void setHas_hotwater(int has_hotwater) { this.has_hotwater = has_hotwater; }

    public int getHas_fridge() { return has_fridge; }
    public void setHas_fridge(int has_fridge) { this.has_fridge = has_fridge; }
}
