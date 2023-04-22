import java.util.Arrays;

public class QuatroEnRaya {

    public String ganador;
    private int numMovimientos;
    public QuatroEnRaya(){

    }
    public int estat(Jugada jugada,Tauler tauler){
        omple(jugada,tauler);
        if (checkForWin(tauler)){
            System.out.println("El ganador es el player " + ganador);
            return 0;
        }else if (numMovimientos == 42){
            System.out.println("Empate");
            return 1;
        }else {System.out.println("Continua jugant");
            return 2;}

    }
    private boolean checkFila(Tauler tauler) {
        // Verificar todas las filas
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 3; j++) {
                if (tauler.board[i][j] != null &&
                        tauler.board[i][j].equals(tauler.board[i][j + 1]) &&
                        tauler.board[i][j].equals(tauler.board[i][j + 2]) &&
                        tauler.board[i][j].equals(tauler.board[i][j + 3])) {
                    ganador = tauler.board[i][j];
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkColumn(Tauler tauler) {
        // Verificar todas las columnas
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j <= 2; j++) {
                if (tauler.board[j][i] != null &&
                        tauler.board[j][i].equals(tauler.board[j + 1][i]) &&
                        tauler.board[j][i].equals(tauler.board[j + 2][i]) &&
                        tauler.board[j][i].equals(tauler.board[j + 3][i])) {
                    ganador = tauler.board[i][j];
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals(Tauler tauler) {
        // Verificar diagonal descendente (de izquierda a derecha)
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 3; j++) {
                if (tauler.board[i][j] != null &&
                        tauler.board[i][j].equals(tauler.board[i + 1][j + 1]) &&
                        tauler.board[i][j].equals(tauler.board[i + 2][j + 2]) &&
                        tauler.board[i][j].equals(tauler.board[i + 3][j + 3])) {
                    ganador = tauler.board[i][j];
                    return true;
                }
            }
        }

        // Verificar diagonal ascendente (de derecha a izquierda)
        for (int i = 0; i <= 2; i++) {
            for (int j = 6; j >= 3; j--) {
                if (tauler.board[i][j] != null &&
                        tauler.board[i][j].equals(tauler.board[i + 1][j - 1]) &&
                        tauler.board[i][j].equals(tauler.board[i + 2][j - 2]) &&
                        tauler.board[i][j].equals(tauler.board[i + 3][j - 3])) {
                    ganador = tauler.board[i][j];
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkForWin(Tauler tauler) {

        // Mira las filas
        if (checkFila(tauler)) {
            return true;
        }
        // Mira las columnas
        if (checkColumn(tauler)) {
            return true;
        }
        if (checkDiagonals(tauler)) {
            return true;
        }
        return false;
    }
    public void omple(Jugada jugada,Tauler tauler){
        if (!jugada.jugador.equals(tauler.ultimoTurno)){
            int colIndex = jugada.columna; // Encuentra la columna del botón
            int rowIndex = -1;
            for (int i = 5; i >= 0; i--) { // Busca la primera posición vacía en la columna
                if (tauler.board[i][colIndex] == null) {
                    rowIndex = i;
                    break;
                }
            }
            if (rowIndex != -1) { // Si la columna no está llena, marca el botón
                tauler.ultimoTurno = jugada.jugador;
                tauler.board[rowIndex][colIndex] = jugada.jugador;
                numMovimientos++;
            }
        }


    }


}
