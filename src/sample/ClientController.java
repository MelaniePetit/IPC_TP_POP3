package sample;

import Model.Client;
import Model.Console;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Mel on 13/03/2017.
 */
public class ClientController{

    @FXML
    private Button btn_send;
    @FXML
    private TextArea tArea_out;
    @FXML
    private TextField tf_data;

    private PrintStream ps;
    private Client client;

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
        client = new Client();
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
}
