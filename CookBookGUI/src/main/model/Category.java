package main.model;

import javax.persistence.*;

/**
 * Category used to sort recipes
 * @author tmati
 */
@Entity
@Table(name="category")
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CategoryID", nullable=false)
	private int CategoryID;
	
	@Column(name="CategoryDescription")
	private String CategoryDescription;
	
	public Category(String CategoryDescription) {
		this.CategoryDescription = CategoryDescription;
	}
	
	public Category() {
	}

	public String getCategoryDescription() {
		return CategoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		CategoryDescription = categoryDescription;
	}
	
	public int getCategoryID() {
		return CategoryID;
	}
}
