package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket socket_server;
    DataOutputStream outputStream;
    DataInputStream inputStream;

    public Client() throws IOException
    {
        this.socket_server = new Socket("localhost",5678);
        outputStream = new DataOutputStream(socket_server.getOutputStream());
        inputStream = new DataInputStream(socket_server.getInputStream());
    }

    public void close_primary_stage()
    {
        try
        {
            outputStream.writeUTF("finish");
            outputStream.flush();
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }
    }

    public void new_game_rand(int size_y, int size_x)
    {
        try
        {
            outputStream.writeUTF("newboxrand");
            outputStream.flush();
            outputStream.writeInt(size_y);
            outputStream.writeInt(size_x);
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }
    }

    public void new_game_life(int size_y, int size_x, int life)
    {
        try
        {
            outputStream.writeUTF("newboxlife");
            outputStream.flush();
            outputStream.writeInt(size_y);
            outputStream.writeInt(size_x);
            outputStream.writeInt(life);
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }
    }

    public void new_generation()
    {
        try
        {
            outputStream.writeUTF("newgener");
            outputStream.flush();
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }
    }

    public void new_generation_noborder()
    {
        try
        {
            outputStream.writeUTF("newgenerno");
            outputStream.flush();
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }
    }

    public boolean cell(int i, int j)
    {
        boolean is_alive = false;
        try
        {
            outputStream.writeUTF("cell");
            outputStream.flush();
            outputStream.writeInt(i);
            outputStream.writeInt(j);

            is_alive = inputStream.readBoolean();
        }
        catch (Exception exception) { System.out.println(exception.getMessage()); }

        return is_alive;
    }
}
