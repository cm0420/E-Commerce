package com.miguel.ecommerce;
import java.util.Random;
public class TestUtils {
    private static final Random RANDOM = new Random();

    // Gera um e-mail aleatório para evitar "Email already exists"
    public static String generateRandomEmail() {
        return "test" + RANDOM.nextInt(1000000) + "@test.com";
    }

    // Gera um CPF válido de 11 dígitos (apenas números) para o seu @Size(11,11)
    public static String generateRandomCpf() {
        int[] cpf = new int[11];

        // Gera os 9 primeiros dígitos
        for (int i = 0; i < 9; i++) {
            cpf[i] = RANDOM.nextInt(10);
        }

        // Calcula o 1º dígito verificador
        cpf[9] = calculateDigit(cpf, 10);
        // Calcula o 2º dígito verificador
        cpf[10] = calculateDigit(cpf, 11);

        StringBuilder sb = new StringBuilder();
        for (int digit : cpf) {
            sb.append(digit);
        }
        return sb.toString();
    }

    private static int calculateDigit(int[] cpf, int weight) {
        int sum = 0;
        for (int i = 0; i < weight - 1; i++) {
            sum += cpf[i] * (weight - i);
        }
        int remainder = 11 - (sum % 11);
        return (remainder > 9) ? 0 : remainder;
    }
}
