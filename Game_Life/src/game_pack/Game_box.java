package game_pack;

import java.util.Random;

// правило: b3\s23 - burn with 3 neighbour, survive with 2 or 3 neighbours
public class Game_box {
    int size_x; // кол-во столбцов
    int size_y; // кол-во строк
    boolean [][] box;
    int [][] neighbour_box;

    public Game_box(int size_y, int size_x) {
        this.size_y = size_y;
        this.size_x = size_x;
        // выделение памяти
        this.box = new boolean[size_y][size_x];
        this.neighbour_box = new int[this.size_y][this.size_x];
    }

    public void fill_up_rand()
    {
        Random rand = new Random();
        for (int i = 0; i < size_y; i++)
        {
            for (int j = 0; j < size_x; j++)
                this.box[i][j] = rand.nextBoolean();
        }
    }

    // если указано конкретное число живых существ
    public void fill_up_spec(int num_creature)
    {
        this.box = new boolean[this.size_y][this.size_x]; // обнуление

        Random rand = new Random();
        int creature_y;
        int creature_x;
        int k = 0;
        while (k < num_creature)
        {
            creature_y = rand.nextInt(size_y);
            creature_x = rand.nextInt(size_x);
            if (this.box[creature_y][creature_x]) // если там уже есть жизнь - ищем новое место
                continue;
            this.box[creature_y][creature_x] = true;
            k += 1;
        }
    }

    public void neighbour_count()
    {
        this.neighbour_box = new int[this.size_y][this.size_x]; // обнуление (мб есть другой способ)

        for (int i = 0; i < size_y; i++)
        {
            for (int j = 0; j < size_x; j++)
                if (this.box[i][j]) // если есть жизнь
                {
                    // прибавляем к сосед клеткам +1 соседа значит (берем квадрат 3*3)
                    for (int help_i: new int[] {i-1, i, i+1})
                        for (int help_j: new int[] {j-1, j, j+1})
                        {
                            try { this.neighbour_box[help_i][help_j] += 1; }
                            catch (Exception ignored) { } // если край массива
                        }
                    this.neighbour_box[i][j] -= 1;
                }
        }
    }

    public void neighbour_count_no_border()
    {
        // через остаток от деления получать столбец крайний
        this.neighbour_box = new int[this.size_y][this.size_x]; // обнуление (мб есть другой способ)

        // перебираем только внутренную область (без границ)
        for (int i = 1; i < size_y - 1; i++)
        {
            for (int j = 1; j < size_x - 1; j++)
                // складываем значения всех соседей (берем квадрат 3*3)
                for (int help_i: new int[] {i-1, i, i+1})
                    for (int help_j: new int[] {j-1, j, j+1})
                    {
                        if (help_i != i || help_j != j) // сам себя не считает
                            this.neighbour_box[i][j] += this.box[help_i][help_j] ? 1 : 0;
                    }

        }

        // отдельно обрабатываем границы (без углов)
        for (int i = 1; i < size_y - 1; i++) // для первого столбца (j = 0)
        {
            for (int help_i: new int[] {i-1, i, i+1})
                for (int help_j: new int[] {0, 1, size_x - 1})
                {
                    if (help_i != i || help_j != 0) // сам себя не считает
                        this.neighbour_box[i][0] += this.box[help_i][help_j] ? 1 : 0;
                }
        }
        for (int i = 1; i < size_y - 1; i++) // для последнего столбца (j = size_x - 1)
        {
            for (int help_i: new int[] {i-1, i, i+1})
                for (int help_j: new int[] {size_x - 1, size_x - 2, 0})
                {
                    if (help_i != i || help_j != size_x - 1) // сам себя не считает
                        this.neighbour_box[i][size_x - 1] += this.box[help_i][help_j] ? 1 : 0;
                }
        }
        for (int j = 1; j < size_x - 1; j++) // для первой строки (i = 0)
        {
            for (int help_i: new int[] {0, 1, size_y - 1})
                for (int help_j: new int[] {j-1, j, j+1})
                {
                    if (help_i != 0 || help_j != j) // сам себя не считает
                        this.neighbour_box[0][j] += this.box[help_i][help_j] ? 1 : 0;
                }
        }
        for (int j = 1; j < size_x - 1; j++) // для последней строки (i = size_y - 1)
        {
            for (int help_i: new int[] {size_y - 1, size_y - 2, 0})
                for (int help_j: new int[] {j-1, j, j+1})
                {
                    if (help_i != size_y - 1 || help_j != j) // сам себя не считает
                        this.neighbour_box[size_y - 1][j] += this.box[help_i][help_j] ? 1 : 0;
                }
        }

        // отдельно обрабатываем углы
        // левый верхний
        for (boolean neighb: new boolean[]{
                this.box[0][1], this.box[1][0], this.box[1][1],
                this.box[0][size_x - 1], this.box[1][size_x - 1],
                this.box[size_y - 1][0],this.box[size_y - 1][1],
                this.box[size_y - 1][size_x - 1]})
        {
            this.neighbour_box[0][0] += neighb ? 1 : 0;
        }
        // правый верхний
        for (boolean neighb: new boolean[]{
                this.box[0][size_x - 2], this.box[1][size_x - 2], this.box[1][size_x - 1],
                this.box[0][0], this.box[1][0],
                this.box[size_y - 1][size_x - 1],this.box[size_y - 1][size_x - 2],
                this.box[size_y - 1][0]})
        {
            this.neighbour_box[0][size_x - 1] += neighb ? 1 : 0;
        }
        // левый нижний
        for (boolean neighb: new boolean[]{
                this.box[size_y - 2][0], this.box[size_y - 2][1], this.box[size_y - 1][1],
                this.box[0][0], this.box[0][1],
                this.box[size_y - 1][size_x - 1],this.box[size_y - 2][size_x - 1],
                this.box[0][size_x - 1]})
        {
            this.neighbour_box[size_y - 1][0] += neighb ? 1 : 0;
        }
        // правый нижний
        for (boolean neighb: new boolean[]{
                this.box[size_y - 1][size_x - 2], this.box[size_y - 2][size_x - 2], this.box[size_y - 2][size_x - 1],
                this.box[0][size_x - 1], this.box[0][size_x - 2],
                this.box[size_y - 1][0],this.box[size_y - 2][0],
                this.box[0][0]})
        {
            this.neighbour_box[size_y - 1][size_x - 1] += neighb ? 1 : 0;
        }
    }

    public void new_generation()
    {
        for (int i = 0; i < size_y; i++)
            for (int j = 0; j < size_x; j++)
            {
                if (this.neighbour_box[i][j] == 3)
                {
                    this.box[i][j] = true; // либо рождается, либо выживает
                    continue;
                }
                if (this.neighbour_box[i][j] == 2 && this.box[i][j])
                    continue; // не трогаем клетку, остается живой

                this.box[i][j] = false;
        }
    }
}
