package game_pack;

public class Game_controller {
    private static Game_box game_box;

    public Game_controller()
    {}

    public static int[] get_size_box()
    {
        return new int[] {game_box.size_y, game_box.size_x};
    }

    public static boolean is_cell_alive(int i, int j)
    {
        return game_box.box[i][j];
    }

    public void create_new_life_box(int size_y, int size_x) throws Exception
    {
        if (size_y <= 0 || size_x <= 0)
            throw new Exception("Shape of box isn't correct, please, enter new size");

        game_box = new Game_box(size_y,size_x);
        game_box.fill_up_rand();
    }

    public void create_new_life_box(int size_y, int size_x, int num_creature) throws Exception
    {
        if (size_y <= 0 || size_x <= 0)
            throw new Exception("Shape of box isn't correct, please, enter new size");

        if (num_creature < 0 || num_creature > size_x * size_y)
            throw new Exception("Number of creature can't be less than 0 and bigger than number of box cells");

        game_box = new Game_box(size_y, size_x);
        game_box.fill_up_spec(num_creature);
    }

    public void get_new_generation()
    {
        game_box.neighbour_count();
        game_box.new_generation();
    }

    public void get_new_generation_no_border()
    {
        game_box.neighbour_count_no_border();
        game_box.new_generation();
    }

    public void print_box() { Game_represent.print_box(game_box.box); }

}
