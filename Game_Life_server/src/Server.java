import game_pack.Game_controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static public void main(String [] args){
        // Установка взаимосвязи
        try{
            // ServerSocket + захват порта
            ServerSocket listener = new ServerSocket(5678);
            System.out.println("Server works");
            while(true)
            {
                // Socket - виртуальный канал, по которому общение
                Socket client = listener.accept();
                Game_controller game_controller = new Game_controller();
                System.out.println("Client connects");
                new ClientDialog(client, game_controller).start(); //создание отдельного потока для клиента
            }
        }
        catch (Exception exception) //если вдруг порт захвачен уже
        {
            System.out.println(exception.getMessage());
        }
    }

    static class ClientDialog extends Thread
    {
        Socket client;
        Game_controller game_controller;
        public ClientDialog(Socket client, Game_controller game_controller)
        {
            this.client = client;
            this.game_controller = game_controller;
        }

        public void run() // пока выполняется, поток живет
        {
            while (true)
            {
                try
                {
                    DataInputStream inputStream = new DataInputStream(client.getInputStream()); // получаем инфу
                    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());  // передаем инфу

                    String comand = inputStream.readUTF();
                    switch (comand)
                    {
                        case "newboxrand":
                            int size_y = inputStream.readInt();
                            int size_x = inputStream.readInt();
                            game_controller.create_new_life_box(size_y, size_x);
                            break;
                        case "newboxlife":
                            int size_y_ = inputStream.readInt();
                            int size_x_ = inputStream.readInt();
                            int life = inputStream.readInt();
                            game_controller.create_new_life_box(size_y_, size_x_, life);
                            break;
                        case "newgener":
                            game_controller.get_new_generation();
                            break;
                        case "newgenerno":
                            game_controller.get_new_generation_no_border();
                            break;
                        case "cell":
                            int i = inputStream.readInt();
                            int j = inputStream.readInt();
                            boolean is_alive = Game_controller.is_cell_alive(i, j);
                            outputStream.writeBoolean(is_alive);
                            break;
                        case "finish":
                            System.out.println("Client disconnects");
                            return; //выход из бесконечного цикла приема сообщений
                    }
                }
                catch (Exception exception) { System.out.println(exception.getMessage()); }
            }
        }
    }
}
