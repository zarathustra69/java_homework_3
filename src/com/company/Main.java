import java.io.Console;
import java.util.Random;
import java.util.Scanner;

public class Main {
    // 3. Определяем размеры массива
    static final int SIZE_X = 5;
    static final int SIZE_Y = 5;
    static final int WIN_COUNT = 4;

    // 1. Создаем двумерный массив
    static char[][] field = new char[SIZE_Y][SIZE_X];

    // 2. Обозначаем кто будет ходить какими фишками
    static final char PLAYER_DOT = 'X';
    static final char AI_DOT = '0';
    static final char EMPTY_DOT = '.';

    // 8. Создаем сканер
    static Scanner scanner = new Scanner(System.in);
    // 12. Создаем рандом
    static final Random rand = new Random();

    // 4. Заполняем на массив
    private static void initField() {
        for(int i = 0; i < SIZE_Y; i++) {
            for(int j = 0; j < SIZE_X; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }

    // 5. Выводим на массив на печать
    private static void printField() {
        //6. украшаем картинку
        System.out.println("-------");
        for(int i = 0; i < SIZE_Y; i++) {
            System.out.print("|");
            for(int j = 0; j < SIZE_X; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
        //6. украшаем картинку
        System.out.println("-------");
    }

    // 7. Метод который устанавливает символ
    private static void setSym(int y, int x, char sym){
        field[y][x] = sym;
    }

    // 9. Ход игрока
    private static void playerStep() {
        // 11. с проверкой
        int x;
        int y;
        do {
            System.out.println("Введите координаты: X Y (1-3)");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(y,x));
        setSym(y, x, PLAYER_DOT);

    }

    // 13. Ход ПК
    private static void aiStep() { //REWRITE
        int x = -1;
        int y = -1;
        int enemy_x = -1;
        int enemy_y = -1;
        for(int i = 0; i<SIZE_Y; i++){
            for(int j = 0;j<SIZE_X; j++){
                if(isCellValid(i,j)){
                    setSym(i,j,PLAYER_DOT);
                    if (checkWin(PLAYER_DOT)){ //проверяет, есть ли выигрышная позиция для игрока
                        enemy_y = i;
                        enemy_x = j;
                    }
                    setSym(i,j,EMPTY_DOT);

                    setSym(i,j,AI_DOT); //проверяет, есть ли выигрышная позиция для себя
                    if (checkWin(AI_DOT)){
                        y = i;
                        x = j;
                    }
                    setSym(i,j,EMPTY_DOT);
                }
            }
        }
        if(x != -1 && y != -1){ //ставит в выигрышную позицию для себя
            setSym(y,x,AI_DOT);
            return;
        }
        if(enemy_x != -1 && enemy_y != -1){ //блокирует выигрышную позицию для врага
            setSym(enemy_y,enemy_x,AI_DOT);
            return;
        }
        do {
            x = rand.nextInt(SIZE_X);
            y = rand.nextInt(SIZE_Y);
        } while (!isCellValid(y, x));
        setSym(y, x, AI_DOT);

    }

    private static boolean checkHorizontal(char sym) {
        for(int i = 0; i<SIZE_Y; i++) {
            int streak = 0;
            for (int j = 0; j < SIZE_X; j++) {
                if(field[i][j] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
            }
        }
        return false;
    }

    private static boolean checkVertical(char sym) {
        for(int i = 0; i<SIZE_X; i++) {
            int streak = 0;
            for (int j = 0; j < SIZE_Y; j++) {
                if(field[j][i] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
            }
        }
        return false;
    }

    private static boolean checkDiagonal(char sym) {
        int offset_x = SIZE_X - WIN_COUNT;
        int offset_y = SIZE_Y - WIN_COUNT;

        //слева-направо
        for(int i = 0; i<=offset_x; i++){ //основная диагональ и выше
            int streak = 0;
            int upper_border = SIZE_X - i;
            if(upper_border>SIZE_Y) upper_border = SIZE_Y;
            for(int j = 0; j<upper_border; j++){
                if(field[j][i+j] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
                //System.out.println("ПРОВЕРЯЮ ЯЧЕЙКУ "+(i+j+1)+" "+(j+1) + " " + streak + " ДЛЯ " + sym);
            }
        }

        for(int i = 1; i<=offset_y; i++){ //ниже основной диагонали
            int streak = 0;
            int upper_border = SIZE_Y - i;
            if(upper_border>SIZE_X) upper_border = SIZE_X;
            for(int j = 0; j<upper_border; j++){
                if(field[i+j][j] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
                //System.out.println("ПРОВЕРЯЮ ЯЧЕЙКУ "+(j+1)+" "+(i+j+1) + " " + streak + " ДЛЯ " + sym);
            }
        }

        //справа-налево

        for(int i = 0; i<=offset_x; i++){ //побочная диагональ и выше
            int streak = 0;
            int upper_border = SIZE_X - i;
            if(upper_border>SIZE_Y) upper_border = SIZE_Y;
            for(int j = 0; j<upper_border; j++){
                if(field[j][SIZE_X - i - j - 1] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
                //System.out.println("ПРОВЕРЯЮ ЯЧЕЙКУ "+(SIZE_X - i - j )+" "+(j + 1) + " " + streak + " ДЛЯ " + sym);
            }
        }


        for(int i = 1; i<=offset_y; i++){ //ниже побочной диагонали
            int streak = 0;
            int upper_border = SIZE_Y - i;
            if(upper_border>SIZE_X) upper_border = SIZE_X;
            for(int j = 0; j<upper_border; j++){
                if(field[i+j][SIZE_X - j - 1] == sym){
                    streak++;
                    if (streak == WIN_COUNT) return true;
                }
                else streak = 0;
                //System.out.println("ПРОВЕРЯЮ ЯЧЕЙКУ "+(SIZE_X - j)+" "+(i+j+1) + " " + streak + " ДЛЯ " + sym);
            }
        }

        return false;
    }

    // 14. Проверка победы
    private static boolean checkWin(char sym) { //REWRITE
        if(checkHorizontal(sym)) return true;
        if(checkVertical(sym)) return true;
        if(checkDiagonal(sym)) return true;
        /* if (field[0][0] == sym && field[0][1] == sym && field[0][2] == sym) {
            return true;
        }
        if (field[1][0] == sym && field[1][1] == sym && field[1][2] == sym) {
            return true;
        }
        if (field[2][0] == sym && field[2][1] == sym && field[2][2] == sym) {
            return true;
        }

        if (field[0][0] == sym && field[1][0] == sym && field[2][0] == sym) {
            return true;
        }
        if (field[0][1] == sym && field[1][1] == sym && field[2][1] == sym) {
            return true;
        }
        if (field[0][2] == sym && field[1][2] == sym && field[2][2] == sym) {
            return true;
        }


        if (field[0][0] == sym && field[1][1] == sym && field[2][2] == sym) {
            return true;
        }
        if (field[2][0] == sym && field[1][1] == sym && field[0][2] == sym) {
            return true;
        } */
        return false;
    }

    // 16. Проверка полное ли поле? возможно ли ходить?
    private static boolean isFieldFull() {
        for (int i = 0; i < SIZE_Y; i++) {
            for(int j = 0; j < SIZE_X; j++) {
                if(field[i][j] == EMPTY_DOT) {
                    return false;
                }
            }
        }
        return true;
    }

    // 10. Проверяем возможен ли ход
    private static boolean isCellValid(int y, int x) {
        // если вываливаемся за пределы возвращаем false
        if(x < 0 || y < 0 || x > SIZE_X -1 || y > SIZE_Y - 1) {
            return false;
        }
        // если не путое поле тоже false
        return (field[y][x] == EMPTY_DOT);
    }

    public static void main(String[] args) {
        // 1 - 1 иницируем и выводим на печать
        initField();
        printField();
        // 1 - 1 иницируем и выводим на печать

        // 15 Основной ход программы

        while (true) {
            playerStep();
            printField();
            if(checkWin(PLAYER_DOT)) {
                System.out.println("Player WIN!");
                break;
            }
            if(isFieldFull()) {
                System.out.println("DRAW");
                break;
            }

            aiStep();
            printField();
            if(checkWin(AI_DOT)) {
                System.out.println("Win SkyNet!");
                break;
            }
            if(isFieldFull()) {
                System.out.println("DRAW!");
                break;
            }
        }
    }
}
