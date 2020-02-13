package main.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import main.model.*;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import main.controller.Controller;
/**
 * FX controller class. Updates View elements to display data. 
 * @author tmati
 *
 */
public class GuiController implements Initializable {
	
	// Initialize all gui elements
	
	@FXML
	private ListView<HBox> recipeList;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label amountLabel;
	
	@FXML
	private Label categoryLabel;
	
	@FXML
	private TextFlow ingredientsList;
	
	@FXML
	private ScrollPane ingredientsScroll;
	
	@FXML
	private TextFlow recipeDescription;
	
	@FXML
	private ScrollPane descriptionScroll;
	
	@FXML
	private ScrollPane recipeListScroll;

	@FXML
	private Button createButton;
	
	@FXML
	private ChoiceBox<Category> categoryChoice;
	
	@FXML
	private TextField searchField;
	
	//Get controller instance.
	Controller ControllerInstance = Controller.getInstance();
	/**
	 * Decimal format to display all numbers untill zero.
	 */
	DecimalFormat format = new DecimalFormat("0.#");
	/**
	 * Recipe to be shown on screen.
	 */
	private Recipe selected = null;
	
	/**
	 * Initialize all elements, prepare converters for collection elements. Fill elements with data
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//createButton.setGraphic(new ImageView(url));
		categoryChoice.setConverter(new StringConverter<Category>() {

			@Override
			public Category fromString(String s) {
				return ControllerInstance.getCategoryByName(s);
			}

			@Override
			public String toString(Category category) {
				return category.getCategoryDescription();
			}
			
		});
		categoryChoice.getItems().addAll(ControllerInstance.getCategories());
		//hide horizontal scroll bars.
		ingredientsScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		descriptionScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		recipeListScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		recipeList.setEditable(false);
		
		/*Category c1 = ControllerInstance.SaveCategory("Pancakes");
		
		Recipe r1 = new Recipe("Best fluffy Pancakes", "12 pancakes", "Combine together the flour, sugar (or sweetener), baking powder, baking soda and salt in a large-sized bowl. Make a well in the centre and add the milk, slightly cooled melted butter, vanilla and egg." 
				+" Use a wire whisk to whisk the wet ingredients together first before slowly folding them into the dry ingredients. Mix together until smooth (there may be a couple of lumps but that's okay)."
						+ " (The batter will be thick and creamy in consistency. If you find the batter too thick -- doesn't pour off the ladle or out of the measuring cup smoothly -- fold a couple tablespoons of extra milk into the batter at a time until reaching desired consistency)."
						+ " Set the batter aside and allow to rest while heating up your pan or griddle. "
						+ " Heat a nonstick pan or griddle over low-medium heat and wipe over with a little butter to lightly grease pan. Pour ¼ cup of batter onto the pan and spread out gently into a round shape with the back of your ladle or measuring cup."
						+ " When the underside is golden and bubbles begin to appear on the surface, flip with a spatula and cook until golden. Repeat with remaining batter. "
						+ " Serve with honey, maple syrup, fruit, ice cream or frozen yoghurt, or enjoy plain!" , c1, ControllerInstance.collectIngredients());
		
		ControllerInstance.SaveRecipe(r1);*/
		
		//Initialize gui elements with default values
		recipeDescription.getChildren().add(new Text(""));
		nameLabel.setText("No recipe selected");
		amountLabel.setText("");
		categoryLabel.setText("");
		
		//Get all recipes.
		List <Recipe> Recipes = ControllerInstance.getRecipes();
		
		//Create A HBox into the listView for each recipe with corresponding update and delete buttons.
		for (Recipe r : Recipes) {
			Pane P = new Pane();
			HBox H = new HBox();
			Button bEdit = new Button("*");
			bEdit.setOnAction(new EventHandler <ActionEvent>() {
				//TODO HANDLE EDIT
				@Override
				public void handle(ActionEvent event) {
					System.out.println(r.getRecipename());
					}
			});
			Button bDelete = new Button ("#");
			bDelete.setOnAction(new EventHandler <ActionEvent>() {
				//TODO HANDLE DELETION
				@Override
				public void handle(ActionEvent event) {
					System.out.println(r.getRecipename());
					}
			});
			H.getChildren().addAll(new Label(r.getRecipename()), P, bEdit, bDelete);
			H.setHgrow(P, Priority.ALWAYS);
			H.setOnMouseClicked(new EventHandler <MouseEvent> () {
				//Populate all fields with recipe data
				@Override
				public void handle(MouseEvent event) {
					populateView(r);
				}
				
			});
			//Add filled HBox to listView
			recipeList.getItems().add(H);
		}
	}
	
	/**
	 * Use selected recipe to fill all elements with it's content.
	 * @param selected
	 */
	void populateView (Recipe selected) {
		recipeDescription.getChildren().clear();
		recipeDescription.getChildren().add(new Text(selected.getDescription()));
		nameLabel.setText(selected.getRecipename());
		amountLabel.setText(selected.getAmount());
		categoryLabel.setText(selected.getCategory().getCategoryDescription());
		for (Ingredient i : selected.getIngredients()) {
			ingredientsList.getChildren().add(new Text(format.format(i.getAmount()) + "   " + i.getMeasure() + "     " + i.getName() + "\n"));
		}
	}
}
