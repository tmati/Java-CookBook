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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
	
	//Initialize data structures for filter-by-text
	ObservableList<HBox> obsRecipes = FXCollections.observableArrayList();
	FilteredList<HBox> filteredList = new FilteredList<HBox>(obsRecipes, s -> true);
	
	//Get controller instance.
	Controller controllerInstance = Controller.getInstance();
	
	/**
	 * Decimal format to display all numbers until zero.
	 */
	DecimalFormat format = new DecimalFormat("0.#");
	
	/**
	 * Recipe to be passed onto edit form.
	 */
	private Recipe selectedRecipe = controllerInstance.getSelected();
	
	/**
	 * Initialize all elements, prepare converters for collection elements. Fill elements with data.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ingredientsList.setBackground(Background.EMPTY);
		//createButton.setGraphic(new ImageView(url));
		categoryChoice.setConverter(new StringConverter<Category>() {

			@Override
			public Category fromString(String s) {
				return controllerInstance.getCategoryByName(s);
			}

			@Override
			public String toString(Category category) {
				return category.getCategoryDescription();
			}
			
		});
		
		Category all = new Category ("All categories");
		categoryChoice.getItems().add(0, all);
		categoryChoice.getItems().addAll(controllerInstance.getCategories());
		categoryChoice.getSelectionModel().selectFirst();

		
		//hide horizontal scroll bars.
		ingredientsScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		descriptionScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		recipeListScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		//Listener for searching recipes with text field
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(member -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				Label textChild = (Label) member.lookup("#recipeLabel");
				if (textChild.getText().contains(newValue)) {
					return true;
				}
				return false;
			});
			recipeList.setItems(filteredList);
		});
		
		recipeList.setEditable(false);
		
		//Initialize gui elements with default values
		recipeDescription.getChildren().add(new Text(""));
		nameLabel.setText("No recipe selected");
		amountLabel.setText("");
		titleLabel.setText("");
		
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

		//Add ingredients to ingredients table.
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

	/**
	 * Open up a popup to create a new recipe.
	 * @param event The MouseEvent of the button pressed.
	 * @throws IOException file handling exception to throw.
	 */
	@FXML
	public void newRecipePopup(MouseEvent event) throws IOException {
		controllerInstance.setSelected(null);
		if(popup == null || !popup.isShowing()) {
			popup = new Popup();
			popup.setOnHidden(new EventHandler <WindowEvent> () {

				@Override
				public void handle(WindowEvent event) {
					//reload recipes upon return to main view.
					searchField.setText("");
					obsRecipes.clear();
					generateRecipeList(controllerInstance.getRecipes());
					categoryChoice.getItems().clear();
					categoryChoice.getItems().addAll(Controller.getInstance().getCategories());
					//TODO clear empty categories here
				}
				
			});
			//Open popup for new recipe
			Object source = event.getSource();
			Node node = (Node) source;
			Scene scene = node.getScene();
			Window window = scene.getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Recipe.fxml"));
			popup.getContent().add((Parent) loader.load());
			popup.show(window);
		}
	}
	
	/**
	 * Generates a visual representation for each recipe into the recipe list element. The representation includes buttons for editing and deleting corresponding recipe objects.
	 * @param Recipes The list of recipes used to populate the ListView.
	 */
	public void generateRecipeList(List<Recipe> Recipes) {
		//Create A HBox into the listView for each recipe with corresponding update and delete buttons.
		for (Recipe r : Recipes) {
			Pane P = new Pane();
			HBox H = new HBox();
			Button bEdit = new Button("EDIT");
			bEdit.setOnAction(new EventHandler <ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						//open popup for editing existing recipe
						controllerInstance.setSelected(r);
						if (popup == null || !popup.isShowing()) {
							Object source = event.getSource();
							Node node = (Node) source;
							Scene scene = node.getScene();
							Window window = scene.getWindow();
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Recipe.fxml"));
							Popup editPopup = new Popup();
							editPopup.getContent().add((Parent) loader.load());
							editPopup.show(window);
							editPopup.setOnHidden(new EventHandler<WindowEvent>() {

								@Override
								public void handle(WindowEvent event) {
									//reload recipes upon return to main view.
									searchField.setText("");
									obsRecipes.clear();
									generateRecipeList(controllerInstance.getRecipes());
									//TODO clear empty Categories here
								}
							});
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				}
			);
			Button bDelete = new Button ("DELETE");
			bDelete.setOnAction(new EventHandler <ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					//Ask for delete confirmation
					Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this recipe?", ButtonType.YES, ButtonType.NO);
					alert.showAndWait();
					
					if(alert.getResult() == ButtonType.YES) {
						controllerInstance.deleteRecipe(r);
						obsRecipes.clear();
						generateRecipeList(controllerInstance.getRecipes());
					}
				}
			});
			Label recipeLabel = new Label(r.getRecipename());
			//Set id for filter to find this text from the HBox element
			recipeLabel.setId("recipeLabel");
			H.getChildren().addAll(recipeLabel, P, bEdit, bDelete);
			H.setHgrow(P, Priority.ALWAYS);
			H.setOnMouseClicked(new EventHandler <MouseEvent> () {
				//Populate all fields with recipe data
				@Override
				public void handle(MouseEvent event) {
					populateView(r);
				}
				
			});
			//Add filled HBox to listView
			obsRecipes.add(H);
		}
		recipeList.setItems(filteredList);
	}
	
	/**
	 * Sorting functionality for category ChoiceBox use.
	 * @param event ActionEvent that is fired when ChoiceBox value changes.
	 */
	@FXML
	public void sortByCategory(ActionEvent event) {
		if(!categoryChoice.getSelectionModel().getSelectedItem().getCategoryDescription().equals("All categories")) {
		Category selected = categoryChoice.getSelectionModel().getSelectedItem();
		int index = categoryChoice.getSelectionModel().getSelectedIndex();
		obsRecipes.clear();
		List <Recipe>filteredRecipes = controllerInstance.getRecipesByCategory(selected);
		//Populate recipe list
		generateRecipeList(filteredRecipes);
		categoryChoice.getItems().clear();
		categoryChoice.getItems().addAll(Controller.getInstance().getCategories());
		//Set new choiceBox option to reset filter.
		categoryChoice.getItems().add(0, new Category("All categories"));
		categoryChoice.getSelectionModel().select(index);
		} else {
			generateRecipeList(controllerInstance.getRecipes());
		}
	}
}
