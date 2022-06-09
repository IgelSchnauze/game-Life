package game_pack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Game_Test {
    @Test
    void creating_num_creature() throws Exception
    {
        Game_box game_box = new Game_box(5,10);
        int num_creat_test = 8;
        game_box.fill_up_spec(num_creat_test);
        int num_creat_real = 0;
        for (boolean [] string: game_box.box)
            for (boolean cell: string)
                num_creat_real += cell ? 1 : 0;

        assertEquals(num_creat_test,num_creat_real);
    }

    @Test
    void incorrect_num_creature()
    {
        Game_controller controller = new Game_controller();
        int size_x = 10, size_y = 10;
        int num_creat_test = -1; // <0
        try
        {
            controller.create_new_life_box(size_y,size_x, num_creat_test);
            fail(); // если не вылетело с исключением в catch, значит тест не пройден
        }
        catch (Exception e)
        {
            assertTrue(true); // тест пройден
        }

        size_x = 10;
        size_y = 10;
        num_creat_test = (size_x * size_y) + 1; // > cells
        try
        {
            controller.create_new_life_box(size_y,size_x, num_creat_test);
            fail(); // если не вылетело с исключением в catch, значит тест не пройден
        }
        catch (Exception e)
        {
            assertTrue(true); // тест пройден
        }
    }

    @Test
    void incorrect_box_size()
    {
        Game_controller controller = new Game_controller();
        int size_x = -1, size_y = 0;
        try
        {
            controller.create_new_life_box(size_y, size_x);
            fail(); // если не вылетело с исключением в catch, значит тест не пройден
        }
        catch (Exception e)
        {
            assertTrue(true); // тест пройден
        }
    }

    @Test
    void neighbour_count_border()
    {
        Game_box game_box = new Game_box(3, 3);

        // заполняю всего живиыми клетками
        game_box.fill_up_spec(9);
        game_box.neighbour_count();

        assertArrayEquals(new int[] {3, 5, 3},game_box.neighbour_box[0]);
        assertArrayEquals(new int[] {5, 8, 5},game_box.neighbour_box[1]);
        assertArrayEquals(new int[] {3, 5, 3},game_box.neighbour_box[2]);

        // заполняю пустыми
        game_box.fill_up_spec(0);
        game_box.neighbour_count();

        assertArrayEquals(new int[] {0, 0, 0},game_box.neighbour_box[0]);
        assertArrayEquals(new int[] {0, 0, 0},game_box.neighbour_box[1]);
        assertArrayEquals(new int[] {0, 0, 0},game_box.neighbour_box[2]);
    }

    @Test
    void neighbour_count_no_border()
    {
        Game_box game_box = new Game_box(3, 3);
        // заполняю всего живиыми клетками
        game_box.fill_up_spec(9); // заполняю всего живиыми клетками
        game_box.neighbour_count_no_border();
        for (int i=0; i<3; i++)
            assertArrayEquals(new int[] {8, 8, 8},game_box.neighbour_box[i]);

        // заполняю особым образом, чтобы проверить углы и края
        game_box.box = new boolean[][]
                        {{true, true, true},
                        {false, false, false},
                        {true, false, true}};
        game_box.neighbour_count_no_border();

        assertArrayEquals(new int[] {4, 4, 4},game_box.neighbour_box[0]);
        assertArrayEquals(new int[] {5, 5, 5},game_box.neighbour_box[1]);
        assertArrayEquals(new int[] {4, 5, 4},game_box.neighbour_box[2]);
    }

    @Test
    void new_generation()
    {
        /*
        влияение на происходящее (влияет - 1)
        0 1 0
        1 1 0
        0 1 1
        */
        Game_box game_box = new Game_box(3, 3);

        // добавление жизни около 3х (+ недобавление при >3), продолжение при 2х и при 3х, смерть при >3
        game_box.box = new boolean[][]
                {{false, true, false},
                {true, true, false},
                {false, true, true}};
        game_box.neighbour_count();
        game_box.new_generation();

        assertArrayEquals(new boolean[] {true, true, false},game_box.box[0]);
        assertArrayEquals(new boolean[] {true, false, false},game_box.box[1]);
        assertArrayEquals(new boolean[] {true, true, true},game_box.box[2]);

        /*
        влияение на происходящее (влияет - 1)
        1 0 1
        0 0 1
        0 0 0
        */
        game_box.box = new boolean[][]
                        {{true, false, true},
                        {false, false, true},
                        {true, false, false}};
        game_box.neighbour_count();
        game_box.new_generation();

        assertArrayEquals(new boolean[] {false, true, false},game_box.box[0]);
        assertArrayEquals(new boolean[] {false, false, false},game_box.box[1]);
        assertArrayEquals(new boolean[] {false, false, false},game_box.box[2]);

        /*
        влияение на происходящее (влияет - 1)
        0 0 0
        0 0 0
        1 0 0
        */
        game_box.box = new boolean[][]
                        {{true, false, false},
                        {true, false, false},
                        {true, false, false}};
        game_box.neighbour_count();
        game_box.new_generation();

        assertArrayEquals(new boolean[] {false, false, false},game_box.box[0]);
        assertArrayEquals(new boolean[] {true, true, false},game_box.box[1]);
        assertArrayEquals(new boolean[] {false, false, false},game_box.box[2]);
    }

    @Test
    void static_object()
    {
        Game_box game_box = new Game_box(3, 3);
        game_box.box = new boolean[][]
                        {{false, false, false},
                        {false, true, true},
                        {false, true, true}};

        int count_gener = 20;
        while (count_gener > 0)
        {
            game_box.neighbour_count();
            game_box.new_generation();
            count_gener --;
        }

        assertArrayEquals(new boolean[] {false, false, false},game_box.box[0]);
        assertArrayEquals(new boolean[] {false, true, true},game_box.box[1]);
        assertArrayEquals(new boolean[] {false, true, true},game_box.box[2]);
    }

    @Test
    void tset()
    {

    }
}