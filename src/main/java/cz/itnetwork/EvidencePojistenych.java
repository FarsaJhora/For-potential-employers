package cz.itnetwork;

import java.util.Scanner;

/**
 * Třída EvidencePojistenych slouží jako hlavní třída pro správu evidence pojištěnců.
 * Umožňuje uživateli přidávat nové pojištěnce, vyhledávat je a vypisovat seznam všech pojištěnců.
 * Program běží v konzoli a poskytuje jednoduché uživatelské rozhraní pro interakci s uživatelem.
 */
public class EvidencePojistenych {
    private Scanner scanner = new Scanner(System.in);

    /**
     * Instance třídy Pojistenci, která spravuje seznam pojištěnců.
     */
    private Pojistenci pojistenci = new Pojistenci();

    /**
     * Hlavní metoda pro spuštění evidence pojištěných.
     */
    public static void main(String[] args) {
        System.out.println(" Evidence pojištěných: ");
        
        /**
         * Vytvoří instanci EvidencePojistenych a spustí program.
         */
        EvidencePojistenych evidence = new EvidencePojistenych();
        evidence.spustit();
    }

    /**
     * Metoda pro spuštění hlavní smyčky programu.
     * Tato metoda zobrazuje menu a zpracovává uživatelské volby.
     * Uživatel může přidávat nové pojištěnce, vypisovat všechny pojištěnce,
     * vyhledávat pojištěnce nebo ukončit program.
     * Cyklus běží, dokud uživatel nezvolí možnost ukončení programu (volba 4).
     * Pokud uživatel zadá neplatnou volbu, program ho vyzve k zadání platné volby.
     * Pokud uživatel zadá neplatný vstup (např. text místo čísla), program ho upozorní
     * a požádá o zadání čísla.
     */
    private void spustit() {
        int volba;
        do {
            zobrazitMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Zadejte číslo.");
                scanner.next(); 
            }
            volba = scanner.nextInt();
            scanner.nextLine(); 
            zpracovatVolbu(volba);
        } while (volba != 4);
    }

    /**
     * Metoda pro zobrazení hlavního menu programu.
     * Zobrazí uživateli dostupné akce, které může provést.
     */
    private void zobrazitMenu() {
        System.out.println("\nVyberte si akci:");
        System.out.println("1 - Přidat nového pojištěnce");
        System.out.println("2 - Vypsat všechny pojištěnce");
        System.out.println("3 - Vyhledat pojištěnce");
        System.out.println("4 - Konec");
    }

    /**
     * Metoda pro zpracování uživatelské volby.
     * @param volba Číslo volby z hlavního menu.
     */
    private void zpracovatVolbu(int volba) {
        switch (volba) {
            case 1 -> pojistenci.pridatPojistence();
            case 2 -> pojistenci.vypisPojistencu();
            case 3 -> pojistenci.vyhledatPojistence();
            case 4 -> System.out.println("Ukončuji program.");
            default -> System.out.println("Neplatná volba, zkuste to prosím znovu.");
        }
    }
}