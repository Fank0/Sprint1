import java.util.Random;
import java.util.Scanner;

public class IllusionistMonster extends Monster {

    IllusionistMonster(int sizeBoard) {
        super(sizeBoard);
        setImage("🎭");
    }

    @Override
    public boolean taskMonster(int difficultGame) {
        Random random = new Random();

        int max = 10 + (difficultGame - 1) * 10;
        int a = random.nextInt(max) + 1;
        int b = random.nextInt(max) + 1;
        int correct = a + b;

        int wrong1;
        do {
            wrong1 = correct + (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(3));
        } while (wrong1 == correct || wrong1 < 0);

        int wrong2;
        do {
            wrong2 = correct + (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(3));
        } while (wrong2 == correct || wrong2 == wrong1 || wrong2 < 0);

        int[] options = new int[3];
        int[] wrongs = {wrong1, wrong2};
        int correctIndex = random.nextInt(3);
        int wrongIdx = 0;
        for (int i = 0; i < 3; i++) {
            if (i == correctIndex) {
                options[i] = correct;
            } else {
                options[i] = wrongs[wrongIdx++];
            }
        }

        System.out.println("🎭 Иллюзионист создаёт иллюзию!");
        System.out.println("Выберите верный ответ: " + a + " + " + b + " = ?");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ") " + options[i]);
        }
        System.out.print("Ваш ответ (1-3): ");

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            System.out.println("Неверный ввод — иллюзия сбила вас с толку.");
            return false;
        }
        int answer = scanner.nextInt();

        if (answer >= 1 && answer <= 3 && options[answer - 1] == correct) {
            System.out.println("Верно! Иллюзия разрушена!");
            return true;
        } else {
            System.out.println("Неверно! Иллюзия сбила вас с толку.");
            return false;
        }
    }
}