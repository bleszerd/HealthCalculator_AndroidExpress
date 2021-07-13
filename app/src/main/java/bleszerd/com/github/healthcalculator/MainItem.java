package bleszerd.com.github.healthcalculator;

public class MainItem {
    private int id;
    private int drawableId;
    private int title;
    private int color;

    public MainItem(int id, int drawableId, int title, int color) {
        this.id = id;
        this.drawableId = drawableId;
        this.title = title;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
