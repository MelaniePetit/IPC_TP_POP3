package Controller;

import Model.Console;
import Model.SClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

/**
 * Created by jeremy on 19/03/2017.
 */
public class SClientController {
    @FXML
    private Button btn_send;
    @FXML
    private TextArea tArea_out;
    @FXML
    private TextField tf_data;
    @FXML
    private ImageView iv_connect;

    private PrintStream ps;
    private SClient client;

    public void initialize() {
        ps = new PrintStream(new Console(tArea_out));
        tArea_out.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                tArea_out.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
        System.setOut(ps);
        System.setErr(ps);

        openDialog();

    }

    protected void openDialog() {
        try {
            TextInputDialog dialog = new TextInputDialog("127.0.0.1");
            dialog.setTitle("Ip address of server POP3");
            dialog.setHeaderText("Fill the textfield with the ip address of the server");
            dialog.setContentText("Ip address:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                client = new SClient(result.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(MouseEvent mouseEvent) {
        client.sendRequest(tf_data.getText());
        tf_data.setText("");
    }

    public void enterSend(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            client.sendRequest(tf_data.getText());
            tf_data.setText("");
        }
    }

    public void connect()
    {
        if (iv_connect.getImage().impl_getUrl() == "/log.png")
        {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Login Dialog");
            dialog.setHeaderText("Look, a Custom Login Dialog");

            // Set the icon (must be included in the project).
            dialog.setGraphic(new ImageView("/login.png"));

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField username = new TextField();
            username.setPromptText("Username");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");

            grid.add(new Label("Username:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Password:"), 0, 1);
            grid.add(password, 1, 1);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> username.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                client.sendRequest("APOP " + usernamePassword.getKey() + " " + usernamePassword.getValue());
            });
        }
        else {
            disconnect();
        }

    }

    public void disconnect()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sur you want to quit ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            client.sendRequest("QUIT");
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
}
