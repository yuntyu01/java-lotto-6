package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        try {
            List<Lotto> lottoNumbers = new ArrayList<>();

            int amount = getAmount();
            int chance = amount / 1000;

            System.out.println(chance + "개를 구매했습니다.");
            generateLottoNumbers(lottoNumbers, chance);

            int[] winningNumbers = inputWinningNumbers();
            int bonusNumber = inputBonusNumber();

            int[] results = calculateResults(lottoNumbers, winningNumbers, bonusNumber);
            printResults(results, amount);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static int getAmount() {
        System.out.println("구입금액을 입력해 주세요.");
        String input = Console.readLine();
        try {
            int amount = Integer.parseInt(input);
            if (amount % 1000 != 0) {
                throw new IllegalArgumentException("구입 금액은 1,000원 단위여야 합니다.");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바른 숫자를 입력해 주세요.");
        }
    }

    private static void generateLottoNumbers(List<Lotto> lottoNumbers, int chance) {
        for (int i = 0; i < chance; i++) {
            Lotto lotto = new Lotto(Randoms.pickUniqueNumbersInRange(1, 45, 6));
            lottoNumbers.add(lotto);
            System.out.println(lotto.getLottoValues());
        }
    }

    private static int[] inputWinningNumbers() {
        System.out.println("당첨 번호를 입력해 주세요:");
        String input = Console.readLine();
        try {
            String[] nums = input.split(",");
            if (nums.length != 6) {
                throw new IllegalArgumentException("6개의 당첨 번호를 입력해야 합니다.");
            }
            int[] winningNumbers = new int[6];
            for (int i = 0; i < nums.length; i++) {
                winningNumbers[i] = Integer.parseInt(nums[i].trim());
            }
            return winningNumbers;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("당첨 번호는 숫자여야 합니다.");
        }
    }

    private static int inputBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요:");
        String input = Console.readLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("보너스 번호는 숫자여야 합니다.");
        }
    }

    private static int[] calculateResults(List<Lotto> lottoNumbers, int[] winningNumbers, int bonusNumber) {
        int[] containCount = new int[7];
        int bonusMatchCount = 0;

        for (Lotto lottoNumber : lottoNumbers) {
            int matchCount = countMatches(lottoNumber, winningNumbers);
            boolean hasBonus = lottoNumber.getLottoValues().contains(bonusNumber);

            if (matchCount >= 3) {
                containCount[matchCount]++;
                if (matchCount == 5 && hasBonus) {
                    bonusMatchCount++;
                }
            }
        }
        containCount[6] = bonusMatchCount;
        return containCount;
    }

    private static int countMatches(Lotto lotto, int[] winningNumbers) {
        int count = 0;
        for (int number : winningNumbers) {
            if (lotto.getLottoValues().contains(number)) {
                count++;
            }
        }
        return count;
    }

    private static void printResults(int[] results, int totalAmount) {
        System.out.println("당첨 통계");
        System.out.println("---");
        System.out.println("3개 일치 (5,000원) - " + results[3] + "개");
        System.out.println("4개 일치 (50,000원) - " + results[4] + "개");
        System.out.println("5개 일치 (1,500,000원) - " + results[5] + "개");
        System.out.println("5개 일치, 보너스 볼 일치 (30,000,000원) - " + results[6] + "개");
        System.out.println("6개 일치 (2,000,000,000원) - " + results[6] + "개");

        double totalRevenue = calculateTotalRevenue(results);
        double profitRate = (totalRevenue / totalAmount) * 100;
        System.out.printf("총 수익률은 %.1f%%입니다.%n", profitRate);
    }


    private static double calculateTotalRevenue(int[] results) {
        int[] rewards = {0, 0, 0, 5000, 50000, 1500000, 30000000, 2000000000};
        double totalRevenue = 0;
        for (int i = 3; i <= 6; i++) {
            totalRevenue += results[i] * rewards[i];
        }
        return totalRevenue;
    }
}
