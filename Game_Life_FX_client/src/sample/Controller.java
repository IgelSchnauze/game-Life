package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {
    @FXML
    GridPane grid_game_box;
    @FXML
    Button btn_new_box;
    @FXML
    Button btn_fromto_file;
    @FXML
    TextField textf_count_str;
    @FXML
    TextField textf_count_column;
    @FXML
    TextField textf_count_life;
    @FXML
    Button btn_new_generation;
    @FXML
    Button btn_start_life;
    @FXML
    Button btn_stop_life;
    @FXML
    TextField textf_count_iteration;
    @FXML
    ToggleButton tgbtn_border;
    @FXML
    ColorPicker color_life;
    @FXML
    ColorPicker color_nolife;

    private int size_y, size_x;
    private StackPane[][] list_of_cells; // ссылки на объекты ячеек
    public static Client client;

    private boolean is_field_empty()
    {
        return textf_count_str.getText().equals("") || textf_count_column.getText().equals("");
    }

    private void make_new_pane()
    {
        grid_game_box.getChildren().clear();
        grid_game_box.getColumnConstraints().clear();
        grid_game_box.getRowConstraints().clear();
        for (int k = 0; k < size_x; k++)
        {
            ColumnConstraints new_column = new ColumnConstraints(Double.MIN_VALUE, 20, Double.MAX_VALUE);
            new_column.setHgrow(Priority.ALWAYS);
            grid_game_box.getColumnConstraints().add(new_column);
        }
        for (int k = 0; k < size_y; k++)
        {
            RowConstraints new_row = new RowConstraints(Double.MIN_VALUE,20, Double.MAX_VALUE);
            new_row.setVgrow(Priority.ALWAYS);
            grid_game_box.getRowConstraints().add(new_row);
        }

        // заполнение сетки объектами, которые будут закрашиваться
        list_of_cells = new StackPane[size_y][size_x];
        for(int i = 0; i < size_y; ++i) {
            for (int j = 0; j < size_x; ++j) {
                StackPane cell = new StackPane();
                grid_game_box.add(cell, j, i);
                list_of_cells[i][j] = cell;
            }
        }
    }

    private void fill_pane()
    {
        //ROSYBROWN, LIGHTBLUE изначально
        Color for_life = color_life.getValue();
        Color for_nolife = color_nolife.getValue();
        for(int i = 0; i < size_y; ++i) {
            for (int j = 0; j < size_x; ++j)
            {
                list_of_cells[i][j].setStyle("-fx-border-color: GRAY");
                if (client.cell(i, j))
                    list_of_cells[i][j].setBackground(new Background
                            (new BackgroundFill(for_life, CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    list_of_cells[i][j].setBackground(new Background
                            (new BackgroundFill(for_nolife, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    @FXML
    public void click_new_box(ActionEvent actionEvent) throws Exception
    {
        client = new Client(); // создание нового объекта

        if (is_field_empty())
            return;
        size_y = Integer.parseInt(textf_count_str.getText());
        size_x = Integer.parseInt(textf_count_column.getText());
        String mb_life_count = textf_count_life.getText();
        if (!mb_life_count.equals(""))
            client.new_game_life(size_y, size_x, Integer.parseInt(mb_life_count));
        else
            client.new_game_rand(size_y, size_x);

        make_new_pane();
        fill_pane();
    }

    @FXML
    public void click_new_generation(ActionEvent actionEvent)
    {
        if (is_field_empty())
            return;
        if (tgbtn_border.isSelected())
            client.new_generation();
        else
            client.new_generation_noborder();
        fill_pane();
    }


    private Timeline timeline;
    private boolean stop;
    private int counter;

    @FXML
    public void click_start_life(ActionEvent actionEvent)
    {
        if (is_field_empty())
            return;
        if (textf_count_iteration.getText().equals(""))
            return;

        stop = false;
        int steps = Integer.parseInt(textf_count_iteration.getText());
        counter = steps;

        timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            if (stop)
                this.timeline.stop();
            client.new_generation();
            fill_pane();
            counter--;
            textf_count_iteration.setText(Integer.toString(counter));
        }));
        timeline.setCycleCount(steps);
        timeline.setOnFinished(e -> textf_count_iteration.setText(""));
        timeline.play();
    }
    @FXML
    public void click_stop_life(ActionEvent actionEvent) { stop = true; }

    @FXML
    public void choose_new_color_life(ActionEvent actionEvent)
    {
        if (is_field_empty())
            return;
        fill_pane(); // перекраска
    }
    @FXML
    public void choose_new_color_nolife(ActionEvent actionEvent)
    {
        if (is_field_empty())
            return;
        fill_pane(); // перекраска
    }
}
