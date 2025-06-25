package cz.itnetwork;

/**
 * Třída Pojistenec představuje pojištěnce v systému.
 * Obsahuje základní informace o pojištěnci, jako je jméno, příjmení, věk a telefonní číslo.
 */
public class Pojistenec {

    final String jmeno;
    final String prijmeni;
    final int vek;
    final String telefonniCislo;

    /**
     * Konstruktor pro vytvoření nového pojištěnce.
     * @param jmeno Jméno pojištěnce.
     * @param prijmeni Příjmení pojištěnce.
     * @param vek Věk pojištěnce.
     * @param telefonniCislo Telefonní číslo pojištěnce.
     */
    public Pojistenec(String jmeno, String prijmeni, int vek, String telefonniCislo) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.vek = vek;
        this.telefonniCislo = telefonniCislo;
    }

    public String getJmeno() {
        return this.jmeno;
    }

    public String getPrijmeni() {
        return this.prijmeni;
    }

    @Override
    public String toString() {
        return jmeno + " " + prijmeni + ", (" + vek + " roků), " + telefonniCislo + " ";
    }
}
