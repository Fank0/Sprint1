import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String castle = "\uD83C\uDFF0";

        int sizeBoard = 5;
        int step = 0;

        Person person = new Person(sizeBoard);

        Random random = new Random();
        int castleX = 1 + random.nextInt(sizeBoard);
        int castleY = 1;

        String[][] board = new String[sizeBoard][sizeBoard];
        for (int y = 1; y <= sizeBoard; y++) {
            for (int x = 1; x <= sizeBoard; x++) {
                board[y - 1][x - 1] = "  ";
            }
        }

        int countMonster = sizeBoard * sizeBoard - sizeBoard - 1;
        Monster[] monsters = new Monster[countMonster];
        int count = 0;

        while (count < countMonster) {
            Monster monster;

            int type = random.nextInt(3);
            if (type == 0) {
                monster = new Monster(sizeBoard);
            } else if (type == 1) {
                monster = new BigMonster(sizeBoard);
            } else {
                monster = new IllusionistMonster(sizeBoard);
            }

            int row = monster.getY() - 1;
            int column = monster.getX() - 1;

            if (board[row][column].equals("  ")) {
                board[row][column] = monster.getImage();
                monsters[count] = monster;
                count++;
            }
        }

        board[castleY - 1][castleX - 1] = castle;

        System.out.println("Привет! Ты готов начать играть в игру? (Напиши: ДА или НЕТ)");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        System.out.println("Ваш ответ:\t" + answer);

        switch (answer.trim().toUpperCase()) {
            case "ДА":
                System.out.println("Выбери сложность игры (от 1 до 5):");
                int difficultGame = scanner.nextInt();
                System.out.println("Выбранная сложность:\t" + difficultGame);

                while (true) {
                    board[person.getY() - 1][person.getX() - 1] = person.getImage();
                    outputBoard(board, person.getLive());

                    System.out.println("Введите команду (W - вверх, A - влево, S - вниз, D - вправо)"
                            + "\nКоординаты персонажа — (x: " + person.getX()
                            + ", y: " + person.getY() + ")");

                    String command = scanner.next().toUpperCase();
                    int dx = 0, dy = 0;
                    switch (command) {
                        case "W": dy = -1; break;
                        case "S": dy = 1;  break;
                        case "A": dx = -1; break;
                        case "D": dx = 1;  break;
                        default:
                            System.out.println("Неверная команда. Используйте W, A, S, D.");
                            continue;
                    }

                    int x = person.getX() + dx;
                    int y = person.getY() + dy;
                    System.out.println(x + ", " + y);

                    if (x >= 1 && x <= sizeBoard && y >= 1 && y <= sizeBoard
                            && person.isMoveCorrect(x, y)) {
                        if (board[y - 1][x - 1].equals("  ")) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;

                            System.out.println("Ход корректный; новые координаты: "
                                    + person.getX() + ", " + person.getY()
                                    + "\nХод номер: " + step);
                        } else if (board[y - 1][x - 1].equals(castle)) {
                            System.out.println("Вы прошли игру!");
                            break;
                        } else {
                            for (int i = 0; i < monsters.length; i++) {
                                Monster monster = monsters[i];
                                if (monster != null && monster.conflictPerson(x, y)) {
                                    if (monster.taskMonster(difficultGame)) {
                                        board[person.getY() - 1][person.getX() - 1] = "  ";
                                        person.move(x, y);
                                        step++;

                                        if (monster instanceof BigMonster && person.getLive() < 3) {
                                            person.upLive();
                                            System.out.println("Победа над большим монстром восстанавливает 1 жизнь!");
                                        }

                                        monsters[i] = null;
                                    } else {
                                        person.downLive();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("Некорректный ход");
                    }

                    if (person.getLive() <= 0) {
                        break;
                    }
                }

                if (person.getLive() <= 0) {
                    System.out.println("Закончились жизни. Итог: ...");
                }
                break;

            case "НЕТ":
                System.out.println("Жаль, приходи ещё!");
                break;

            default:
                System.out.println("Данные введены некорректно");
                break;
        }
    }

    static void outputBoard(String[][] board, int live) {
        String leftBlock = "| ";
        String rightBlock = "|";
        String wall = "+ —— + —— + —— + —— + —— +";

        for (String[] row : board) {
            System.out.println(wall);
            for (String col : row) {
                System.out.print(leftBlock + col + " ");
            }
            System.out.println(rightBlock);
        }
        System.out.println(wall);

        System.out.println("Количество жизней:\t" + live + "\n");
    }
}