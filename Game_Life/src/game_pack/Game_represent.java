package game_pack;

public class Game_represent {

    // вывод с запросами контроллеру на каждую ячейку отдельно
    public static void print()
    {
        int[] sizes = Game_controller.get_size_box();
        int size_y = sizes[0], size_x = sizes[1];

        for (int i = 0; i < size_y; i++)
        {
            for (int j = 0; j < size_x; j++)
                System.out.print((Game_controller.is_cell_alive(i,j) ? '@' : '-') + "  ");
            System.out.println();
        }
        System.out.println();
    }

    // вывод с параметром целой матрицы, небезопасный!!!
    public static void print_box (boolean[][] game_box)
    {
        for (boolean[] str: game_box)
        {
            for (boolean elem: str)
                //System.out.print((elem ? 1 : 0) + "  ");
                System.out.print((elem ? '@' : '-') + "  ");
            System.out.println();
        }
        System.out.println();
    }

    public static void print_neighbor_box(int[][] neighbor_box)
    {
        for (int[] str: neighbor_box)
        {
            for (int elem: str)
                System.out.print(elem + "  ");
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception
    {
        Game_controller control = new Game_controller();
        control.create_new_life_box(6,7, 5);
        //control.print_box();
        print();

        int loop = 10;
        while (loop > 0)
        {
            control.get_new_generation();
            print();
            loop--;
        }
    }
}
