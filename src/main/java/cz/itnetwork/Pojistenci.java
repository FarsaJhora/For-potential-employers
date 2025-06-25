package cz.itnetwork;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Třída Pojistenci spravuje seznam pojištěnců a poskytuje metody pro přidávání,
 * vyhledávání a vypisování pojištěnců.
 */
public class Pojistenci {
    public ArrayList<Pojistenec> pojistenci;
    private Scanner scanner;

    /**
     * Konstruktor pro inicializaci seznamu pojištěnců a skeneru pro vstup od uživatele.
     */
    public Pojistenci() {
        this.pojistenci = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Metoda pro přidání nového pojištěnce do seznamu.
     * Uživatel je vyzván k zadání jména, příjmení, věku a telefonního čísla pojištěnce.
     * Po zadání údajů je pojištěnec přidán do seznamu a uživatel je informován o úspěšném uložení dat.
     */
    public void pridatPojistence() {
        System.out.println("Zadejte jméno pojištěnce:");
        String jmeno = scanner.nextLine();
        System.out.println("Zadejte příjmení pojištěnce:");
        String prijmeni = scanner.nextLine();
        System.out.println("Zadejte věk pojištěnce:");
        int vek = Integer.parseInt(scanner.nextLine());
        System.out.println("Zadejte telefonní číslo pojištěnce:");
        String telCislo = scanner.nextLine();
        System.out.println();
        this.pojistenci.add(new Pojistenec(jmeno, prijmeni, vek, telCislo));
        System.out.println("Data byla uložena. Pokračujte libovolnou klávesou ...");
    }

    /**
     * Metoda pro vyhledání pojištěnce podle jména a příjmení.
     * Uživatel je vyzván k zadání jména a příjmení pojištěnce.
     * Program prohledá seznam pojištěnců a pokud nalezne shodu, vypíše informace o pojištěnci.
     * Pokud pojištěnec není nalezen, uživatel je o tom informován.
     */
    public void vyhledatPojistence() {
        System.out.println("Zadejte jméno pojištěnce:");
        String jmeno = scanner.nextLine();
        System.out.println("Zadejte příjmení pojištěnce:");
        String prijmeni = scanner.nextLine();
        for (Pojistenec pojistenec : pojistenci) {
            if (pojistenec.getJmeno().equalsIgnoreCase(jmeno) && pojistenec.getPrijmeni().equalsIgnoreCase(prijmeni)) {
                System.out.println("Pojištěnec nalezen: " + pojistenec);
                return;
            }
        }
        System.out.println("Pojištěnec nebyl nalezen.");
    }

    /**
     * Metoda pro výpis všech pojištěnců v databázi.
     * Pokud databáze neobsahuje žádné pojištěnce, uživatel je o tom informován.
     */
    public void vypisPojistencu() {
        if (pojistenci.isEmpty()) {
            System.out.println("V databázi nejsou žádní pojištěnci.");
            return;
        }
        System.out.println("Výpis pojištěnců:");
        for (Pojistenec pojistenec : pojistenci) {
            System.out.println(pojistenec);
        }
    }
}
