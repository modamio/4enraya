import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tauler implements Serializable {
    public Map<String,Integer> map_jugadors;
    public int resultat = 3, acabats;
    private int numPlayers;
    public String[][] board;

    public String ultimoTurno;



    public Tauler(){
        map_jugadors = new HashMap<>();
        acabats = 0;
        board = new String[6][7];
    }
    public int getNumPlayers() {
        return numPlayers;
    }

    public void addNUmPlayers() {
        this.numPlayers++;
    }

    @Override
    public String toString() {
        String tablero = "";
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 7;j++){
                if (board[i][j]== null){
                    tablero +="[ ]";
                }
                else{
                    tablero +="[" + board[i][j] + "]";
                }

            }
            tablero += "\n";
        }
        return tablero;
    }

}

class Jugada implements Serializable {
    String jugador;
    int columna;
}