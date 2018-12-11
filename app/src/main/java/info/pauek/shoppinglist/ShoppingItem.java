package info.pauek.shoppinglist;

public class ShoppingItem {
    private boolean checked;
    private String name;

    public ShoppingItem() {}

    public ShoppingItem(String name) {
        this.name = name;
    }

    public ShoppingItem(String name, boolean checked) {
        this.checked = checked;
        this.name = name;
    }

    public static ShoppingItem fromLine(String line) {
        ShoppingItem item = new ShoppingItem();
        String[] parts = line.split(";");
        item.name = parts[0];
        item.checked = (parts[1] == "true");
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}
