package main.view;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import main.model.*;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Window;
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
	private Pane bgPane;
	
	@FXML
	private ListView<HBox> recipeList;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label amountLabel;
	
	@FXML
	private Label titleLabel;
	
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
	
	@FXML private Label categoryLabel;
	
	Popup popup;
	
	//Get controller instance.
	Controller ControllerInstance = Controller.getInstance();
	/**
	 * Decimal format to display all numbers until zero.
	 */
	DecimalFormat format = new DecimalFormat("0.#");
	/**
	 * Recipe to be shown on screen.
	 */
	private Recipe selectedRecipe = ControllerInstance.getSelected();
	
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
		categoryChoice.getItems().add(0, new Category("All categories"));
		
		//hide horizontal scroll bars.
		ingredientsScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		descriptionScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		recipeListScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		recipeList.setEditable(false);
		
		//Initialize gui elements with default values
		recipeDescription.getChildren().add(new Text(""));
		nameLabel.setText("No recipe selected");
		amountLabel.setText("");
		titleLabel.setText("");
		
		//Get all recipes.
		List <Recipe> Recipes = ControllerInstance.getRecipes();
		
		generateRecipeList(Recipes);
	}
	
	/**
	 * Use selected recipe to fill all elements with it's content.
	 * @param selected
	 */
	void populateView (Recipe selected) {
		ingredientsList.getChildren().clear();
		recipeDescription.getChildren().clear();
		recipeDescription.getChildren().add(new Text(selected.getDescription()));
		nameLabel.setText(selected.getRecipename());
		amountLabel.setText(selected.getAmount());
		titleLabel.setText(selected.getCategory().getCategoryDescription());

		for (Ingredient i : selected.getIngredients()) {
			Text amount = new Text(format.format(i.getAmount()) + "		");
			Text measure = new Text(i.getMeasure()+ "		");
			Text name = new Text(i.getName());
			Text newLine = new Text("\n");
			
			ingredientsList.getChildren().add(amount);
			ingredientsList.getChildren().add(measure);
			ingredientsList.getChildren().add(name);
			ingredientsList.getChildren().add(newLine);
		}
	}

	@FXML
	public void newRecipePopup(MouseEvent event) throws IOException {
		if(popup == null || !popup.isShowing()) {
			popup = new Popup();
			Object source = event.getSource();
			Node node = (Node) source;
			Scene scene = node.getScene();
			Window window = scene.getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Recipe.fxml"));
			popup.getContent().add((Parent) loader.load());
			popup.show(window);
		}
	}
	
	public void generateRecipeList(List<Recipe> Recipes) {
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
	
	@FXML
	public void sortByCategory(ActionEvent event) {
		recipeList.getItems().clear();
		if(!categoryChoice.getSelectionModel().getSelectedItem().getCategoryDescription().equals("All categories")) {
		Category selected = categoryChoice.getSelectionModel().getSelectedItem();
		List <Recipe>filteredRecipes = ControllerInstance.getRecipesByCategory(selected);
		generateRecipeList(filteredRecipes);
		categoryChoice.getItems().clear();
		categoryChoice.getItems().addAll(Controller.getInstance().getCategories());
		categoryChoice.getItems().add(0, new Category("All categories"));
		} else {
			generateRecipeList(ControllerInstance.getRecipes());
		}
	}
}
