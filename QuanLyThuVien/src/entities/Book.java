package entities;

public class Book {
    private String id;
    private String title;
    private boolean status;
    private double price;
    private String description;
    private String author;
    private String categoryId;

    public Book() {
    }

    public Book(String id, String title, boolean status,
                double price, String description,
                String author, String categoryId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.price = price;
        this.description = description;
        this.author = author;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", status=" + status + ", price=" + price + ", description="
				+ description + ", author=" + author + ", categoryId=" + categoryId + "]";
	}
}