/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.initScene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import snakesandladders.players.ePlayerType;

/**
 * FXML Controller class
 *
 * @author Noam
 */
public class SceneInitController implements Initializable {

    private List<RadioButton> m_radioButtons;
    private List<CheckBox> m_checkedPlayers;
    private List<TextField> m_textPlayers;
    private List<MenuButton> m_menuButtonsPlayers;
    private boolean isErrorMessageShown = false;
    private SimpleBooleanProperty finishedInit;
    private SimpleBooleanProperty CancelInit;
    private int m_NumberOfCheckedPlayers;

    @FXML
    private TextField textBoxNamePlayer1;
    @FXML
    private CheckBox checkBoxPlayer2;
    @FXML
    private TextField textBoxNamePlayer4;
    @FXML
    private TextField textBoxNamePlayer2;
    @FXML
    private TextField textBoxNamePlayer3;
    @FXML
    private RadioButton radioButton7;
    @FXML
    private RadioButton radioButton5;
    @FXML
    private CheckBox checkBoxPlayer1;
    @FXML
    private RadioButton radioButton6;
    @FXML
    private CheckBox checkBoxPlayer3;
    @FXML
    private RadioButton radioButton8;
    @FXML
    private CheckBox checkBoxPlayer4;
    @FXML
    private Label labelNumOfPlayers;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonOk;
    @FXML
    private Slider slider;
    @FXML
    private MenuButton MenuButtonPlayer1;
    @FXML
    private MenuButton MenuButtonPlayer2;
    @FXML
    private MenuButton MenuButtonPlayer3;
    @FXML
    private MenuButton MenuButtonPlayer4;
    @FXML
    private MenuItem humanItem1;
    @FXML
    private MenuItem computerItem1;
    @FXML
    private MenuItem humanItem2;
    @FXML
    private MenuItem computerItem2;
    @FXML
    private MenuItem humanItem3;
    @FXML
    private MenuItem computerItem3;
    @FXML
    private MenuItem humanItem4;
    @FXML
    private MenuItem computerItem4;
    @FXML
    private Label labelNumOfSoldiersToWin;
    @FXML
    private Slider sliderSoldiersToWin;
    @FXML
    private AnchorPane paneBoardSize;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private AnchorPane PlayerPane;
    private int m_BoardSize;
    private int m_ComputerIndex;
    private SimpleBooleanProperty openGameSelected;
    private int m_IndexOfPlayerList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        m_radioButtons = new ArrayList<>();
        m_checkedPlayers = new ArrayList<>();
        m_menuButtonsPlayers = new ArrayList<>();
        m_textPlayers = new ArrayList<>();
        m_NumberOfCheckedPlayers = 2;
        m_IndexOfPlayerList = 0;

        initRadioButtons();
        initCheckedPlayers();
        initMenuButtonsPlayers();
        initTextPlayers();
        finishedInit = new SimpleBooleanProperty(false);
        CancelInit = new SimpleBooleanProperty(false);
        openGameSelected = new SimpleBooleanProperty(false);

        m_BoardSize = 5;
        m_ComputerIndex = 0;
    }

    @FXML
    private void handleSubmitRadioButtonAction(ActionEvent event) {
        for (RadioButton button : m_radioButtons) {
            if ((((RadioButton) event.getSource()).getId()) != (button.getId())) {
                button.setSelected(false);
            } else {
                button.setSelected(true);
            }
        }
        setSlider(event);
        slider.setValue(1);
        sliderSoldiersToWin.setValue(1);
        dragged();
        draggedSoldiersToWin();
        slider.disableProperty().set(false);
        sliderSoldiersToWin.disableProperty().set(false);
        buttonOk.disableProperty().set(false);
        buttonCancel.disableProperty().set(false);

    }

    @FXML
    private void dragged() {
        labelNumOfPlayers.textProperty().set(String.valueOf((int) slider.getValue()));
    }

    @FXML
    private void draggedSoldiersToWin() {
        labelNumOfSoldiersToWin.textProperty().set(String.valueOf((int) sliderSoldiersToWin.getValue()));
    }

    private void initRadioButtons() {
        m_radioButtons.add(radioButton5);
        m_radioButtons.add(radioButton6);
        m_radioButtons.add(radioButton7);
        m_radioButtons.add(radioButton8);
    }

    private void initCheckedPlayers() {
        m_checkedPlayers.add(checkBoxPlayer1);
        m_checkedPlayers.add(checkBoxPlayer2);
        m_checkedPlayers.add(checkBoxPlayer3);
        m_checkedPlayers.add(checkBoxPlayer4);
    }

    private void initMenuButtonsPlayers() {
        m_menuButtonsPlayers.add(MenuButtonPlayer1);
        m_menuButtonsPlayers.add(MenuButtonPlayer2);
        m_menuButtonsPlayers.add(MenuButtonPlayer3);
        m_menuButtonsPlayers.add(MenuButtonPlayer4);
    }

    private void initTextPlayers() {
        m_textPlayers.add(textBoxNamePlayer1);
        m_textPlayers.add(textBoxNamePlayer2);
        m_textPlayers.add(textBoxNamePlayer3);
        m_textPlayers.add(textBoxNamePlayer4);

        for (TextField textField : m_textPlayers) {
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (playerDuplicateNameExist()) {
                        showError("Name already exists");
                    } else {
                        hideError();
                    }
                }
            });
        }
    }

    @FXML
    private void playerChecked(ActionEvent event) {
        for (int i = 0; i < m_checkedPlayers.size(); i++) {
            if (((CheckBox) (event.getSource())).getId().equals(m_checkedPlayers.get(i).getId())) {
                if (m_checkedPlayers.get(i).selectedProperty().get() == true) {
                    m_NumberOfCheckedPlayers++;
                    m_menuButtonsPlayers.get(i).disableProperty().set(false);
                    m_textPlayers.get(i).disableProperty().set(false);
                    m_menuButtonsPlayers.get(i).disableProperty().set(false);
                } else {
                    m_NumberOfCheckedPlayers--;
                    m_menuButtonsPlayers.get(i).disableProperty().set(true);
                    m_textPlayers.get(i).disableProperty().set(true);
                    m_menuButtonsPlayers.get(i).disableProperty().set(true);
                }
            }
        }
        if (m_NumberOfCheckedPlayers >= 2) {
            buttonOk.disableProperty().set(false);
        } else {
            buttonOk.disableProperty().set(true);
        }
    }

    private void setSlider(ActionEvent event) {
        int numOfSnakesAndLadders;

        switch (((RadioButton) event.getSource()).getText()) {
            case "5X5":
                m_BoardSize = 5;
                numOfSnakesAndLadders = calculateNumOfSnakesAndLadder(25);
                slider.maxProperty().set(numOfSnakesAndLadders);
                break;
            case "6X6":
                m_BoardSize = 6;
                numOfSnakesAndLadders = calculateNumOfSnakesAndLadder(36);
                slider.maxProperty().set(numOfSnakesAndLadders);
                break;
            case "7X7":
                m_BoardSize = 7;
                numOfSnakesAndLadders = calculateNumOfSnakesAndLadder(49);
                slider.maxProperty().set(numOfSnakesAndLadders);
                break;
            case "8X8":
                m_BoardSize = 8;
                numOfSnakesAndLadders = calculateNumOfSnakesAndLadder(64);
                slider.maxProperty().set(numOfSnakesAndLadders);
                break;
            default:
                break;
        }
    }

    private int calculateNumOfSnakesAndLadder(int BoardSize) {
        return ((int) (BoardSize / 5));
    }

    @FXML
    private void menuItemPlayer1Checked(ActionEvent event) {
        String text = ((MenuItem) event.getSource()).getText();
        if (text.equals("Computer")) {
            textBoxNamePlayer1.disableProperty().set(true);
        } else {
            textBoxNamePlayer1.disableProperty().set(false);
        }

        MenuButtonPlayer1.textProperty().bind(Bindings.concat((text)));
    }

    @FXML
    private void menuItemPlayer2Checked(ActionEvent event) {
        String text = ((MenuItem) event.getSource()).getText();
        if (text.equals("Computer")) {
            textBoxNamePlayer2.disableProperty().set(true);
        } else {
            textBoxNamePlayer2.disableProperty().set(false);
        }

        MenuButtonPlayer2.textProperty().bind(Bindings.concat((text)));
    }

    @FXML
    private void menuItemPlayer3Checked(ActionEvent event) {
        String text = ((MenuItem) event.getSource()).getText();
        if (text.equals("Computer")) {
            textBoxNamePlayer3.disableProperty().set(true);
        } else {
            textBoxNamePlayer3.disableProperty().set(false);
        }

        MenuButtonPlayer3.textProperty().bind(Bindings.concat(text));
    }

    @FXML
    private void menuItemPlayer4Checked(ActionEvent event) {
        String text = ((MenuItem) event.getSource()).getText();
        if (text.equals("Computer")) {
            textBoxNamePlayer4.disableProperty().set(true);
        } else {
            textBoxNamePlayer4.disableProperty().set(false);
        }

        MenuButtonPlayer4.textProperty().bind(Bindings.concat((text)));
    }

    public void showError(String message) {
        if (!isErrorMessageShown) {
            isErrorMessageShown = true;
            errorMessageLabel.textProperty().setValue(message);
            FadeTransition animation = FadeTransitionBuilder.create()
                    .node(errorMessageLabel)
                    .duration(Duration.seconds(0.3))
                    .fromValue(0.0)
                    .toValue(1.0)
                    .build();
            animation.play();
        }

    }

    private void hideError() {
        if (isErrorMessageShown) {
            FadeTransition animation = FadeTransitionBuilder.create()
                    .node(errorMessageLabel)
                    .duration(Duration.seconds(0.3))
                    .fromValue(1.0)
                    .toValue(0.0)
                    .build();
            animation.play();

            isErrorMessageShown = false;
            errorMessageLabel.textProperty().setValue("");

        }
    }

    private boolean playerDuplicateNameExist() {
        boolean duplicate = false;
        for (TextField textField : m_textPlayers) {
            for (TextField player : m_textPlayers) {
                if (textField != player && !textField.getText().equals("") && textField.getText().equals(player.getText())) {
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }

    @FXML
    private void onCancel(ActionEvent event) {
        CancelInit.set(true);
    }

    public SimpleBooleanProperty getCancelInit() {
        return CancelInit;
    }

    @FXML
    private void onContinue(ActionEvent event) {
        finishedInit.set(true);
    }

    public SimpleBooleanProperty getFinishedInit() {
        return finishedInit;
    }

    public void setFinishedInit(boolean value) {
        finishedInit.set(value);
    }

    public int GetNumOfPlayers() {
        int numOfPlayers = 0;
        for (CheckBox checkBox : m_checkedPlayers) {
            if (checkBox.selectedProperty().get() == true) {
                numOfPlayers++;
            }
        }

        return numOfPlayers;
    }

    public int GetNumOfSnakesAndLadders() {
        return (int) slider.getValue();
    }

    public int getNumberOfSoldiersToWin() {
        return (int) sliderSoldiersToWin.getValue();
    }

    public int getBoardSize() {
        return m_BoardSize;
    }

    private ePlayerType getPlayerType(int i) {
        ePlayerType typeReturned = null;
        switch (m_menuButtonsPlayers.get(i).getText()) {
            case "Human":
                typeReturned = ePlayerType.HUMAN;
                break;
            case "Computer":
                typeReturned = ePlayerType.COMPUTER;
                break;
            default:
                break;
        }
        return typeReturned;
    }

    private String getPlayerString(int index) {
        String text = m_textPlayers.get(index).getText();
        String promptText = m_textPlayers.get(index).getPromptText();
        if (text.equals("")) {
            return promptText;
        } else {
            return text;
        }
    }

    @FXML
    private void openGameClicked(ActionEvent event) {
        openGameSelected.set(true);
    }

    public SimpleBooleanProperty getOpenGameSelected() {
        return openGameSelected;
    }

    public String getLegalPlayerString() {
        for (int i = m_IndexOfPlayerList; i < 4; i++) {
            if (m_checkedPlayers.get(i).selectedProperty().get()) {
                return getPlayerString(i);
            }
        }
        return null;
    }

    public ePlayerType getLegalPlayerType() {
        for (int i = m_IndexOfPlayerList; i < 4; i++) {
            if (m_checkedPlayers.get(i).selectedProperty().get()) {
                m_IndexOfPlayerList = i + 1;
                return getPlayerType(i);
            }
        }
        return null;
    }

}
